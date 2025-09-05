package com.vilt.narmada.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle User Not Found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.NOT_FOUND.getReasonPhrase()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle Email Already Exists
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEmailExists(EmailAlreadyExistsException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.CONFLICT.getReasonPhrase()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Handle Invalid Credentials
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.UNAUTHORIZED.getReasonPhrase()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidEmail(InvalidEmailException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.NOT_FOUND.getReasonPhrase()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle validation errors (@Valid / @NotBlank etc.)
    // Not need to create a new class for MethodArgumentNotValidException
    // It’s automatically thrown whenever a @Valid request body fails validation (for example, missing required field, invalid email, etc.).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        String errors = ex.getBindingResult().getFieldErrors()
                .stream().map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(),
                "Validation failed: " + errors,
                request.getDescription(false),
                HttpStatus.BAD_REQUEST.getReasonPhrase()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGlobalException(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
