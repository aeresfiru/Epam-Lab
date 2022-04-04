package com.epam.esm.service.validator;


import com.epam.esm.domain.Tag;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Qualifier("tagValidator")
public class TagValidator extends AbstractValidator<Tag> {

    @Override
    public Set<ConstraintViolation<Tag>> validate(Tag tag) {
        return validator.validate(tag);
    }
}
