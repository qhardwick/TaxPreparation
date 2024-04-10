package com.skillstorm.services;

import com.skillstorm.dtos.UserDto;

public interface UserService {

    // Add new User:
    UserDto addUser(UserDto newUser);

    // Find User by ID:
    UserDto findUserById(String id);

    // Update User by ID:
    UserDto updateUserById(String id, UserDto updatedUser);

    // Delete User by ID:
    void deleteUserById(String id);
}
