package com.skillstorm.services;

import com.skillstorm.dtos.UserCreditDto;
import com.skillstorm.dtos.UserDto;
import com.skillstorm.entities.User;
import com.skillstorm.exceptions.UserNotFoundException;
import com.skillstorm.repositories.UserCreditRepository;
import com.skillstorm.repositories.UserRepository;
import com.skillstorm.configs.SystemMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@PropertySource("classpath:SystemMessages.properties")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCreditRepository userCreditRepository;
    private final Environment environment;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserCreditRepository userCreditRepository, Environment environment) {
        this.userRepository = userRepository;
        this.userCreditRepository = userCreditRepository;
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
    public UserCreditDto addTaxCredit(int id, UserCreditDto creditToBeAdded) {
        User user = findUserById(id).getUser();
        return null;
    }
}
