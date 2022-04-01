package com.epam.esm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Short duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Set<Tag> tags;
}
