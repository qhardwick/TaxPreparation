package com.skillstorm.utils;

public enum SystemMessages {

    USER_NOT_FOUND("user.not.found");

    private final String message;

    SystemMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
