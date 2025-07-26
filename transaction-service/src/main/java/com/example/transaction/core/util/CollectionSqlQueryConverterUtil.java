package com.example.transaction.core.util;

import java.util.Collection;
import java.util.function.Function;

import static org.springframework.util.CollectionUtils.isEmpty;

public class CollectionSqlQueryConverterUtil {
    public static <T, R> R[] toArray(
        Collection<T> collection,
        Function<T, R> mapper,
        java.util.function.IntFunction<R[]> generator
    ) {
        if (isEmpty(collection)) {
            return generator.apply(0);
        }

        return collection.stream()
            .map(mapper)
            .toArray(generator);
    }
}
