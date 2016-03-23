package org.zalando.planb.provider.exceptions.realms;

import org.zalando.planb.provider.exceptions.RestException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class RealmNotFoundException extends RestException {

    public RealmNotFoundException(String realmName) {
        super(BAD_REQUEST.value(), realmName + " not found", null, "realm_not_found", realmName + " not found");
    }
}
