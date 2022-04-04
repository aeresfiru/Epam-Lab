package com.epam.esm.service.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class CertificateDTO {

    @NotEmpty(message = "Name may not be empty")
    @Size(min = 3, max = 100, message = "Name should be between 3 and 100 characters long")
    private String name;

    @NotEmpty(message = "Description may not be empty")
    @Size(min = 20, max = 500, message = "Description should be between 20 and 500 characters long")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 3, fraction = 2)
    private String price;

    @Min(value = 3, message = "Duration should be more than 3 days")
    @Max(value = 365, message = "Duration should be lesser than year")
    private String duration;
}
