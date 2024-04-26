package com.skillstorm.services;

import com.skillstorm.dtos.*;
import com.skillstorm.entities.Credit;
import com.skillstorm.entities.User;
import com.skillstorm.entities.UserCredit;
import com.skillstorm.entities.UserDeduction;
import com.skillstorm.exceptions.CreditNotFoundException;
import com.skillstorm.exceptions.DeductionNotFoundException;
import com.skillstorm.exceptions.UserNotFoundException;
import com.skillstorm.repositories.UserCreditRepository;
import com.skillstorm.repositories.UserDeductionRepository;
import com.skillstorm.repositories.UserRepository;
import com.skillstorm.configs.SystemMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@PropertySource("classpath:SystemMessages.properties")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCreditRepository userCreditRepository;
    private final UserDeductionRepository userDeductionRepository;
    private final RestTemplate restTemplate;
    private final String creditsUrl;
    private final String deductionsUrl;
    private final Environment environment;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserCreditRepository userCreditRepository, UserDeductionRepository userDeductionRepository, RestTemplate restTemplate,
                           @Value("${credits.url}") String creditsUrl, @Value("${deductions.url}") String deductionsUrl, Environment environment) {
        this.userRepository = userRepository;
        this.userCreditRepository = userCreditRepository;
        this.userDeductionRepository = userDeductionRepository;
        this.restTemplate = restTemplate;
        this.creditsUrl = creditsUrl;
        this.deductionsUrl = deductionsUrl;
        this.environment = environment;
    }

    // Add new User:
    @Override
    public UserDto addUser(UserDto newUser) {
        return new UserDto(userRepository.saveAndFlush(newUser.getUser()));
    }

    // Find User by ID:
    @Override
    public UserDto findUserById(int id) {
        return userRepository.findById(id)
                .map(UserDto::new)
                .orElseThrow(() -> new UserNotFoundException(environment.getProperty(SystemMessages.USER_NOT_FOUND.toString())));
    }

    // Find User by Username:
    @Override
    public UserDto findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserDto::new)
                .orElseThrow(() -> new UserNotFoundException(environment.getProperty(SystemMessages.USER_NOT_FOUND.toString())));
    }

    // Update User by ID:
    @Override
    public UserDto updateUserById(int id, UserDto updatedUser) {
        findUserById(id);
        updatedUser.setId(id);
        return new UserDto(userRepository.saveAndFlush(updatedUser.getUser()));
    }

    // Delete User by ID:
    @Override
    public void deleteUserById(int id) {
        findUserById(id);
        userRepository.deleteById(id);
    }

    // Add Tax Credit to User:
    @Override
    public UserCreditDto addTaxCredit(int id, UserCreditDto userCreditDto) {

        // Verify User exists:
        User user = findUserById(id).getUser();

        // Make a GET request to the credits service to get the credit with the given ID:
        CreditDto creditDto;
        try {
            creditDto = restTemplate.getForObject(creditsUrl + "/" + userCreditDto.getCreditId(), CreditDto.class);
        } catch (HttpClientErrorException e) {
            throw new CreditNotFoundException(environment.getProperty(SystemMessages.CREDIT_NOT_FOUND.toString()));
        }

        // Multiply the value of the credit by the number of credits claimed by the user to get the total value of the UserCredit:
        UserCredit userCredit = userCreditDto.getUserCredit();
        BigDecimal totalValue = creditDto.getValue().multiply(BigDecimal.valueOf(userCredit.getCreditsClaimed()));
        userCredit.setTotalValue(totalValue.setScale(2, RoundingMode.HALF_UP));

        userCredit.setUser(user);
        userCredit.setCredit(creditDto.getCredit());

        return new UserCreditDto(userCreditRepository.saveAndFlush(userCredit));
    }

    // Find all UserCredits by User ID:
    @Override
    public List<UserCreditDto> findAllCreditsByUserId(int id) {
        findUserById(id);
        return userCreditRepository.findAllByUserId(id).stream().map(UserCreditDto::new).toList();
    }

    // Add Deduction to User:
    @Override
    public UserDeductionDto addDeduction(int id, UserDeductionDto userDeductionDto) {
        // Verify User exists:
        User user = findUserById(id).getUser();

        // Make a GET request to the deductions service to get the deduction with the given ID:
        DeductionDto deductionDto;
        try {
            deductionDto = restTemplate.getForObject(deductionsUrl + "/" + userDeductionDto.getDeductionId(), DeductionDto.class);
        } catch (HttpClientErrorException e) {
            throw new DeductionNotFoundException(environment.getProperty(SystemMessages.DEDUCTION_NOT_FOUND.toString()));
        }

        // Use the deduction's rate to calculate the total value of the UserDeduction based on the amount spent by the user:
        UserDeduction userDeduction = userDeductionDto.getUserDeduction();
        userDeduction.setDeductionAmount(userDeduction.getAmountSpent().multiply(deductionDto.getRate()));
        userDeduction.setUser(user);
        userDeduction.setDeduction(deductionDto.getDeduction());

        return new UserDeductionDto(userDeductionRepository.saveAndFlush(userDeduction));
    }

    // Find all UserDeductions by User ID:
    @Override
    public List<UserDeductionDto> findAllDeductionsByUserId(int id) {
        findUserById(id);
        return userDeductionRepository.findAllByUserId(id).stream().map(UserDeductionDto::new).toList();
    }
}
