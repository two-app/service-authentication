package com.two.authentication.tokens;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AccessTokenGenerator {

    private final Algorithm algorithm = Algorithm.HMAC256("NOT_THE_REAL_SECRET");

    public AccessToken createAccessToken(int userId, int partnerId, int coupleId) {
        String token = JWT.create()
                .withIssuer("two")
                .withClaim("userId", userId)
                .withClaim("partnerId", partnerId)
                .withClaim("coupleId", coupleId)
                .withExpiresAt(Date.from(Instant.now().plus(2, ChronoUnit.MINUTES)))
                .sign(algorithm);

        return new AccessToken(token, userId, partnerId, coupleId);
    }

    public ConnectToken createConnectToken(int userId) {
        String token = JWT.create()
                .withIssuer("two")
                .withClaim("userId", userId)
                .withExpiresAt(Date.from(Instant.now().plus(2, ChronoUnit.MINUTES)))
                .sign(algorithm);

        return new ConnectToken(token, userId, null)
    }

}
