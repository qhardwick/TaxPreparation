package com.skillstorm.exceptions;

public class TaxFormNotFoundException extends IllegalArgumentException {
    public TaxFormNotFoundException(String errors) {
        super(errors);
    }
}
