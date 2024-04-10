package com.skillstorm.controllers;

import com.skillstorm.dtos.UserDto;
import com.skillstorm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto newUser) {
        UserDto createdUser = userService.addUser(newUser);
        return ResponseEntity.created(URI.create("/" + createdUser.getId())).body(createdUser);
    }

    // Find User by ID:
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    // Update User by ID:
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable String id, @RequestBody UserDto updatedUser) {
        return ResponseEntity.ok(userService.updateUserById(id, updatedUser));
    }

    // Delete User by ID:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
