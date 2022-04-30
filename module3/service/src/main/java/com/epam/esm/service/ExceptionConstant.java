package com.epam.esm.service;

/**
 * ExceptionConstant
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
public class ExceptionConstant {

    private ExceptionConstant() {
    }

    public static final String CERTIFICATE_NAME = "error.certificate.name";
    public static final String CERTIFICATE_DESCRIPTION = "error.certificate.description";
    public static final String CERTIFICATE_PRICE = "error.certificate.price";
    public static final String CERTIFICATE_DURATION = "error.certificate.duration";
    public static final String CERTIFICATE_DUPLICATE = "error.certificate.duplicate";
    public static final String ORDER_COST = "error.order.cost";

    public static final String RESOURCE_NOT_FOUND = "error.resource.notFound";

    public static final String INCORRECT_PAGE_NUMBER = "error.page.number";
    public static final String INCORRECT_PAGE_SIZE = "error.page.size";

    public static final String TAG_EXIST = "error.tag.duplicate";
    public static final String TAG_NAME = "error.tag.name";

    public static final String ACCESS_DENIED = "error.access";
}
