package com.epam.esm.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DuplicateEntityException
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DuplicateEntityException extends RuntimeException {

    private final String field;
    private final String messageKey;
}
