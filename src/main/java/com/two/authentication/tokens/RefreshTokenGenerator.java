package com.two.authentication.tokens;

import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenGenerator {

    private final Algorithm algorithm = Algorithm.HMAC256("NOT_THE_REAL_SECRET_REFRESH");
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenGenerator.class);

    /**
     * @return a refresh token holding the uid. This token has no expiration date.
     */
    String createRefreshToken(int uid) {
        logger.info("Creating Refresh token for UID: " + uid);
        return TwoToken.withoutExpiration()
                .withClaim("role", "REFRESH")
                .withClaim("userId", uid)
                .sign(algorithm);
    }
}
