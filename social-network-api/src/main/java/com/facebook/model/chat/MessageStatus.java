package com.facebook.model.chat;

public enum MessageStatus {
    SENT,
    READ,
    FAILED;

    public static MessageStatus fromString(String status) {
        return switch (status.toLowerCase()) {
            case "sent" -> MessageStatus.SENT;
            case "read" -> MessageStatus.READ;
            case "failed" -> MessageStatus.FAILED;
            default -> throw new IllegalArgumentException("Invalid MessageStatus: " + status);
        };
    }
}
