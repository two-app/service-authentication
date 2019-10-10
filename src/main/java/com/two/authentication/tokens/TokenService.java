package com.two.authentication.tokens;

import com.two.http_api.model.Tokens;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service to generate both a refresh token and access token.
 */
@Service
@AllArgsConstructor
public class TokenService {

    private final RefreshTokenGenerator refreshTokenGenerator;
    private final AccessTokenGenerator accessTokenGenerator;
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    /**
     * @param uid who the token will belong to.
     * @param pid optional partner id. If this is not present the user will receive a connect token.
     * @param cid optional couple id, compulsory if the partner id is present (and vice-versa).
     * @return a Tokens object.
     * @throws ResponseStatusException Bad Request if the pid or cid is provided, but the other is not present.
     */
    public Tokens createTokens(int uid, Integer pid, Integer cid) {
        logger.info("Creating tokens with UID: {}, PID: {}, and CID: {}.", uid, pid, cid);
        if ((pid != null && cid == null) || pid == null && cid != null) {
            logger.warn("Either PID or CID is present and the other is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both PID and CID must be provided.");
        }

        String refreshToken = this.refreshTokenGenerator.createRefreshToken(uid);

        if (pid == null) {
            logger.info("PID not present. Creating Connect token.");
            return new Tokens(refreshToken, this.accessTokenGenerator.createConnectToken(uid));
        } else {
            logger.info("PID present. Creating Access token.");
            return new Tokens(refreshToken, this.accessTokenGenerator.createAccessToken(uid, pid, cid));
        }
    }

}
