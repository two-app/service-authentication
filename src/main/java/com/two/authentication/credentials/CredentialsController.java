package com.two.authentication.credentials;

import com.two.authentication.tokens.TokenService;
import com.two.http_api.api.AuthenticationServiceContract;
import com.two.http_api.model.Tokens;
import com.two.http_api.model.User;
import com.two.http_api.model.UserWithCredentials;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@Validated
@AllArgsConstructor
public class CredentialsController implements AuthenticationServiceContract {

    private final CredentialsService credentialsService;
    private final TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(CredentialsController.class);

    @PostMapping("/credentials")
    @Override
    public Tokens storeCredentialsAndGenerateTokens(@Valid UserWithCredentials user) {
        logger.info("Storing credentials for UID: {}.", user.getUid());
        credentialsService.storeCredentials(user);

        logger.info("Creating tokens with UID: {}, PID: null, and CID: null.", user.getUid());
        Tokens tokens = tokenService.createTokens(user.getUid(), null, null);

        logger.info("Responding with tokens: {}.", tokens);
        return tokens;
    }

    @PostMapping("/authenticate")
    @Override
    public Tokens authenticateCredentialsAndGenerateTokens(UserWithCredentials user) {
        boolean credentialsAreValid = credentialsService.validateCredentials(user);

        if (!credentialsAreValid) {
            logger.warn("User with UID {} provided an incorrect password.", user.getUid());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password.");
        }

        logger.info(
                "Generating tokens with UID: {}, PID: {}, and CID: {}.",
                user.getUid(),
                user.getPid(),
                user.getCid()
        );

        Tokens tokens = tokenService.createTokens(user.getUid(), user.getPid(), user.getCid());

        logger.info("Responding with tokens: {}.", tokens);
        return tokens;
    }

    @PostMapping("/tokens")
    @Override
    public Tokens getToken(@Valid User user) {
        logger.info("Generating tokens for UID {} with PID {} and CID {}.", user.getUid(), user.getPid(), user.getCid());
        return tokenService.createTokens(user.getUid(), user.getPid(), user.getCid());
    }
}
