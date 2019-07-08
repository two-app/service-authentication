package com.two.authentication.tokens;

import lombok.Value;

@Value
public class Tokens {

    private final RefreshToken refreshToken;
    private final AccessToken accessToken;
    private final ConnectToken connectToken;

}
