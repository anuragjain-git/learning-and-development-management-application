package com.vilt.narmada.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("Invalid Email.");
    }
}
