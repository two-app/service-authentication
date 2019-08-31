package com.two.authentication.tokens;

import com.two.authentication.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to generate both a refresh token and access token.
 */
@Service
public class TokenService {

    private final RefreshTokenGenerator refreshTokenGenerator;
    private final AccessTokenGenerator accessTokenGenerator;

    @Autowired
    public TokenService(RefreshTokenGenerator refreshTokenGenerator, AccessTokenGenerator accessTokenGenerator) {
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.accessTokenGenerator = accessTokenGenerator;
    }

    /**
     * @param userId    who the token will belong to.
     * @param partnerId optional partner id. If this is not present the user will receive a connect token.
     * @param coupleId  optional couple id, compulsory if the partner id is present (and vice-versa).
     * @return a Tokens object.
     */
    public Tokens createTokens(int userId, Integer partnerId, Integer coupleId) {
        if ((partnerId != null && coupleId == null) || partnerId == null && coupleId != null) {
            throw new BadRequestException("Both partner ID and couple ID must be provided.");
        }

        RefreshToken refreshToken = this.refreshTokenGenerator.createRefreshToken(userId);

        if (partnerId == null) {
            return new Tokens(refreshToken, this.accessTokenGenerator.createConnectToken(userId));
        } else {
            return new Tokens(refreshToken, this.accessTokenGenerator.createAccessToken(userId, partnerId, coupleId));
        }
    }

}
