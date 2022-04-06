package com.epam.esm.dao.builder.select;

import java.util.Arrays;
import java.util.Optional;

public enum CertificateSortColumn {
    NAME("name", "name"),
    DATE("date", "create_date");

    private final String nameInRequest;
    private final String nameInDb;

    CertificateSortColumn(String nameInRequest, String nameInDb) {
        this.nameInRequest = nameInRequest;
        this.nameInDb = nameInDb;
    }

    public String getNameInRequest() {
        return nameInRequest;
    }

    public String getNameInDb() {
        return nameInDb;
    }

    public static Optional<String> resolveDbName(String nameInRequest) {
        Optional<String> nameInDb = Optional.empty();
        Optional<CertificateSortColumn> column = Arrays.stream(values())
                .filter(v -> v.getNameInRequest().equals(nameInRequest))
                .findAny();
        if (column.isPresent()) {
            nameInDb = Optional.ofNullable(column.get().getNameInDb());
        }
        return nameInDb;
    }
}
