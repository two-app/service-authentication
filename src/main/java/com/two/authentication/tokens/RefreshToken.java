package com.two.authentication.tokens;

import lombok.Value;

@Value
class RefreshToken {

    private final String role = "REFRESH";
    private final String refreshToken;
    private final int userId;

}
