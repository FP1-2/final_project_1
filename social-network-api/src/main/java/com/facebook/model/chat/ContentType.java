package com.facebook.model.chat;

public enum ContentType {
    TEXT,
    IMAGE,
    FILE,
    LIKE,
    EMOJI;
    public static ContentType fromString(String type) {
        return switch (type.toLowerCase()) {
            case "text" -> ContentType.TEXT;
            case "image" -> ContentType.IMAGE;
            case "file" -> ContentType.FILE;
            case "like" -> ContentType.LIKE;
            case "emoji" -> ContentType.EMOJI;
            default -> throw new IllegalArgumentException("Invalid ContentType: " + type);
        };
    }
}
