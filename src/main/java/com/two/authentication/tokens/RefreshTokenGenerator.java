package com.two.authentication.tokens;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenGenerator {

    private final Algorithm algorithm = Algorithm.HMAC256("NOT_THE_REAL_SECRET_REFRESH");

    public RefreshToken createRefreshToken(int userId) {
        String token = TwoToken.withoutExpiration()
                .withClaim("role", "REFRESH")
                .withClaim("userId", userId)
                .sign(algorithm);

        return new RefreshToken(token, userId);
    }
}
