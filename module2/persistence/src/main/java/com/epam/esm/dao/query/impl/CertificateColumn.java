package com.epam.esm.dao.query.impl;

import java.util.Arrays;
import java.util.Optional;

public enum CertificateColumn {
    ID("id", "id"),
    NAME("name", "name"),
    DATE("date", "create_date"),
    LAST_UPDATE("lastUpdateDate", "last_update_date"),
    CREATE_DATE("createDate", "create_date");

    private final String nameInRequest;
    private final String nameInDb;

    CertificateColumn(String nameInRequest, String nameInDb) {
        this.nameInRequest = nameInRequest;
        this.nameInDb = nameInDb;
    }

    public static Optional<String> resolveDbName(String nameInRequest) {
        Optional<String> nameInDb = Optional.empty();
        Optional<CertificateColumn> column = Arrays.stream(values())
                .filter(v -> v.getNameInRequest().equals(nameInRequest))
                .findAny();
        if (column.isPresent()) {
            nameInDb = Optional.ofNullable(column.get().getNameInDb());
        }
        return nameInDb;
    }

    public String getNameInRequest() {
        return nameInRequest;
    }

    public String getNameInDb() {
        return nameInDb;
    }
}
