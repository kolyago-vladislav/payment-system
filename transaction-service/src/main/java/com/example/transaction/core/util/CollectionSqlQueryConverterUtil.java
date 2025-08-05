package com.example.transaction.core.util;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class CollectionSqlQueryConverterUtil {
    public static <T, R> List<R> toList(
        Collection<T> collection,
        Function<T, R> mapper
    ) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return collection.stream()
            .map(mapper)
            .toList();
    }
}
