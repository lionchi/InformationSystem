package ru.gavrilov.common;

import com.google.common.base.Strings;

import java.util.Collection;

public class Guard {

    @SuppressWarnings("ConstantConditions")
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String text, String message) {
        if (Strings.isNullOrEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}
