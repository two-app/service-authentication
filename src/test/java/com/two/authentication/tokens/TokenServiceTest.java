package com.two.authentication.tokens;

import com.two.authentication.exceptions.BadRequestException;
import dev.testbed.TestBed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    @Nested
    class CreateTokens {

        private TestBuilder tb;

        @BeforeEach
        void createTokenService() {
            this.tb = new TestBuilder();
        }

        @Test
        @DisplayName("with a partner id but no couple id a BadRequestException is thrown")
        void invalidPartnerCombo() {
            assertThatThrownBy(() -> tb.build().createTokens(1, 2, null))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Both partner ID and couple ID must be provided.");
        }

        @Test
        @DisplayName("with a couple id but no partner id a BadRequestException is thrown")
        void invalidCoupleCombo() {
            assertThatThrownBy(() -> tb.build().createTokens(1, null, 3))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Both partner ID and couple ID must be provided.");
        }

        @Test
        @DisplayName("with a partner id and a couple id, an access token is generated")
        void accessTokenGenerated() {
            TokenService tokenService = tb.whenCreateAccessTokenReturn("test").build();

            Tokens tokens = tokenService.createTokens(1, 2, 3);

            assertThat(tokens.getAccessToken()).isEqualTo("test");
        }

        @Test
        @DisplayName("with a partner id and a couple id, an access token is generated using the correct generators")
        void accessTokenGeneratorsCalledCorrectly() {
            tb.build().createTokens(1, 2, 3);

            verify(tb.getDependency(AccessTokenGenerator.class)).createAccessToken(1, 2, 3);
            verify(tb.getDependency(AccessTokenGenerator.class), never()).createConnectToken(anyInt());
        }

        @Test
        @DisplayName("with no partner id, a connect token is generated")
        void connectTokenGenerated() {
            TokenService tokenService = tb.whenCreateConnectTokenReturn("test").build();

            Tokens tokens = tokenService.createTokens(1, null, null);

            assertThat(tokens.getAccessToken()).isEqualTo("test");
        }

        @Test
        @DisplayName("with no partner id, a connect token is generated using the correct generators")
        void connectTokenGeneratorsCalledCorrectly() {
            tb.build().createTokens(1, null, null);

            verify(tb.getDependency(AccessTokenGenerator.class)).createConnectToken(1);
            verify(tb.getDependency(AccessTokenGenerator.class), never()).createAccessToken(anyInt(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("a refresh token is generated")
        void refreshTokenGenerated() {
            TokenService tokenService = tb.whenCreateRefreshTokenReturn("test").build();

            Tokens tokens = tokenService.createTokens(1, null, null);

            assertThat(tokens.getRefreshToken()).isEqualTo("test");
        }

        @Test
        @DisplayName("a refresh token is generated using the correct generators")
        void refreshTokenGeneratorsCalledCorrectly() {
            tb.build().createTokens(1, 2, 3);

            verify(tb.getDependency(RefreshTokenGenerator.class)).createRefreshToken(1);
        }

    }

    class TestBuilder extends TestBed<TokenService, TestBuilder> {
        TestBuilder() {
            super(TokenService.class);
        }

        TestBuilder whenCreateRefreshTokenReturn(String refreshToken) {
            when(getDependency(RefreshTokenGenerator.class).createRefreshToken(anyInt())).thenReturn(refreshToken);
            return this;
        }

        TestBuilder whenCreateAccessTokenReturn(String accessToken) {
            when(getDependency(AccessTokenGenerator.class).createAccessToken(anyInt(), anyInt(), anyInt())).thenReturn(accessToken);
            return this;
        }

        TestBuilder whenCreateConnectTokenReturn(String connectToken) {
            when(getDependency(AccessTokenGenerator.class).createConnectToken(anyInt())).thenReturn(connectToken);
            return this;
        }
    }

}