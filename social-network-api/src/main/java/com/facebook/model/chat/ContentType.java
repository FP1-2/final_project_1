package com.facebook.model.chat;

public enum ContentType {
    TEXT,
    IMAGE,
    LIKE;
    public static ContentType fromString(String type) {
        return switch (type.toLowerCase()) {
            case "text" -> ContentType.TEXT;
            case "image" -> ContentType.IMAGE;
            case "like" -> ContentType.LIKE;
            default -> throw new IllegalArgumentException("Invalid ContentType: " + type);
        };
    }
}
