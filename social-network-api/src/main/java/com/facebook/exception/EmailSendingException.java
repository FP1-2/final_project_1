package com.facebook.exception;

public class EmailSendingException extends Exception {
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
