package org.zalando.planb.provider.api.controller;

import org.junit.Test;
import org.zalando.planb.provider.config.RealmConfig;
import org.zalando.planb.provider.exceptions.realms.RealmNotFoundException;

import java.util.Optional;

public class OIDCControllerTest {

    @Test(expected = RealmNotFoundException.class)
    public void realmNotFound() {
        RealmConfig realms = new RealmConfig(null);
        OIDCController.getRealmName(realms, Optional.empty(), Optional.of("some.header.value"));
    }

}
