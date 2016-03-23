package org.zalando.planb.provider.config;

import com.google.common.collect.ImmutableSet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zalando.planb.provider.exceptions.realms.RealmNotFoundException;
import org.zalando.planb.provider.config.properties.RealmProperties;
import org.zalando.planb.provider.realms.impl.CassandraClientRealm;
import org.zalando.planb.provider.realms.impl.CassandraUserRealm;
import org.zalando.planb.provider.realms.ClientRealm;
import org.zalando.planb.provider.realms.UserRealm;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class RealmConfig implements BeanFactoryAware {
    private final Map<String, ClientRealm> clientRealms = new HashMap<>();
    private final Map<String, UserRealm> userRealms = new HashMap<>();
    private BeanFactory beanFactory;

    private static final Pattern HOST_WORD_BOUNDARY = Pattern.compile("[.-]");

    private RealmProperties realmProperties;

    @Autowired
    public RealmConfig(RealmProperties realmProperties) {
        this.realmProperties = realmProperties;
    }

    public static String ensureLeadingSlash(String realmName) {
        return realmName.startsWith("/") ? realmName : "/" + realmName;
    }

    public static String stripLeadingSlash(String realm) {
        return realm.startsWith("/") ? realm.substring(1) : realm;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    void newRealm(String realmName, Class<? extends ClientRealm> clientRealmImpl, Class<? extends UserRealm> userRealmImpl) {
        ClientRealm clientRealm = beanFactory.getBean(clientRealmImpl);
        clientRealm.initialize(realmName);
        clientRealms.put(realmName, clientRealm);

        UserRealm userRealm = beanFactory.getBean(userRealmImpl);
        userRealm.initialize(realmName);
        userRealms.put(realmName, userRealm);
    }

    @PostConstruct
    public void setup() {
        for (String realmName : realmProperties.getNames()) {
            Class<? extends ClientRealm> clientImpl = realmProperties.getClientImpl(realmName, CassandraClientRealm.class);
            Class<? extends UserRealm> userImpl = realmProperties.getUserImpl(realmName, CassandraUserRealm.class);
            newRealm(realmName, clientImpl, userImpl);
        }
    }

    public static Optional<String> findRealmNameInHost(@NotNull final Set<String> realmNames, @NotNull final String host) {
        Set<String> hostParts = ImmutableSet.copyOf(HOST_WORD_BOUNDARY.split(host));
        return realmNames.stream()
                .filter(realm -> hostParts.contains(stripLeadingSlash(realm)))
                .sorted()
                .findFirst();
    }

    public Optional<String> findRealmNameInHost(@NotNull final String host) {
        return findRealmNameInHost(clientRealms.keySet(), host);
    }

    public UserRealm getUserRealm(String name) {
        UserRealm realm = userRealms.get(name);
        if (realm == null) {
            throw new RealmNotFoundException(name);
        }
        return realm;
    }

    public ClientRealm getClientRealm(String name) {
        ClientRealm realm = clientRealms.get(name);
        if (realm == null) {
            throw new RealmNotFoundException(name);
        }
        return realm;
    }
}
