package org.semester.util;

import lombok.Getter;

@Getter
public enum StaticString {
    BAD_TOKEN("Bad token"),
    EXPIRED_TOKEN("Expired token"),
    USED_TOKEN("Used token"),
    WRONG_CREDENTIALS("Wrong credentials"),
    EMAIL_IN_USE("Email is in use");

    private final String value;
    StaticString(String value) {
        this.value = value;
    }

}
