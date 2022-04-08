package com.epam.esm.service.exception;

public class ErrorConstraint {

    public static final String INVALID_CERTIFICATE_NAME = "error.certificate.name";
    public static final String INVALID_CERTIFICATE_DESCRIPTION = "error.certificate.description";
    public static final String INVALID_CERTIFICATE_PRICE = "error.certificate.price";
    public static final String INVALID_CERTIFICATE_DURATION = "error.certificate.duration";
    public static final String INVALID_TAG_NAME = "error.tag.name";
    public static final String RESOURCE_NOT_FOUND = "error.not.found";
    public static final String CONFLICT = "error.cant.create";

    private ErrorConstraint() {
    }
}
