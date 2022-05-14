package com.epam.esm.service.model;

import com.epam.esm.domain.Status;
import com.epam.esm.service.validator.OnPersist;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
public class CertificateModel extends RepresentationModel<CertificateModel> {

    private Long id;

    @NotBlank(groups = OnPersist.class)
    @Length(min = 3, max = 95, groups = OnPersist.class)
    @Length(max = 95)
    private String name;

    @NotBlank(groups = OnPersist.class)
    @Length(min = 3, max = 95, groups = OnPersist.class)
    @Length(max = 95)
    private String description;

    @NotNull(groups = OnPersist.class)
    @DecimalMin("0.01")
    private BigDecimal price;

    @NotNull(groups = OnPersist.class)
    @Min(1)
    @Max(366)
    private Short duration;

    @Valid
    private Set<TagModel> tags = new HashSet<>();

    private Status status = Status.ACTIVE;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;
}
