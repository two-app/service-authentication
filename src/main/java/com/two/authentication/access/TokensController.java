package com.two.authentication.access;

import com.two.authentication.tokens.Tokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokensController {

    @GetMapping(path = "/tokens")
    public Tokens getTokens() {
        return null;
    }

}
