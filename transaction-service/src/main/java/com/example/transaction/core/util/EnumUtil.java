package com.example.transaction.core.util;

import java.util.function.Supplier;

public class EnumUtil {

    public static <T extends Enum<T>> T from(
        Class<T> enumClass,
        String value,
        Supplier<? extends RuntimeException> exceptionSupplier
    ) {
        if (value == null) {
            return null;
        }

        for (T constant : enumClass.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(value)) {
                return constant;
            }
        }
        throw exceptionSupplier.get();
    }
}
