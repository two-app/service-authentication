package com.two.authentication.tokens;

import com.auth0.jwt.algorithms.Algorithm;
import org.hashids.Hashids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenGenerator {

    private final Algorithm algorithm = Algorithm.HMAC256("NOT_THE_REAL_SECRET");
    private final Hashids hashids;
    private static final Logger logger = LoggerFactory.getLogger(AccessTokenGenerator.class);

    @Autowired
    public AccessTokenGenerator(Hashids hashids) {
        this.hashids = hashids;
    }

    /**
     * @return an access token holding the uid, pid, and cid. Two minute expiry.
     */
    String createAccessToken(int uid, int pid, int cid) {
        logger.info("Creating Access token with UID: {}, PID: {}, and CID: {}.", uid, pid, cid);
        return TwoToken.withExpiration()
                .withClaim("role", "ACCESS")
                .withClaim("userId", uid)
                .withClaim("partnerId", pid)
                .withClaim("coupleId", cid)
                .sign(algorithm);
    }

    /**
     * @param uid to generate the connect code for.
     * @return an connect token, that is used as an access token. Holds the uid and their generated connect code.
     */
    String createConnectToken(int uid) {
        logger.info("Creating Connect token with UID: {}.", uid);
        String connectCode = this.hashids.encode(uid);
        logger.info("Encoded UID to Connect Code: {}.", connectCode);

        return TwoToken.withExpiration()
                .withClaim("role", "ACCESS")
                .withClaim("userId", uid)
                .withClaim("connectCode", connectCode)
                .sign(algorithm);
    }

}
