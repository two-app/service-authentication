package com.two.authentication.credentials;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.two.authentication.exceptions.BadRequestException;
import com.two.authentication.tokens.TokenService;
import com.two.http_api.model.Tokens;
import com.two.http_api.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CredentialsController.class)
@AutoConfigureMockMvc
public class CredentialsControllerTest {

    private final String path = "/credentials";
    private final ObjectMapper m = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CredentialsService credentialsService;
    @MockBean
    private TokenService tokenService;
    private User.Credentials credentials = new User.Credentials(12, "raw-password");

    @Test
    @DisplayName("it should store the users valid credentials")
    void validCredentials() throws Exception {
        Tokens tokens = new Tokens("test-refresh-token", "test-access-token");
        when(tokenService.createTokens(12, null, null)).thenReturn(tokens);

        mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(m.writeValueAsBytes(credentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").value("test-refresh-token"))
                .andExpect(jsonPath("$.accessToken").value("test-access-token"));

        verify(credentialsService).storeCredentials(credentials);
    }

    @Test
    @DisplayName("it should return a bad request if the credentials uid already exists")
    void uidExists() throws Exception {
        doThrow(new BadRequestException("user exists")).when(credentialsService).storeCredentials(credentials);

        mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(m.writeValueAsBytes(credentials)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("user exists"));
    }

    @Test
    @DisplayName("it should return a bad request if the credentials are missing from the request")
    void credentialsMissing() throws Exception {
        mvc.perform(post(path)).andExpect(status().isBadRequest());
    }

}