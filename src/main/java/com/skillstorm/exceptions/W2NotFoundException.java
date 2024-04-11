package com.skillstorm.exceptions;

public class W2NotFoundException extends IllegalArgumentException {

    public W2NotFoundException(String errors) {
        super(errors);
    }
}
