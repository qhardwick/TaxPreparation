package com.skillstorm.configs;

public enum SystemMessages {

    // Exception Messages:
    USER_NOT_FOUND("user.not.found"),
    USER_ALREADY_EXISTS("user.already.exists"),
    W2_NOT_FOUND("w2.not.found"),
    TAX_FORM_NOT_FOUND("taxForm.not.found"),
    DEDUCTION_NOT_FOUND("deduction.not.found"),
    CREDIT_NOT_FOUND("credit.not.found"),

    // Validation Messages:
    // User:
    USER_FIRST_NAME_SIZE("user.firstName.size"),
    USER_FIRST_NAME_PATTERN("user.firstName.pattern"),
    USER_LAST_NAME_SIZE("user.lastName.size"),
    USER_LAST_NAME_PATTERN("user.lastName.pattern"),
    USER_EMAIL_MUST("user.email.must"),
    USER_EMAIL_VALID("user.email.valid"),
    USER_ADDRESS_MUST("user.address.must"),
    USER_PHONE_NUMBER_VALID("user.phoneNumber.valid"),
    USER_SSN_VALID("user.ssn.valid");

    // W2:

    private final String message;

    SystemMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
