package com.two.authentication.tokens;

import lombok.Value;

@Value
public class RefreshToken {

    private final String role = "REFRESH";
    private final String refreshToken;
    private final int userId;

}
