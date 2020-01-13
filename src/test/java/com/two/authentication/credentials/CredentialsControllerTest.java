package com.two.authentication.credentials;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.two.authentication.tokens.TokenService;
import com.two.http_api.model.Tokens;
import com.two.http_api.model.User;
import com.two.http_api.model.UserWithCredentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CredentialsController.class)
@AutoConfigureMockMvc
public class CredentialsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private CredentialsService credentialsService;

    @MockBean
    private TokenService tokenService;

    private User user = User.builder().uid(12).pid(null).cid(null).firstName("Two").lastName("TwoL").build();
    private UserWithCredentials userWithCredentials = UserWithCredentials.fromUser(user, "two@two.com", "rawPassword");

    @AfterEach
    void afterEach() {
        Mockito.reset(credentialsService, tokenService);
    }

    @Nested
    class StoreCredentials {
        @Test
        @DisplayName("it should store the users valid credentials")
        void validCredentials() throws Exception {
            Tokens tokens = new Tokens("test-refresh-token", "test-access-token");
            when(tokenService.createTokens(user.getUid(), user.getPid(), user.getCid())).thenReturn(tokens);

            postCredentials(userWithCredentials).andExpect(status().isOk())
                    .andExpect(content().bytes(om.writeValueAsBytes(tokens)));

            verify(credentialsService).storeCredentials(userWithCredentials);
        }

        @Test
        @DisplayName("it should return a bad request if the credentials uid already exists")
        void uidExists() throws Exception {
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "user exists")).when(credentialsService)
                    .storeCredentials(userWithCredentials);

            postCredentials(userWithCredentials)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("user exists"));
        }

        @Test
        @DisplayName("it should return a bad request if the request body is missing")
        void emptyBody() throws Exception {
            postCredentials(null).andExpect(status().isBadRequest());
        }

        private ResultActions postCredentials(UserWithCredentials userWithCredentials) throws Exception {
            return mvc.perform(
                    post("/credentials")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsBytes(userWithCredentials))
            );
        }
    }

    @Nested
    class AuthenticateCredentials {
        @Test
        @DisplayName("it should return tokens when the credentials are valid")
        void validCredentials() throws Exception {
            when(credentialsService.validateCredentials(userWithCredentials)).thenReturn(true);

            Tokens tokens = new Tokens("refresh-token", "access-token");
            when(tokenService.createTokens(user.getUid(), user.getPid(), user.getCid())).thenReturn(tokens);

            postAuthenticate(userWithCredentials).andExpect(status().isOk())
                    .andExpect(content().bytes(om.writeValueAsBytes(tokens)));
        }

        @Test
        @DisplayName("it should return a bad request if the credentials are invalid")
        void invalidCredentials() throws Exception {
            when(credentialsService.validateCredentials(userWithCredentials)).thenReturn(false);

            postAuthenticate(userWithCredentials).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Incorrect password."));
        }

        @Test
        @DisplayName("it should return a bad request if the request body is missing")
        void emptyBody() throws Exception {
            postAuthenticate(null).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Badly formed HTTP request."));
        }

        private ResultActions postAuthenticate(UserWithCredentials userWithCredentials) throws Exception {
            return mvc.perform(post("/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userWithCredentials))
            );
        }
    }

    @Nested
    class GetTokens {
        @Test
        @DisplayName("it should return tokens for a valid user")
        void returnsTokens() throws Exception {
            Tokens tokens = new Tokens("test-refresh-token", "test-access-token");
            when(tokenService.createTokens(user.getUid(), user.getPid(), user.getCid())).thenReturn(tokens);

            postTokens(user).andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").isNotEmpty())
                    .andExpect(jsonPath("$.refreshToken").isNotEmpty());

            verify(tokenService).createTokens(user.getUid(), user.getPid(), user.getCid());
        }

        @Test
        @DisplayName("it should return a bad request if the request body is missing")
        void emptyBody() throws Exception {
            postTokens(null).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Badly formed HTTP request."));
        }

        private ResultActions postTokens(User user) throws Exception {
            return mvc.perform(post("/tokens")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(user))
            );
        }
    }

}