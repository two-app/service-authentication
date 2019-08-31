package com.two.authentication.tokens;

import lombok.Value;

@Value
public class AccessToken {

    private final String role = "ACCESS";
    private final String accessToken;

    private final int userId;
    private final int partnerId;
    private final int coupleId;

}
