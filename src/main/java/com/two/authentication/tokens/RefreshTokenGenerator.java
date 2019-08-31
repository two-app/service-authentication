package com.two.authentication.tokens;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class RefreshTokenGenerator {

    private final Algorithm algorithm = Algorithm.HMAC256("NOT_THE_REAL_SECRET_REFRESH");
    private final Hashids hashids;

    @Autowired
    public RefreshTokenGenerator(Hashids hashids) {
        this.hashids = hashids;
    }

    public RefreshToken createRefreshToken(int userId) {
        String token = JWT.create()
                .withIssuer("two")
                .withClaim("role", "REFRESH")
                .withClaim("userId", userId)
                .withExpiresAt(Date.from(Instant.now().plus(2, ChronoUnit.MINUTES)))
                .sign(algorithm);

        return new RefreshToken(token, userId);
    }
}
