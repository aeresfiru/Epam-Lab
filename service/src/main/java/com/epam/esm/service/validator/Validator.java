package com.epam.esm.service.validator;

import org.springframework.stereotype.Component;

@Component
public abstract class Validator<T> {

    public abstract void validate(T t);
}
