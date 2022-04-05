package com.epam.esm.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Tag extends AbstractEntity {

    @NotEmpty(message = "Name may not be empty")
    @Size(min = 3, max = 100, message = "Name should be between 3 and 100 characters long")
    private String name;
}
