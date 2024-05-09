package com.skillstorm.services;

import com.skillstorm.dtos.*;
import com.skillstorm.entities.User;
import com.skillstorm.entities.UserCredit;
import com.skillstorm.entities.UserDeduction;
import com.skillstorm.exceptions.UserNotFoundException;
import com.skillstorm.repositories.UserCreditRepository;
import com.skillstorm.repositories.UserDeductionRepository;
import com.skillstorm.repositories.UserRepository;
import com.skillstorm.configs.SystemMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@PropertySource("classpath:SystemMessages.properties")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserCreditRepository userCreditRepository;
    private final UserDeductionRepository userDeductionRepository;
    private final CreditService creditService;
    private final DeductionService deductionService;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    @Autowired
    public UserServiceImpl (UserRepository userRepository, UserCreditRepository userCreditRepository,
                            UserDeductionRepository userDeductionRepository, CreditService creditService,
                            DeductionService deductionService, PasswordEncoder passwordEncoder, Environment environment){
        this.userRepository = userRepository;
        this.userCreditRepository = userCreditRepository;
        this.userDeductionRepository = userDeductionRepository;
        this.creditService = creditService;
        this.deductionService = deductionService;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    // Add new User:
    @Override
    public UserDto addUser(UserDto newUserDto) {
        User newUser = newUserDto.getUser();
        // Spring Security demands a username, but we don't want to force our users to come up with one:
        newUser.setUsername(newUserDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        try {
            return new UserDto(userRepository.saveAndFlush(newUser));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(environment.getProperty(SystemMessages.USER_ALREADY_EXISTS.toString()));
        }
    }

    // Add new Admin:
    @Override
    public UserDto addAdmin(UserDto newAdmin) {
        User admin = newAdmin.getUser();
        admin.setUsername(admin.getEmail());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole("ADMIN");

        try {
            return new UserDto(userRepository.saveAndFlush(admin));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(environment.getProperty(SystemMessages.USER_ALREADY_EXISTS.toString()));
        }
    }

    // Find User by ID:
    @Override
    public UserDto findUserById(int id) {
        return userRepository.findById(id)
                .map(UserDto::new)
                .orElseThrow(() -> new UserNotFoundException(environment.getProperty(SystemMessages.USER_NOT_FOUND.toString())));
    }

    // Login User:
    @Override
    public UserDto login(UserDto authCredentials) {
        String email = authCredentials.getEmail();
        String password = authCredentials.getPassword();

        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException(environment.getProperty(SystemMessages.INVALID_CREDENTIALS.toString())));

        if (!passwordEncoder.matches(authCredentials.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(environment.getProperty(SystemMessages.INVALID_CREDENTIALS.toString()));
        }

        return new UserDto(user);
    }

    // Load User by Username (for Spring Security):
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(environment.getProperty(SystemMessages.USER_NOT_FOUND.toString())));
    }

    // Update User by ID:
    @Override
    public UserDto updateUserById(int id, UserDto updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(environment.getProperty(SystemMessages.USER_NOT_FOUND.toString())));

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setUsername(updatedUser.getEmail());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setSsn(updatedUser.getSsn());

        return new UserDto(userRepository.saveAndFlush(existingUser));
    }

    // Update Password by ID:
    @Override
    public void updatePasswordById(int id, String updatedPassword) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(environment.getProperty(SystemMessages.USER_NOT_FOUND.toString())));

        existingUser.setPassword(passwordEncoder.encode(updatedPassword));
        userRepository.saveAndFlush(existingUser);
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
        userCreditDto.setUserId(id);

        // Make a GET request to the credits service to get the credit with the given ID:
        CreditDto creditDto = creditService.findCreditById(userCreditDto.getCreditId());

        // Multiply the value of the credit by the number of credits claimed by the user to get the total value of the UserCredit:
        UserCredit userCredit = userCreditDto.getUserCredit();
        BigDecimal totalValue = creditDto.getValue().multiply(BigDecimal.valueOf(userCredit.getCreditsClaimed()));
        userCredit.setTotalValue(totalValue.setScale(2, RoundingMode.HALF_UP));

        userCredit.setUser(user);
        userCredit.setCredit(creditDto.getCredit());

        return new UserCreditDto(userCreditRepository.saveAndFlush(userCredit));
    }

    // Find all UserCredits by User ID and Year:
    @Override
    public List<UserCreditDto> findAllCreditsByUserIdAndYear(int id, int year) {
        findUserById(id);
        return userCreditRepository.findAllByUserIdAndYear(id, year).stream().map(UserCreditDto::new).toList();
    }

    // Remove Tax Credit from User:
    @Override
    public void removeTaxCredit(int id, int creditId) {
        User user = findUserById(id).getUser();
        UserCredit userCredit = userCreditRepository.findById(creditId)
                .orElseThrow(() -> new IllegalArgumentException(environment.getProperty(SystemMessages.CREDIT_NOT_FOUND.toString())));

        if (userCredit.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException(environment.getProperty(SystemMessages.CREDIT_NOT_FOUND.toString()));
        }

        userCreditRepository.deleteById(creditId);
    }

    // Add Deduction to User:
    @Override
    public UserDeductionDto addDeduction(int id, UserDeductionDto userDeductionDto) {
        // Verify User exists:
        User user = findUserById(id).getUser();
        userDeductionDto.setUserId(id);

        // Make a GET request to the deductions service to get the deduction with the given ID:
        DeductionDto deductionDto = deductionService.findDeductionById(userDeductionDto.getDeductionId());

        // Use the deduction's rate to calculate the total value of the UserDeduction based on the amount spent by the user:
        UserDeduction userDeduction = userDeductionDto.getUserDeduction();
        userDeduction.setDeductionAmount(userDeduction.getAmountSpent().multiply(deductionDto.getRate()).setScale(2, RoundingMode.HALF_UP));
        userDeduction.setUser(user);
        userDeduction.setDeduction(deductionDto.getDeduction());

        return new UserDeductionDto(userDeductionRepository.saveAndFlush(userDeduction));
    }

    // Find all UserDeductions by User ID:
    @Override
    public List<UserDeductionDto> findAllDeductionsByUserIdAndYear(int id, int year) {
        findUserById(id);
        return userDeductionRepository.findAllByUserIdAndYear(id, year).stream().map(UserDeductionDto::new).toList();
    }

    // Remove Deduction from User:
    @Override
    public void removeDeduction(int id, int deductionId) {
        User user = findUserById(id).getUser();
        UserDeduction userDeduction = userDeductionRepository.findById(deductionId)
                .orElseThrow(() -> new IllegalArgumentException(environment.getProperty(SystemMessages.DEDUCTION_NOT_FOUND.toString())));
        if (userDeduction.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException(environment.getProperty(SystemMessages.DEDUCTION_NOT_FOUND.toString()));
        }
        userDeductionRepository.deleteById(deductionId);
    }
}
