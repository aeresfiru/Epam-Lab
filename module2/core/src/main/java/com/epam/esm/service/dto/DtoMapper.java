package com.epam.esm.service.dto;

public interface DtoMapper<T, R> {

    R mapToDto(T entity);

    T mapFromDto(R dto);
}
