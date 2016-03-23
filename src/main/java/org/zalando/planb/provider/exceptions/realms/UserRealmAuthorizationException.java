package org.zalando.planb.provider.exceptions.realms;

import org.zalando.planb.provider.exceptions.realms.RealmAuthorizationException;

public class UserRealmAuthorizationException extends RealmAuthorizationException {

    public UserRealmAuthorizationException(String identity, String realm, Iterable<String> invalidScopes) {
        super(identity, realm, USER_ERROR, invalidScopes);
    }
}
