package com.epam.esm.service.validator;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.ExceptionResult;
import com.epam.esm.service.IncorrectParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * TagValidator
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Slf4j
@Component
@Qualifier("tagValidator")
public class TagValidator extends Validator<TagDto> {

    @Value("#{new Integer('${tag.name.max_length}')}")
    private int maxNameLength;

    @Value("#{new Integer('${tag.name.min_length}')}")
    private int minNameLength;

    @Override
    public void validateForCreate(TagDto dto) throws IncorrectParameterException {
        log.info("Validating tag: " + dto + ".");
        ExceptionResult result = new ExceptionResult(new HashMap<>());

        if (!this.isValidString(dto.getName(), minNameLength, maxNameLength)) {
            result.addException(ExceptionConstant.TAG_NAME, dto.getName());
        }
        this.throwExceptionIfEntityIsNotValid(result);
    }

    @Override
    public void validateForUpdate(TagDto tagDto) throws IncorrectParameterException {
        throw new UnsupportedOperationException("This operation is unsupported");
    }
}
