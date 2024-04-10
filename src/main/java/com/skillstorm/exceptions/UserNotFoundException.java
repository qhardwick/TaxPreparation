package com.skillstorm.exceptions;

public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException(String errors) {
        super(errors);
    }
}
