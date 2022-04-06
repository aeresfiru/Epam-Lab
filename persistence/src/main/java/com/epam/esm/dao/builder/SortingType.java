package com.epam.esm.dao.builder;

import java.util.Arrays;
import java.util.Optional;

public enum SortingType {
    ASC, DESC;

    public static Optional<SortingType> resolveType(String name) {
        return Arrays.stream(values()).filter(v -> v.name().equals(name)).findAny();
    }
}
