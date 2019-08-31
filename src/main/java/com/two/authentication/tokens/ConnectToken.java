package com.two.authentication.tokens;

import lombok.Value;

@Value
public class ConnectToken {

    private final String role = "ACCESS";
    private final String connectToken;

    private final int userId;
    private final String connectCode;

}
