package com.epam.esm.util;

import com.epam.esm.repository.builder.SortType;
import lombok.Data;

import java.util.Map;

/**
 * SortParameters
 *
 * @author alex
 * @version 1.0
 * @since 27.04.22
 */
@Data
public class SortParameters {
    private final Map<String, SortType> sortParameters;
}
