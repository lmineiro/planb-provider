package org.zalando.planb.provider.realms;

import org.zalando.planb.provider.dto.UserData;
import org.zalando.planb.provider.dto.UserPasswordHash;
import org.zalando.planb.provider.exceptions.NotFoundException;
import org.zalando.planb.provider.exceptions.realms.UserRealmAuthenticationException;
import org.zalando.planb.provider.exceptions.realms.UserRealmAuthorizationException;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toSet;
import static org.zalando.planb.provider.realms.Realm.checkBCryptPassword;
import static org.zalando.planb.provider.exceptions.realms.UserRealmAuthenticationException.userNotFound;
import static org.zalando.planb.provider.exceptions.realms.UserRealmAuthenticationException.wrongUserSecret;

public interface UserManagedRealm extends UserRealm {

    @Override
    default Map<String, String> authenticate(String username, String password, Set<String> scopes, Set<String> defaultScopes)
            throws UserRealmAuthenticationException, UserRealmAuthorizationException {
        final UserData user = get(username).orElseThrow(() -> userNotFound(username, getName()));

        if (!user.getPasswordHashes().stream().map(UserPasswordHash::getPasswordHash).anyMatch(passwordHash -> checkBCryptPassword(password, passwordHash))) {
            throw wrongUserSecret(username, getName());
        }

        final Set userScopes = ((Map) user.getScopes()).keySet();
        final Set<String> missingScopes = scopes.stream()
                .filter(scope -> !defaultScopes.contains(scope))
                .filter(scope -> !userScopes.contains(scope))
                .collect(toSet());

        if (!missingScopes.isEmpty()) {
            throw new UserRealmAuthorizationException(username, getName(), missingScopes);
        }

        return singletonMap(SUB, username);
    }

    void update(String username, UserData data) throws NotFoundException;

    void delete(String username) throws NotFoundException;

    void createOrReplace(String username, UserData user);

    void addPassword(String username, UserPasswordHash password);

    Optional<UserData> get(String username);

}
