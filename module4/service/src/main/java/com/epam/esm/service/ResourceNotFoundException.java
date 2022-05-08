package com.epam.esm.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * EntityNotFoundException
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ResourceNotFoundException extends RuntimeException {

     private long id;
     private final String messageKey;

    public ResourceNotFoundException(String messageKey) {
        this.messageKey = messageKey;
    }

    public ResourceNotFoundException(long id, String messageKey) {
        this.id = id;
        this.messageKey = messageKey;
    }
}
