package com.skillstorm.exceptions;

public class DeductionNotFoundException extends IllegalArgumentException {
    public DeductionNotFoundException(String errors) {
        super(errors);
    }
}
