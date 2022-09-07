package com.epam.esm.service.exception;

public class ErrorCode {

    private ErrorCode() {
    }

    public static final String INCORRECT_CERTIFICATE_PARAMETER = "40001";

    public static final String INCORRECT_TAG_PARAMETER = "40002";

    public static final String CERTIFICATE_NOT_FOUND = "40401";

    public static final String TAG_NOT_FOUND = "40402";

    public static final String DUPLICATE_CERTIFICATE_NAME = "40901";

    public static final String DUPLICATE_TAG_NAME = "40902";

    public static final String INTERNAL_SERVER_ERROR = "50001";
}
