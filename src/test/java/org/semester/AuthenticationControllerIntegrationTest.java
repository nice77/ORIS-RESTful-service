package org.semester;

import org.junit.jupiter.api.Test;
import org.semester.controller.AuthenticationController;
import org.semester.dto.authServiceDto.AuthorizeRequest;
import org.semester.dto.authServiceDto.RefreshRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:/application.yaml"})
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private AuthenticationController authenticationController;

    @LocalServerPort
    private int port;

    @Test
    public void test() throws Exception {
        assertThat(authenticationController).isNotNull();
    }

    @Test
    public void testAuthentication() throws Exception {
        AuthorizeRequest authorizeRequest = new AuthorizeRequest("1@gmail.com", "1");
        ResponseEntity<Object> response = this.authenticationController.authenticate(authorizeRequest);
        assertThat(response).isNotNull();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRefresh() throws Exception {
        RefreshRequest refreshRequest = new RefreshRequest("random_refresh_token_that_does_not_exist");
        ResponseEntity<Object> response = this.authenticationController.refresh_tokens(refreshRequest);
        assertThat(response).isNotNull();
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
