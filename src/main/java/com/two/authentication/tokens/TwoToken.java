package com.two.authentication.tokens;

import com.auth0.jwt.JWT;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utility class to maintain the issuer and access expiration date.
 */
class TwoToken {

    /**
     * @return a premade JWT builder with the 'two' issuer and expiration set at 2 minutes.
     */
    static com.auth0.jwt.JWTCreator.Builder withExpiration() {
        return JWT.create()
                .withIssuer("two")
                .withExpiresAt(Date.from(Instant.now().plus(2, ChronoUnit.MINUTES)));
    }

    static com.auth0.jwt.JWTCreator.Builder withoutExpiration() {
        return JWT.create().withIssuer("two");
    }


}
