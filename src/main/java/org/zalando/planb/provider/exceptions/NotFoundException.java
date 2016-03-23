package org.zalando.planb.provider.exceptions;

public class NotFoundException extends RestException {

    public NotFoundException(String message) {
        super(404, message, null, "not_found", message);
    }
}
