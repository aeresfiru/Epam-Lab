package com.epam.esm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ExceptionJSONInfo {

    private final String url;
    private final String message;
    private final String code;
}
