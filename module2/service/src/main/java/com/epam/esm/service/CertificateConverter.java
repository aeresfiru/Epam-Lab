package com.epam.esm.service;

import com.epam.esm.service.dto.impl.CertificateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * EntityConverter.
 * <p>This class has only one method that converts object into a map.</p>
 *
 * @author alex
 * @version 1.0
 * @since 7.04.22
 */
@Component
public final class CertificateConverter {

    private final ObjectMapper objectMapper;

    @Autowired
    private CertificateConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Method converts dto instance to parameter map.
     */
    public Map<String, Object> convertToParameterMap(CertificateDto dto) {
        Map<String, Object> map = objectMapper.convertValue(dto, Map.class);
        map = map.entrySet().stream().filter(k ->
                k.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return map;
    }
}
