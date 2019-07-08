package com.two.authentication.access;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokensServiceTest {

    @Nested
    class CreateToken {

        @Test
        @DisplayName("with a partner id but no couple id a BadRequestException is thrown")
        void invalidCombo() {

        }

        @Test
        @DisplayName("with a partner id and a couple id, an access token is generated")
        void accessTokenGenerated() {

        }

        @Test
        @DisplayName("with no partner id, a connect token is generated")
        void connectTokenGenerated() {

        }

        @Test
        @DisplayName("a refresh token is generated")
        void refreshTokenGenerated() {

        }

    }

}