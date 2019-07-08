package com.two.authentication.access;

import com.two.authentication.exceptions.BadRequestException;
import com.two.authentication.tokens.TokensService;
import dev.testbed.TestBed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TokensServiceTest {

    @Nested
    class CreateToken {

        @Test
        @DisplayName("with a partner id but no couple id a BadRequestException is thrown")
        void invalidCombo() {
            TokensService tokensService = new TestBuilder().build();

            assertThatThrownBy(() -> tokensService.createTokens(1, 2, null))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Both partner ID and couple ID must be provided.");
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

    class TestBuilder extends TestBed<TokensService, TestBuilder> {
        TestBuilder() { super(TokensService.class); }
    }

}