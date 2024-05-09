package com.skillstorm.services;

import com.skillstorm.dtos.UserCreditDto;
import com.skillstorm.dtos.UserDeductionDto;
import com.skillstorm.dtos.UserDto;

import java.util.List;

public interface UserService {

    // Add new User:
    UserDto addUser(UserDto newUser);

    // Add new Admin:
    UserDto addAdmin(UserDto newAdmin);

    // Find User by ID:
    UserDto findUserById(int id);

    // Find User by Username:
    UserDto login(UserDto authCredentials);

    // Update User by ID:
    UserDto updateUserById(int id, UserDto updatedUser);

    // Update Password by ID:
    void updatePasswordById(int id, String updatedPassword);

    // Delete User by ID:
    void deleteUserById(int id);

    // Add Tax Credit to User:
    UserCreditDto addTaxCredit(int id, UserCreditDto creditToBeAdded);

    // Find all UserCredits by User ID and Year:
    List<UserCreditDto> findAllCreditsByUserIdAndYear(int id, int year);

    // Remove Tax Credit from User:
    void removeTaxCredit(int id, int creditId);

    // Add Deduction to User:
    UserDeductionDto addDeduction(int id, UserDeductionDto deductionToBeAdded);

    // Find all UserDeductions by User ID:
    List<UserDeductionDto> findAllDeductionsByUserIdAndYear(int id, int year);

    // Remove Deduction from User:
    void removeDeduction(int id, int deductionId);
}
