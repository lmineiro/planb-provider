package org.zalando.planb.provider.api;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.zalando.planb.provider.AbstractSpringTest;
import org.zalando.planb.provider.Main;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringApplicationConfiguration(classes = { Main.class })
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("it")
public class OIDCJWKsIT extends AbstractSpringTest {
    @Value("${local.server.port}")
    private int port;

    RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    @Test
    public void jwksResponse() {
        ResponseEntity<String> response = rest.getForEntity(
                URI.create("http://localhost:" + port + "/oauth2/connect/keys"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);

        // TODO check the actual expected keys
    }
}
