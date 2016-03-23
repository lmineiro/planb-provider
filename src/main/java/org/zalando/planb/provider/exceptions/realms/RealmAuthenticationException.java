package org.zalando.planb.provider.exceptions.realms;

import org.zalando.planb.provider.exceptions.RestException;

/**
 * If identity doesn't exist or secret is wrong
 */
public class RealmAuthenticationException extends RestException {

    public RealmAuthenticationException(int statusCode, String message, String errorLocation, String errorType, String errorDescription) {
        super(statusCode, message, errorLocation, errorType, errorDescription);
    }
}
