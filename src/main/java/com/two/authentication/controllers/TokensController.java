package com.two.authentication.controllers;

import com.two.authentication.tokens.TokenService;
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

    private final TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(TokensController.class);

    @Autowired
    public TokensController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping(path="/tokens")
    public Tokens getTokens(
            @RequestHeader("userId") @Min(value = 1, message = "User ID must be greater than 0.") int uid,
            @RequestHeader(value = "partnerId", required = false) @Min(value = 1, message = "Partner ID must be greater than 0.") Integer pid,
            @RequestHeader(value = "coupleId", required = false) @Min(value = 1, message = "Couple ID must be greater than 0.") Integer cid
    ) {
        logger.info("[GET] /tokens UID: '" + uid + "', PID: '" + pid + "', and CID: '" + cid + "'.");
        Tokens tokens = this.tokenService.createTokens(uid, pid, cid);

        logger.info("[200] with tokens: " + tokens + ".");
        return tokens;
    }

}
