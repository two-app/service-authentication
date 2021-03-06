package com.two.authentication.tokens;

import com.two.http_api.model.Tokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@Validated
public class TokensController {

    private static final Logger logger = LoggerFactory.getLogger(TokensController.class);
    private final TokenService tokenService;

    @Autowired
    public TokensController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping(path = "/tokens")
    public Tokens getTokens(
            @RequestHeader("uid") @Min(value = 1, message = "UID must be greater than 0.") int uid,
            @RequestHeader(value = "pid", required = false) @Min(value = 1, message = "PID must be greater than 0.") Integer pid,
            @RequestHeader(value = "cid", required = false) @Min(value = 1, message = "CID must be greater than 0.") Integer cid
    ) {
        logger.info("Creating tokens with UID: {}, PID: {}, and CID: {}.", uid, pid, cid);
        Tokens tokens = this.tokenService.createTokens(uid, pid, cid);

        logger.info("Responding with tokens: {}.", tokens);
        return tokens;
    }

}
