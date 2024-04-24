package com.skillstorm.services;

import com.skillstorm.dtos.UserCreditDto;
import com.skillstorm.dtos.UserDto;

public interface UserService {

    // Add new User:
    UserDto addUser(UserDto newUser);

    // Find User by ID:
    UserDto findUserById(int id);

    // Find User by Username:
    UserDto findUserByUsername(String username);

    // Update User by ID:
    UserDto updateUserById(int id, UserDto updatedUser);

    // Delete User by ID:
    void deleteUserById(int id);

    // Add Tax Credit to User:
    UserCreditDto addTaxCredit(int id, UserCreditDto creditToBeAdded);
}
