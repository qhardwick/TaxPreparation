package com.skillstorm.controllers;

import com.skillstorm.dtos.UserCreditDto;
import com.skillstorm.dtos.UserDeductionDto;
import com.skillstorm.dtos.UserDto;
import com.skillstorm.services.UserService;
import com.skillstorm.validations.AddUserGroup;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins="*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Add new User:
    @PostMapping
    public ResponseEntity<UserDto> addUser(@Validated(AddUserGroup.class) @RequestBody UserDto newUser) {
        UserDto createdUser = userService.addUser(newUser);
        return ResponseEntity.created(URI.create("/" + createdUser.getId())).body(createdUser);
    }

    // Add new Admin:
    @PostMapping("/admins")
    public ResponseEntity<UserDto> addAdmin(@Validated(AddUserGroup.class) @RequestBody UserDto newAdmin) {
        UserDto createdAdmin = userService.addAdmin(newAdmin);
        return ResponseEntity.created(URI.create("/" + createdAdmin.getId())).body(createdAdmin);
    }

    // Find User by ID:
    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserDto> findUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    // Find User by Username:
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto authCredentials) {
        return ResponseEntity.ok(userService.login(authCredentials));
    }


    // Update User by ID:
    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserDto> updateUserById(@PathVariable int id, @Valid @RequestBody UserDto updatedUser) {
        return ResponseEntity.ok(userService.updateUserById(id, updatedUser));
    }

    // Update Password by ID:
    @PutMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<Void> updatePasswordById(@PathVariable int id, @RequestBody String updatedPassword) {
        userService.updatePasswordById(id, updatedPassword);
        return ResponseEntity.ok().build();
    }

    // Delete User by ID:
    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<Void> deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // Add Tax Credit to User:
    @PostMapping("/{id}/credits")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserCreditDto> addTaxCredit(@PathVariable int id, @RequestBody UserCreditDto creditToBeAdded) {
        return ResponseEntity.ok(userService.addTaxCredit(id, creditToBeAdded));
    }

    // Find all UserCredits by User ID And Year:
    @GetMapping("/{id}/credits/{year}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<List<UserCreditDto>> findAllCreditsByUserId(@PathVariable int id, @PathVariable int year) {
        return ResponseEntity.ok(userService.findAllCreditsByUserIdAndYear(id, year));
    }

    // Remove Tax Credit from User:
    @DeleteMapping("/{id}/credits/{creditId}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<Void> removeTaxCredit(@PathVariable int id, @PathVariable int creditId) {
        userService.removeTaxCredit(id, creditId);
        return ResponseEntity.noContent().build();
    }

    // Add Deduction to User:
    @PostMapping("/{id}/deductions")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserDeductionDto> addDeduction(@PathVariable int id, @RequestBody UserDeductionDto deductionToBeAdded) {
        return ResponseEntity.ok(userService.addDeduction(id, deductionToBeAdded));
    }

    // Find all UserDeductions by User ID And Year:
    @GetMapping("/{id}/deductions/{year}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<List<UserDeductionDto>> findAllDeductionsByUserIdAndYear(@PathVariable int id, @PathVariable int year) {
        return ResponseEntity.ok(userService.findAllDeductionsByUserIdAndYear(id, year));
    }

    // Remove Deduction from User:
    @DeleteMapping("/{id}/deductions/{deductionId}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<Void> removeDeduction(@PathVariable int id, @PathVariable int deductionId) {
        userService.removeDeduction(id, deductionId);
        return ResponseEntity.noContent().build();
    }

}
