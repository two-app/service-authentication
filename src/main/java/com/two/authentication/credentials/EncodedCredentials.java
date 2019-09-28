package com.two.authentication.credentials;

import lombok.Value;

@Value
class EncodedCredentials {
    private final int uid;
    private final String encodedPassword;
}
