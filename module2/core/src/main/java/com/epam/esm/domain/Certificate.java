package com.epam.esm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Certificate implements Serializable {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Short duration;

    private LocalDateTime created;

    private LocalDateTime updated;

    private Set<Tag> tags = new HashSet<>();

    public void addTag(Tag tag) {
        if (!this.tags.contains(tag)) {
            tags.add(tag);
        }
    }
}
