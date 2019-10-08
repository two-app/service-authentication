package com.two.authentication.credentials;

import com.two.authentication.exceptions.BadRequestException;
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
    public Tokens storeCredentialsAndGenerateTokens(
            @NotNull(message = "You must provide credentials.") User.WithCredentials user
    ) {
        logger.info("Storing credentials for UID: {}.", user.getUser().getUid());
        credentialsService.storeCredentials(user);

        logger.info("Creating tokens with UID: {}, PID: null, and CID: null.", user.getUser().getUid());
        Tokens tokens = tokenService.createTokens(user.getUser().getUid(), null, null);

        logger.info("Responding with tokens: {}.", tokens);
        return tokens;
    }

    @Override
    public Tokens authenticateCredentialsAndGenerateTokens(
            @NotNull(message = "You must provide credentials.") User.WithCredentials user
    ) {
        boolean credentialsAreValid = credentialsService.validateCredentials(user);

        if (!credentialsAreValid) {
            logger.warn("User with UID {} provided an incorrect password.", user.getUser().getUid());
            throw new BadRequestException("Incorrect password.");
        }

        logger.info("Generating tokens with UID: {}, PID: {}, and CID: {}");

        return null;
    }
}
