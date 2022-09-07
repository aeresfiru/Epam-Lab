package com.epam.esm.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncorrectParameterException extends RuntimeException {

    private final String fieldName;

    private final Object value;

    private final String errorCode;
}
