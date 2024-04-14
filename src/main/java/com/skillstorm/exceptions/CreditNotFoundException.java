package com.skillstorm.exceptions;

public class CreditNotFoundException extends IllegalArgumentException {
    public CreditNotFoundException(String errors) {
        super(errors);
    }
}
