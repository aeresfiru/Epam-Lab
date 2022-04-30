package com.epam.esm.dto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DtoMapper
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Component
public abstract class Mapper<T, D> {

    protected final ModelMapper modelMapper;

    @Autowired
    protected Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public abstract T toEntity(D dto);

    public abstract D toDto(T dto);
}
