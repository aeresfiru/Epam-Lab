package com.epam.esm.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicateEntityException extends RuntimeException {

    private final String field;

    private final String errorCode;
}
