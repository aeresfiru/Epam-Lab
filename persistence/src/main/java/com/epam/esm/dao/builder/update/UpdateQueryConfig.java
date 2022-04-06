package com.epam.esm.dao.builder.update;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
public class UpdateQueryConfig {
    private final Map<String, Object> paramsValueMap;
}
