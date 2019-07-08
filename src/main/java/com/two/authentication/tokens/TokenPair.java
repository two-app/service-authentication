package com.two.authentication.access;

import lombok.Value;

@Value
public class TokenPair {

    private final String refreshToken;
    private final String accessToken;

}
