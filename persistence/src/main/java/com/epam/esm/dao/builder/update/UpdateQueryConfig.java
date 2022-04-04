package com.epam.esm.dao.builder.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
public class UpdateQueryConfig {
    private final Map<String, String> paramsValueMap;
}
