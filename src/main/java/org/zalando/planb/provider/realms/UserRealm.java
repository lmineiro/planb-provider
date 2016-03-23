package org.zalando.planb.provider.realms;

import org.zalando.planb.provider.exceptions.realms.UserRealmAuthenticationException;
import org.zalando.planb.provider.exceptions.realms.UserRealmAuthorizationException;

import java.util.Map;
import java.util.Set;

public interface UserRealm extends Realm {

    Map<String, String> authenticate(String username, String password, Set<String> scopes, Set<String> defaultScopes)
            throws UserRealmAuthenticationException, UserRealmAuthorizationException;
}
