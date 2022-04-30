package com.epam.esm.util;

import com.epam.esm.repository.builder.CertificateParameter;
import com.epam.esm.repository.builder.SortType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * SortTypeMapConverter
 *
 * @author alex
 * @version 1.0
 * @since 27.04.22
 */
@Slf4j
public class SortTypeMapConverter {

    private SortTypeMapConverter() {
    }

    public static Map<String, SortType> convert(String sort) {
        log.info("Converting sort parameters to a map: " + sort);
        Map<String, SortType> sortTypeMap = new HashMap<>();

        if (sort != null) {
            String[] elements = sort.split(",");
            for (String element : elements) {
                SortType direction = SortType.ASC;
                if (element.startsWith("-")) {
                    direction = SortType.DESC;
                    element = element.substring(1);
                }
                CertificateParameter parameter = CertificateParameter.fromString(element);
                if (parameter != null) {
                    sortTypeMap.put(parameter.getEntityName(), direction);
                }
            }
        }
        return sortTypeMap;
    }
}
