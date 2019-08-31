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
public class AccessTokenGenerator {

    private final Algorithm algorithm = Algorithm.HMAC256("NOT_THE_REAL_SECRET");
    private final Hashids hashids;

    @Autowired
    public AccessTokenGenerator(Hashids hashids) {
        this.hashids = hashids;
    }

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
        String connectCode = this.hashids.encode(userId);

        String token = JWT.create()
                .withIssuer("two")
                .withClaim("userId", userId)
                .withClaim("connectCode", connectCode)
                .withExpiresAt(Date.from(Instant.now().plus(2, ChronoUnit.MINUTES)))
                .sign(algorithm);

        return new ConnectToken(token, userId, connectCode);
    }

}