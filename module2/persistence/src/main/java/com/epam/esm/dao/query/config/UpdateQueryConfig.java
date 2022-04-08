package com.epam.esm.dao.query.config;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
public class UpdateQueryConfig {
    private final Map<String, Object> paramsValueMap;
}
