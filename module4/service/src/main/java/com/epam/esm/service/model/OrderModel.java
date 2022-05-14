package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * OrderDto
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderModel extends RepresentationModel<OrderModel> {

    @Positive
    private Long id;

    private BigDecimal cost;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;

    private UserModel user;

    private List<CertificateModel> certificates;
}
