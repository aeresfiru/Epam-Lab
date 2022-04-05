package com.epam.esm.service.dto;

import org.springframework.stereotype.Component;

@Component
public interface DtoMapper<T, R> {

    R mapToDto(T entity);

    T mapFromDto(R dto);
}
