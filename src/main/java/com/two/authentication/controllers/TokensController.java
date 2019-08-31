package com.two.authentication.controllers;

import com.two.authentication.tokens.TokenService;
import com.two.authentication.tokens.Tokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@Validated
public class TokensController {

//    private final TokenService tokenService;
//
//    @Autowired
//    public TokensController(TokenService tokenService) {
//        this.tokenService = tokenService;
//    }

    @GetMapping(path="/tokens")
    public void getTokens(
            @RequestHeader("userId") @Min(value = 1, message = "User ID must be greater than 0.") int userId,
            @RequestHeader(value = "partnerId", required = false) @Min(value = 1, message = "Partner ID must be greater than 0.") Integer partnerId,
            @RequestHeader(value = "coupleId", required = false) @Min(value = 1, message = "Couple ID must be greater than 0.") Integer coupleId
    ) {
//        Tokens tokens = this.tokenService.createTokens(userId, partnerId, coupleId);
    }

}
