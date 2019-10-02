package com.two.authentication.credentials;

import com.two.authentication.tokens.TokenService;
import com.two.http_api.api.AuthenticationServiceContract;
import com.two.http_api.model.Tokens;
import com.two.http_api.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@Validated
@AllArgsConstructor
public class CredentialsController implements AuthenticationServiceContract {

    private final CredentialsService credentialsService;
    private final TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(CredentialsController.class);

    @PostMapping("/credentials")
    @Override
    public Tokens storeCredentialsAndGenerateTokens(@NotNull(message = "You must provide credentials.") User.Credentials credentials) {
        logger.info("[POST] /credentials UID: " + credentials.getUid() + ".");
        logger.debug("User Credentials: " + credentials);
        credentialsService.storeCredentials(credentials);

        logger.info("Creating tokens with UID: '" + credentials.getUid() + "', PID: null, Couple ID: null.");
        Tokens tokens = tokenService.createTokens(credentials.getUid(), null, null);

        logger.info("[200] with tokens: " + tokens + ".");
        return tokens;
    }

}
