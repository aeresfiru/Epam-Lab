package com.epam.esm.service.validator;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.exception.IncorrectParameterException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import static com.epam.esm.service.exception.ErrorCode.INCORRECT_CERTIFICATE_PARAMETER;

@Component
public class CertificateValidator {

    private static final String NAME_REGEX = "[\\w\\W]{7,20}";

    private static final String DESCRIPTION_REGEX = "[\\w\\W]{20,200}$";

    private static final Integer MAX_SCALE = 2;

    private static final BigDecimal MIN_PRICE = new BigDecimal("0.01");

    private static final BigDecimal MAX_PRICE = new BigDecimal("999999.99");

    private static final int MAX_DURATION = 365;

    private static final int MIN_DURATION = 3;

    private CertificateValidator() {
    }

    public static void validate(CertificateDto certificate) throws IncorrectParameterException {
        validateName(certificate.getName());
        validateDescription(certificate.getDescription());
        validatePrice(certificate.getPrice());
        validateDuration(certificate.getDuration());
    }

    public static void validateForUpdate(CertificateDto certificate) {
        if (certificate.getName() != null) {
            validateName(certificate.getName());
        }
        if (certificate.getDescription() != null) {
            validateDescription(certificate.getDescription());
        }
        if (certificate.getPrice() != null) {
            validatePrice(certificate.getPrice());
        }
        if (certificate.getDuration() != null) {
            validateDuration(certificate.getDuration());
        }
    }

    private static void validateName(String name) throws IncorrectParameterException {
        if (StringUtils.isBlank(name)
                || !Pattern.compile(NAME_REGEX).matcher(name.trim()).find()) {
            throw new IncorrectParameterException("name", name, INCORRECT_CERTIFICATE_PARAMETER);
        }
    }

    private static void validateDescription(String description) throws IncorrectParameterException {
        if (StringUtils.isBlank(description)
                || !Pattern.compile(DESCRIPTION_REGEX).matcher(description.trim()).find()) {
            throw new IncorrectParameterException("description", description, INCORRECT_CERTIFICATE_PARAMETER);
        }
    }

    private static void validatePrice(BigDecimal price) throws IncorrectParameterException {
        if (price == null || price.scale() > MAX_SCALE
                || price.compareTo(MIN_PRICE) < 0
                || price.compareTo(MAX_PRICE) > 0) {
            throw new IncorrectParameterException("price", price, INCORRECT_CERTIFICATE_PARAMETER);
        }
    }

    private static void validateDuration(Short duration) throws IncorrectParameterException {
        if (duration == null || duration < MIN_DURATION || duration > MAX_DURATION) {
            throw new IncorrectParameterException("duration", duration, INCORRECT_CERTIFICATE_PARAMETER);
        }
    }
}
