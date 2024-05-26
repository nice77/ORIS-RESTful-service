package org.semester.util;

import lombok.Getter;

@Getter
public enum StaticString {
    BAD_TOKEN("Bad token"),
    EXPIRED_TOKEN("Expired token"),
    USED_TOKEN("Used token"),
    WRONG_CREDENTIALS("Wrong credentials"),
    EMAIL_IN_USE("Email is in use"),
    BANNED("Banned"),
    EVENT_NOT_FOUND("Event not found"),
    USER_NOT_FOUND("User not found"),
    ERROR_ON_FILE_ADD("File upload error"),
    ERROR_ON_FILE_READ("No such file or directory"),
    ERROR_ON_FILE_DELETE("No such file or directory"),
    WRONG_FILE_TYPE("Wrong file type"),
    TOKEN_NOT_FOUND("Token not found");

    private final String value;
    StaticString(String value) {
        this.value = value;
    }

    public static StaticString getByValue(String message) {
        for (StaticString staticString : StaticString.values()) {
            if (staticString.getValue().equals(message)) {
                return staticString;
            }
        }
        throw new IllegalArgumentException("No enum constant with message: " + message);
    }
}
