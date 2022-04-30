package com.epam.esm.dto;

import com.epam.esm.domain.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * CertificateDto
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CertificateDto extends RepresentationModel<CertificateDto> {

    private Long id;

    @NotEmpty
    private String name;

    private String description;

    private BigDecimal price;

    private Short duration;

    private Set<TagDto> tags = new HashSet<>();

    private Status status = Status.ACTIVE;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;
}
