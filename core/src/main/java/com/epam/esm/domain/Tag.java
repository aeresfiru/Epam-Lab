package com.epam.esm.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag {
    private Long id;

    @NotEmpty(message = "Name may not be empty")
    @Length(min = 3, max = 100, message = "Name should be between 3 and 100 characters long")
    private String name;
}
