package com.epam.esm.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * IncorrectParameterException
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class IncorrectParameterException extends RuntimeException {

    private final ExceptionResult exceptionResult;
}
