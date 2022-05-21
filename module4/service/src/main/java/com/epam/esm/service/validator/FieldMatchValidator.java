package com.epam.esm.service.validator;

import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @SneakyThrows
    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        Object firstField = BeanUtils.getProperty(value, firstFieldName);
        Object secondField = BeanUtils.getProperty(value, secondFieldName);

        boolean valid = isEqual(firstField, secondField);
        if (!valid) {
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(firstFieldName)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }

    private boolean isEqual(Object firstField, Object secondField) {
        return firstField == null
                && secondField == null
                || firstField != null
                && firstField.equals(secondField);
    }
}