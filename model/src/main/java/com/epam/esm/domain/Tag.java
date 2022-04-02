package com.epam.esm.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tag extends AbstractEntity{

    @NotEmpty(message = "Name may not be empty")
    @Length(min = 3, max = 100, message = "Name should be between 3 and 100 characters long")
    private String name;
}
