package com.epam.esm.service.exception;

public enum ErrorCode {
    TAG_ERROR("01"),
    CERTIFICATE_ERROR("02");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
