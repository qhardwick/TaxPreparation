package com.skillstorm.services;

import com.skillstorm.dtos.UserDto;
import com.skillstorm.entities.User;
import com.skillstorm.exceptions.UserNotFoundException;
import com.skillstorm.repositories.UserRepository;
import com.skillstorm.utils.SystemMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
        return new UserDto(userRepository.save(newUser.getUser()));
    }

    // Find User by ID:
    @Override
    public UserDto findUserById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) {
            throw new UserNotFoundException(environment.getProperty(SystemMessages.USER_NOT_FOUND.toString()));
        }
        return new UserDto(userOptional.get());
    }

    // Update User by ID:
    @Override
    public UserDto updateUserById(String id, UserDto updatedUser) {
        findUserById(id);
        return new UserDto(userRepository.save(updatedUser.getUser()));
    }

    // Delete User by ID:
    @Override
    public void deleteUserById(String id) {
        findUserById(id);
        userRepository.deleteById(id);
    }
}
