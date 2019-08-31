package com.two.authentication.tokens;

import lombok.Getter;

@Getter
public class Tokens {
    private final String refreshToken;
    private final String accessToken;

    public Tokens(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
