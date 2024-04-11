package com.skillstorm.utils;

public enum SystemMessages {

    USER_NOT_FOUND("user.not.found"),
    W2_NOT_FOUND("w2.not.found");

    private final String message;

    SystemMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
