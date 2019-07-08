package com.two.authentication.tokens;

import lombok.Getter;

@Getter
public class Tokens {

    private final RefreshToken refreshToken;
    private final AccessToken accessToken;
    private final ConnectToken connectToken;

    Tokens(RefreshToken refreshToken, AccessToken accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.connectToken = null;
    }

    Tokens(RefreshToken refreshToken, ConnectToken connectToken) {
        this.refreshToken = refreshToken;
        this.accessToken = null;
        this.connectToken = connectToken;
    }

}
