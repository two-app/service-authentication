package com.two.authentication.tokens;

import com.two.http_api.model.Tokens;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TokensController.class)
@AutoConfigureMockMvc
class TokensControllerTest {

    private final String path = "/tokens";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private TokenService tokenService;

    @Test
    void missingUID_BadRequest() throws Exception {
        mvc.perform(get(path))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing header uid."));
    }

    @Test
    void invalidUID_BadRequest() throws Exception {
        mvc.perform(get(path).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).header("uid", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("UID must be greater than 0."));
    }

    @Test
    void invalidPID_BadRequest() throws Exception {
        mvc.perform(get(path).header("uid", 1).header("pid", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("PID must be greater than 0."));
    }

    @Test
    void invalidCID_BadRequest() throws Exception {
        mvc.perform(get(path).header("uid", 1).header("cid", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("CID must be greater than 0."));
    }

    @Test
    void onlyUID_AccessTokenReturned() throws Exception {
        Tokens tokens = new Tokens(
                "test.refresh.token",
                "test.access.token"
        );

        when(tokenService.createTokens(1, null, null)).thenReturn(tokens);

        mvc.perform(get(path).header("uid", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").value("test.refresh.token"))
                .andExpect(jsonPath("$.accessToken").value("test.access.token"));

        verify(tokenService).createTokens(1, null, null);
    }

}