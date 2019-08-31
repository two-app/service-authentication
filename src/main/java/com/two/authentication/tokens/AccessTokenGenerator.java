package com.two.authentication.tokens;

import com.auth0.jwt.algorithms.Algorithm;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenGenerator {

    private final Algorithm algorithm = Algorithm.HMAC256("NOT_THE_REAL_SECRET");
    private final Hashids hashids;

    @Autowired
    public AccessTokenGenerator(Hashids hashids) {
        this.hashids = hashids;
    }

    String createAccessToken(int userId, int partnerId, int coupleId) {
        return TwoToken.withExpiration()
                .withClaim("role", "ACCESS")
                .withClaim("userId", userId)
                .withClaim("partnerId", partnerId)
                .withClaim("coupleId", coupleId)
                .sign(algorithm);
    }

    String createConnectToken(int userId) {
        String connectCode = this.hashids.encode(userId);

        return TwoToken.withExpiration()
                .withClaim("role", "ACCESS")
                .withClaim("userId", userId)
                .withClaim("connectCode", connectCode)
                .sign(algorithm);
    }

}
