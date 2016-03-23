package org.zalando.planb.provider.realms;

import org.zalando.planb.provider.dto.ClientData;
import org.zalando.planb.provider.exceptions.realms.ClientRealmAuthenticationException;
import org.zalando.planb.provider.exceptions.realms.ClientRealmAuthorizationException;

import java.util.Optional;
import java.util.Set;

public interface ClientRealm extends Realm {

    void authenticate(String clientId, String clientSecret, Set<String> scopes, Set<String> defaultScopes)
            throws ClientRealmAuthenticationException, ClientRealmAuthorizationException;

    Optional<ClientData> get(String clientId);
}
