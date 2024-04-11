package com.skillstorm.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Ref;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Environment environment;

    @Autowired
    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    // Handle Bad Requests from trying to look up entities not present in DB:
    @ExceptionHandler({UserNotFoundException.class, W2NotFoundException.class})
    public ResponseEntity<ErrorMessage> handleEntityNotFoundExceptions(IllegalArgumentException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setCode(HttpStatus.NOT_FOUND.value());
        errorMessage.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
