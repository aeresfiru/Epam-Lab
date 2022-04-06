package com.epam.esm.service.validator;

import com.epam.esm.domain.Certificate;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ErrorConstraint;
import com.epam.esm.service.exception.IncorrectParameterException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Qualifier("certificateValidator")
public class CertificateValidator extends Validator<Certificate> {
    private static final int MAX_LENGTH_NAME = 100;
    private static final int MIN_LENGTH_NAME = 3;
    private static final int MIN_LENGTH_DESCRIPTION = 20;
    private static final int MAX_LENGTH_DESCRIPTION = 500;
    private static final Integer MAX_SCALE = 2;
    private static final BigDecimal MIN_PRICE = new BigDecimal("0.01");
    private static final BigDecimal MAX_PRICE = new BigDecimal("999.99");
    private static final int MAX_DURATION = 365;
    private static final int MIN_DURATION = 1;

    @Override
    public void validate(Certificate certificate) throws IncorrectParameterException {
        validateName(certificate.getName());
        validateDescription(certificate.getDescription());
        validatePrice(certificate.getPrice());
        validateDuration(certificate.getDuration());
    }

    public void validateForUpdate(Certificate certificate) {
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

    private void validateName(String name) throws IncorrectParameterException {
        if (name == null
                || name.length() < MIN_LENGTH_NAME
                || name.length() > MAX_LENGTH_NAME) {
            throw new IncorrectParameterException(
                    ErrorConstraint.INVALID_CERTIFICATE_NAME,
                    ErrorCode.CERTIFICATE_ERROR);
        }
    }

    private void validateDescription(String description) throws IncorrectParameterException {
        if (description == null
                || description.length() > MAX_LENGTH_DESCRIPTION
                || description.length() < MIN_LENGTH_DESCRIPTION) {
            throw new IncorrectParameterException(
                    ErrorConstraint.INVALID_CERTIFICATE_DESCRIPTION,
                    ErrorCode.CERTIFICATE_ERROR);
        }
    }

    private void validatePrice(BigDecimal price) throws IncorrectParameterException {
        if (price == null
                || price.scale() > MAX_SCALE
                || price.compareTo(MIN_PRICE) < 0
                || price.compareTo(MAX_PRICE) > 0) {
            throw new IncorrectParameterException(
                    ErrorConstraint.INVALID_CERTIFICATE_PRICE,
                    ErrorCode.CERTIFICATE_ERROR);
        }
    }

    private void validateDuration(Short duration) throws IncorrectParameterException {
        if (duration == null
                || duration < MIN_DURATION
                || duration > MAX_DURATION) {
            throw new IncorrectParameterException(
                    ErrorConstraint.INVALID_CERTIFICATE_DURATION,
                    ErrorCode.CERTIFICATE_ERROR);
        }
    }
}
