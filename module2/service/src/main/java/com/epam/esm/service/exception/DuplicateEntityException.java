package com.epam.esm.service.exception;

public class DuplicateEntityException extends RuntimeException {

    private final String name;
    private final ErrorCode code;

    public DuplicateEntityException(String name, ErrorCode code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code.getCode();
    }
}
