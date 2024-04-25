package com.skillstorm.controllers;

import com.skillstorm.dtos.UserCreditDto;
import com.skillstorm.dtos.UserDeductionDto;
import com.skillstorm.dtos.UserDto;
import com.skillstorm.services.UserService;
import com.skillstorm.validations.AddUserGroup;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Test Endpoint:
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    // Add new User:
    @PostMapping
    public ResponseEntity<UserDto> addUser(@Validated(AddUserGroup.class) @RequestBody UserDto newUser) {
        UserDto createdUser = userService.addUser(newUser);
        return ResponseEntity.created(URI.create("/" + createdUser.getId())).body(createdUser);
    }

    // Find User by ID:
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    // Find User by Username:
    @GetMapping()
    public ResponseEntity<UserDto> findUserByUsername(@PathParam("username") String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }

    // Update User by ID:
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable int id, @Valid @RequestBody UserDto updatedUser) {
        return ResponseEntity.ok(userService.updateUserById(id, updatedUser));
    }

    // Delete User by ID:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // Add Tax Credit to User:
    @PostMapping("/{id}/credits")
    public ResponseEntity<UserCreditDto> addTaxCredit(@PathVariable int id, @RequestBody UserCreditDto creditToBeAdded) {
        return ResponseEntity.ok(userService.addTaxCredit(id, creditToBeAdded));
    }

    // Find all UserCredits by User ID:
    @GetMapping("/{id}/credits")
    public ResponseEntity<List<UserCreditDto>> findAllCreditsByUserId(@PathVariable int id) {
        return ResponseEntity.ok(userService.findAllCreditsByUserId(id));
    }

    // Add Deduction to User:
    @PostMapping("/{id}/deductions")
    public ResponseEntity<UserDeductionDto> addDeduction(@PathVariable int id, @RequestBody UserDeductionDto deductionToBeAdded) {
        return ResponseEntity.ok(userService.addDeduction(id, deductionToBeAdded));
    }

    // Find all UserDeductions by User ID:
    @GetMapping("/{id}/deductions")
    public ResponseEntity<List<UserDeductionDto>> findAllDeductionsByUserId(@PathVariable int id) {
        return ResponseEntity.ok(userService.findAllDeductionsByUserId(id));
    }

}
