package org.zalando.planb.provider.exceptions.realms;

public class ClientRealmAuthorizationException extends RealmAuthorizationException {

    public ClientRealmAuthorizationException(String identity, String realm, Iterable<String> invalidScopes) {
        super(identity, realm, CLIENT_ERROR, invalidScopes);
    }
}
