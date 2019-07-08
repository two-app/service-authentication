package com.two.authentication.tokens;

import com.two.authentication.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

/**
 * Service to generate both a refresh token and access token.
 */
@Service
public class TokensService {

    /**
     * @param userId    who the token will belong to.
     * @param partnerId optional partner id. If this is not present the user will receive a connect token.
     * @param coupleId  optional couple id, compulsory if the partner id is present.
     * @return a Tokens object.
     */
    public Tokens createTokens(int userId, Integer partnerId, Integer coupleId) {
        if (partnerId != null && coupleId == null) {
            throw new BadRequestException("Both partner ID and couple ID must be provided.");
        }

        RefreshToken refreshToken = this.createRefreshToken(userId);

        if (partnerId == null) {
            return new Tokens(refreshToken, this.createConnectToken(userId));
        } else {
            return new Tokens(refreshToken, this.createAccessToken(userId, partnerId, coupleId));
        }
    }

    private AccessToken createAccessToken(int userId, int partnerId, int coupleId) {
        return null;
    }

    private ConnectToken createConnectToken(int userId) {
        return null;
    }

    private RefreshToken createRefreshToken(int userId) {
        return null;
    }

}
