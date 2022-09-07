package com.epam.esm.service.model;

import com.epam.esm.domain.Status;
import com.epam.esm.service.validator.OnPersist;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * CertificateAdminResponse
 *
 * @author alex
 * @version 1.0
 * @since 9.05.22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CertificateModel extends RepresentationModel<CertificateModel> {

    private Long id;

    @NotBlank(groups = OnPersist.class, message = "Name shouldn't be empty")
    @Length(min = 3, max = 95, groups = OnPersist.class)
    @Length(max = 95, message = "Name shouldn't be more than 95 symbols")
    private String name;

    @NotBlank(groups = OnPersist.class, message = "Description shouldn't be empty")
    @Length(min = 3, max = 95, groups = OnPersist.class)
    @Length(max = 95, message = "Description shouldn't be longer than 95 symbols")
    private String description;

    @NotNull(groups = OnPersist.class, message = "Price shouldn't be empty")
    @DecimalMin(value = "0.01", message = "Duration should be more than 0.01")
    private BigDecimal price;

    @NotNull(groups = OnPersist.class, message = "Duration shouldn't be empty")
    @Min(value = 1, message = "Duration should be at least 1")
    @Max(value = 366, message = "Duration shouldn't be more than 366")
    private Short duration;

    @Valid
    private Set<TagModel> tags = new HashSet<>();

    private Status status = Status.ACTIVE;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;
}
