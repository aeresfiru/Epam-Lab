package com.epam.esm.service.validator;

import com.epam.esm.repository.Pagination;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.ExceptionResult;
import com.epam.esm.service.IncorrectParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * PageValidator
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Slf4j
@Component
@Qualifier("pageValidator")
public class PageValidator extends Validator<Pagination> {

    @Value("#{new Integer('${page.size.min}')}")
    private int minPageSize;

    @Value("#{new Integer('${page.size.max}')}")
    private int maxPageSize;

    @Value("#{new Integer('${page.number.min}')}")
    private int minPageNumber;

    @Override
    public void validateForCreate(Pagination pagination) throws IncorrectParameterException {
        log.info("Validating pagination: " + pagination + ".");
        ExceptionResult result = new ExceptionResult(new HashMap<>());

        if (pagination.getPage() == null
                || pagination.getPage() < minPageNumber) {
            result.addException(ExceptionConstant.INCORRECT_PAGE_NUMBER, pagination.getPage());
        }

        if (pagination.getPageSize() == null
                || pagination.getPageSize() > maxPageSize
                || pagination.getPageSize() < minPageSize) {
            result.addException(ExceptionConstant.INCORRECT_PAGE_SIZE, pagination.getPageSize());
        }
        this.throwExceptionIfEntityIsNotValid(result);
    }

    @Override
    public void validateForUpdate(Pagination pagination) throws IncorrectParameterException {
        throw new UnsupportedOperationException("This operation is unsupported");
    }
}
