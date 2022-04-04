package com.epam.esm.service.validator;

import com.epam.esm.domain.Certificate;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Qualifier("certificateValidator")
public class CertificateValidator extends AbstractValidator<Certificate> {

    @Override
    public Set<ConstraintViolation<Certificate>> validate(Certificate certificate) {
        return validator.validate(certificate);
    }
}
