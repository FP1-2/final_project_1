package com.facebook.exception;

public class BannedMemberException extends RuntimeException {
    public BannedMemberException(String message) {
        super(message);
    }
}
