package com.two.authentication.access;

import com.two.authentication.tokens.AccessToken;
import com.two.authentication.tokens.ConnectToken;
import com.two.authentication.tokens.RefreshToken;
import com.two.authentication.tokens.Tokens;
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
        return null;
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
