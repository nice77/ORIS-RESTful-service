package org.semester;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.semester.controller.AuthenticationController;
import org.semester.dto.authServiceDto.AuthorizeRequest;
import org.semester.dto.authServiceDto.RefreshRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"spring.config.location=classpath:/application-test.properties"})
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthentication() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/authorization")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                        {
                                            "email": "1@gmail.com",
                                            "password": "1"
                                        }
                                        """
                                )
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void testNegativeAuthentication() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/authorization")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                        {
                                            "email": "1@gmail.com",
                                            "password": "12"
                                        }
                                        """
                                )
                ).andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void testInvalidRefresh() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/authorization")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                        {
                                            "refresh": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInR5cGUiOiJyZWZyZXNoIiwiZW1haWwiOiIxQGdtYWlsLmNvbSIsImlhdCI6MTcxNjY1NjkyNywiZXhwIjoxNzE2NzQzMzI3fQ.FIxliTOZyKcoMrAn5vwaclJaAzGzk58QK90v_wSV7Nk"
                                        }
                                        """
                                )
                ).andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn();
    }
}
