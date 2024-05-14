package com.skillstorm.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    // Test the no-arg constructor:
    @Test
    void testUser() {
        User user = new User();
        assertEquals(0, user.getId());
        assertEquals(null, user.getFirstName());
        assertEquals(null, user.getLastName());
        assertEquals(null, user.getEmail());
        assertEquals(null, user.getUsername());
        assertEquals(null, user.getPassword());
        assertEquals(null, user.getAddress());
        assertEquals(null, user.getPhoneNumber());
        assertEquals(null, user.getSsn());
        assertEquals(null, user.getRole());
    }

    // Test the getAuthorities method:
    @Test
    void testGetAuthorities() {
        User user = new User();
        user.setRole("ROLE_USER");
        assertEquals(1, user.getAuthorities().size());
    }
}
