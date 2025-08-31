package com.vilt.narmada.exception;

import java.time.LocalDate;

public class ExceptionResponse {
    private LocalDate timestamp;
    private String message;
    private String details;
    private String httpCodeMessage;

    public ExceptionResponse(LocalDate timestamp, String message, String details, String httpCodeMessage) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.httpCodeMessage = httpCodeMessage;
    }

    public LocalDate getTimestamp() { return timestamp; }
    public String getMessage() { return message; }
    public String getDetails() { return details; }
    public String getHttpCodeMessage() { return httpCodeMessage; }
}
