package com.epam.esm.service;

import com.epam.esm.service.dto.impl.CertificateDto;
import com.fasterxml.jackson.databind.ObjectMapper;

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
public final class CertificateConverter {

    private CertificateConverter() {
    }

    /**
     * Method converts dto instance to parameter map.
     */
    public static Map<String, Object> convertToParameterMap(CertificateDto dto) {
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(dto, Map.class);
        map = map.entrySet().stream().filter(k ->
                k.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return map;
    }
}
