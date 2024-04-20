package com.skillstorm.controllers;

import com.skillstorm.dtos.UserDto;
import com.skillstorm.services.UserService;
import com.skillstorm.validations.AddUserGroup;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    @Secured("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> addUser(@Validated(AddUserGroup.class) @RequestBody UserDto newUser) {
        UserDto createdUser = userService.addUser(newUser);
        return ResponseEntity.created(URI.create("/" + createdUser.getId())).body(createdUser);
    }

    // Find User by ID:
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<UserDto> findUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.findUserById(id));
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

}
