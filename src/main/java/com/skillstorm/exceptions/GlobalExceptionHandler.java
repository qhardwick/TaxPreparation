package com.skillstorm.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@PropertySource("classpath:SystemMessages.properties")
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // Handle 404 Not Found from trying to look up entities not present in DB:
    @ExceptionHandler({UserNotFoundException.class, W2NotFoundException.class, TaxFormNotFoundException.class, DeductionNotFoundException.class, CreditNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleEntityNotFoundExceptions(IllegalArgumentException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setCode(HttpStatus.NOT_FOUND.value());
        errorMessage.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    // Handle Bad Requests from trying to add or update entities with invalid data in the RequestBody:
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setCode(HttpStatus.BAD_REQUEST.value());
        errorMessage.setMessage(e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).collect(Collectors.joining(", ")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    // Handle Constraint Violation Exceptions that occur from methods updating entities with invalid data:
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationExceptions(ConstraintViolationException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setCode(HttpStatus.BAD_REQUEST.value());
        errorMessage.setMessage(e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
