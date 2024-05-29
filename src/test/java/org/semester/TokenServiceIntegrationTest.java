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
@TestPropertySource(properties = {"spring.config.location=classpath:/application-test.properties"})
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
        assertNotNull(null, tokenService.getToken("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTikiLCJ0eXBlIjoicmVmcmVzaCIsImVtYWlsIjoiMUBnbWFpbC5jb20iLCJpYXQiOjE3MTY3MzA2NzIsImV4cCI6MTcxNjgxNzA3Mn0.L5tjpk0dlAci0DiSXIXoGRulLwRwAPepaCNZE7PEfgM"));
    }

    @Test
    void testCheckIfRevoked() throws Exception {
        assertEquals(false, tokenService.checkIfTokenRevoked(2L));
    }

    @Test
    void testUpdateToken() throws Exception {
        String tokenString = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTikiLCJ0eXBlIjoicmVmcmVzaCIsImVtYWlsIjoiMUBnbWFpbC5jb20iLCJpYXQiOjE3MTY3MzA4NzMsImV4cCI6MTcxNjgxNzI3M30.tV6ryfGjWWPvklkVAtySj5ScJA-mwZLpLNiWRJSYr1Y";
        Token foundToken = tokenService.getToken(tokenString);
        Boolean currentState = foundToken.getIsRevoked();
        foundToken.setIsRevoked(!currentState);
        tokenService.updateToken(foundToken);
        foundToken = tokenService.getToken(tokenString);
        assertEquals(!currentState, foundToken.getIsRevoked());
    }
}
