package org.zalando.planb.provider.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BadRequestException extends RestException {

    public BadRequestException(String message, String errorType, String errorDescription) {
        super(BAD_REQUEST.value(), message, null, errorType, errorDescription);
    }
}
