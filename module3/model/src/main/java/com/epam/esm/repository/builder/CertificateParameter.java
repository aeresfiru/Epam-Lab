package com.epam.esm.repository.builder;

/**
 * CertificateParameter
 *
 * @author alex
 * @version 1.0
 * @since 28.04.22
 */
public enum CertificateParameter {
    ID("id", "id"),
    NAME("name", "name"),
    CREATE_DATE("date", "createDate");

    private final String nameInRequest;
    private final String entityName;

    CertificateParameter(String nameInRequest, String entityName) {
        this.nameInRequest = nameInRequest;
        this.entityName = entityName;
    }

    public String getNameInRequest() {
        return nameInRequest;
    }

    public String getEntityName() {
        return entityName;
    }

    public static CertificateParameter fromString(String name) {
        for (CertificateParameter parameter : CertificateParameter.values()) {
            if (parameter.nameInRequest.equalsIgnoreCase(name)) {
                return parameter;
            }
        }
        return null;
    }
}
