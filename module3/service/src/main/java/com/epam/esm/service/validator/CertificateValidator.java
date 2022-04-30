package com.epam.esm.service.validator;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.ExceptionResult;
import com.epam.esm.service.IncorrectParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * CertificateValidator
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Slf4j
@Component
@Qualifier("certificateValidator")
public class CertificateValidator extends Validator<CertificateDto> {

    @Value("#{new Integer('${certificate.name.max_length}')}")
    private int maxNameLength;

    @Value("#{new Integer('${certificate.name.min_length}')}")
    private int minNameLength;

    @Value("#{new Integer('${certificate.description.max_length}')}")
    private int maxDescriptionLength;

    @Value("#{new Integer('${certificate.description.min_length}')}")
    private int minDescriptionLength;

    @Value("#{new Integer('${certificate.price.max_scale}')}")
    private int maxPriceScale;

    @Value("${certificate.price.max}")
    private BigDecimal maxPrice;

    @Value("${certificate.price.min}")
    private BigDecimal minPrice;

    @Value("#{new Short('${certificate.duration.min}')}")
    private short minDuration;

    @Value("#{new Short('${certificate.duration.max}')}")
    private short maxDuration;


    @Override
    public void validateForCreate(CertificateDto dto) throws IncorrectParameterException {
        log.info("Validating certificate for create: " + dto + ".");
        ExceptionResult result = new ExceptionResult(new HashMap<>());

        if (!this.isValidString(dto.getName(), minNameLength, maxNameLength)) {
            result.addException(ExceptionConstant.CERTIFICATE_NAME, dto.getName());
        }
        if (!this.isValidString(dto.getDescription(), minDescriptionLength, maxDescriptionLength)) {
            result.addException(ExceptionConstant.CERTIFICATE_DESCRIPTION, dto.getDescription());
        }
        if (!this.isValidPrice(dto.getPrice())) {
            result.addException(ExceptionConstant.CERTIFICATE_PRICE, dto.getPrice());
        }
        if (!this.isValidDuration(dto.getDuration())){
            result.addException(ExceptionConstant.CERTIFICATE_DURATION, dto.getDuration());
        }

        this.throwExceptionIfEntityIsNotValid(result);
    }

    @Override
    public void validateForUpdate(CertificateDto dto) throws IncorrectParameterException {
        log.info("Validating certificate for update: " + dto + ".");
        ExceptionResult result = new ExceptionResult(new HashMap<>());

        if (dto.getName() != null
                && !this.isValidString(dto.getName(), minNameLength, maxNameLength)) {
            result.addException(ExceptionConstant.CERTIFICATE_NAME, dto.getName());
        }
        if (dto.getDescription() != null
                && !this.isValidString(dto.getDescription(), minDescriptionLength, maxDescriptionLength)) {
            result.addException(ExceptionConstant.CERTIFICATE_DESCRIPTION, dto.getDescription());
        }
        if (dto.getPrice() != null
                && !this.isValidPrice(dto.getPrice())) {
            result.addException(ExceptionConstant.CERTIFICATE_PRICE, dto.getPrice());
        }
        if (dto.getDuration() != null
                && !this.isValidDuration(dto.getDuration())){
            result.addException(ExceptionConstant.CERTIFICATE_DURATION, dto.getDuration());
        }

        this.throwExceptionIfEntityIsNotValid(result);
    }

    private boolean isValidPrice(BigDecimal price) {
        return price != null
                && price.scale() <= maxPriceScale
                && price.compareTo(minPrice) >= 0
                && price.compareTo(maxPrice) <= 0;
    }

    private boolean isValidDuration(short duration) {
        return duration >= minDuration && duration <= maxDuration;
    }
}
