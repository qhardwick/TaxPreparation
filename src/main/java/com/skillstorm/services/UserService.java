package com.skillstorm.services;

import com.skillstorm.dtos.UserCreditDto;
import com.skillstorm.dtos.UserDeductionDto;
import com.skillstorm.dtos.UserDto;

import java.util.List;

public interface UserService {

    // Add new User:
    UserDto addUser(UserDto newUser);

    // Find User by ID:
    UserDto findUserById(int id);

    // Update User by ID:
    UserDto updateUserById(int id, UserDto updatedUser);

    // Update Password by ID:
    void updatePasswordById(int id, UserDto updatedPassword);

    // Delete User by ID:
    void deleteUserById(int id);

    // Add Tax Credit to User:
    UserCreditDto addTaxCredit(int id, UserCreditDto creditToBeAdded);

    // Find all UserCredits by User ID:
    List<UserCreditDto> findAllCreditsByUserId(int id);

    // Add Deduction to User:
    UserDeductionDto addDeduction(int id, UserDeductionDto deductionToBeAdded);

    // Find all UserDeductions by User ID:
    List<UserDeductionDto> findAllDeductionsByUserId(int id);
}
