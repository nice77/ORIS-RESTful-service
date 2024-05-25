package org.semester;

import org.junit.jupiter.api.Test;
import org.semester.entity.Token;
import org.semester.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:/application.yaml"})
public class TokenServiceIntegrationTest {

    @Autowired
    private TokenService tokenService;

    @Test
    void testAddToken() throws Exception {
        Token token = new Token(1L, "", false);
        Token returnToken = tokenService.addToken(token);
        assertNotNull(null, returnToken);
    }

    @Test
    void testGetToken() throws Exception {
        assertNull(tokenService.getToken("random_token_that_not_exists"));
    }

    @Test
    void testCheckIfRevoked() throws Exception {
        assertEquals(false, tokenService.checkIfTokenRevoked(1206L));
    }

    @Test
    void testUpdateToken() throws Exception {
        String tokenString = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInR5cGUiOiJyZWZyZXNoIiwiZW1haWwiOiIxQGdtYWlsLmNvbSIsImlhdCI6MTcxNjY1NDc1NSwiZXhwIjoxNzE2NzQxMTU1fQ.U_o7NXkTcsXp4taSF1ccM2Po2_cr4C1cNWE1RZcNRKY";
        Token foundToken = tokenService.getToken(tokenString);
        Boolean currentState = foundToken.getIsRevoked();
        foundToken.setIsRevoked(!currentState);
        tokenService.updateToken(foundToken);
        foundToken = tokenService.getToken(tokenString);
        assertEquals(!currentState, foundToken.getIsRevoked());
    }
}
