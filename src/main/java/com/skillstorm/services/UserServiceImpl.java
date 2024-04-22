package com.skillstorm.services;

import com.skillstorm.dtos.UserDto;
import com.skillstorm.entities.User;
import com.skillstorm.exceptions.UserNotFoundException;
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
    private final Environment environment;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Environment environment) {
        this.userRepository = userRepository;
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
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) {
            throw new UserNotFoundException(environment.getProperty(SystemMessages.USER_NOT_FOUND.toString()));
        }
        return new UserDto(userOptional.get());
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
}
