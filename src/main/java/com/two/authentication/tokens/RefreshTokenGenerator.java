package com.two.authentication.tokens;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenGenerator {

    private final Algorithm algorithm = Algorithm.HMAC256("NOT_THE_REAL_SECRET_REFRESH");

    /**
     * @return a refresh token holding the uid. This token has no expiration date.
     */
    String createRefreshToken(int userId) {
        return TwoToken.withoutExpiration()
                .withClaim("role", "REFRESH")
                .withClaim("userId", userId)
                .sign(algorithm);
    }
}
