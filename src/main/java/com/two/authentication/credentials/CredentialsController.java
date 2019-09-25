package com.two.authentication.credentials;

import com.two.authentication.tokens.TokenService;
import com.two.http_api.api.AuthenticationServiceContract;
import com.two.http_api.model.Tokens;
import com.two.http_api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@Validated
public class CredentialsController implements AuthenticationServiceContract {

    private final CredentialsService userService;
    private final TokenService tokenService;

    @Autowired
    public CredentialsController(CredentialsService credentialsService, TokenService tokenService) {
        this.userService = credentialsService;
        this.tokenService = tokenService;
    }

    @PostMapping("/credentials")
    @Override
    public ResponseEntity<Tokens> storeCredentialsAndGenerateTokens(@NotNull(message = "You must provide credentials.") User.Credentials credentials) {
        this.userService.storeCredentials(credentials);

        Tokens tokens = tokenService.createTokens(credentials.getUid(), null, null);

        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

}
