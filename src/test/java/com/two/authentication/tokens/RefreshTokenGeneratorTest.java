package com.two.authentication.tokens;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.testbed.TestBed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenGeneratorTest {


    private RefreshToken refreshToken;
    private DecodedJWT decodedRefreshToken;

    @BeforeEach
    void createRefreshToken() {
        this.refreshToken = new TestBuilder().build().createRefreshToken(1);
        this.decodedRefreshToken = JWT.decode(this.refreshToken.getRefreshToken());
    }

    @Nested
    class DecodedToken {

        @Test
        void hasRefreshRole() {
            assertThat(decodedRefreshToken.getClaim("role").asString()).isEqualTo("REFRESH");
        }

        @Test
        void hasUserIdEqualToOne() {
            assertThat(decodedRefreshToken.getClaim("userId").asInt()).isEqualTo(1);
        }

        @Test
        void hasExpirationWithinTwoMinutes() {
            assertThat(decodedRefreshToken.getExpiresAt()).isInSameMinuteAs(Date.from(Instant.now().plus(2, ChronoUnit.MINUTES)));
        }

    }

    @Nested
    class RefreshTokenObj {

        @Test
        void hasRefreshRole() {
            assertThat(refreshToken.getRole()).isEqualTo("REFRESH");
        }

        @Test
        void hasUserIdEqualToOne() {
            assertThat(refreshToken.getUserId()).isEqualTo(1);
        }

    }

    class TestBuilder extends TestBed<RefreshTokenGenerator, TestBuilder> {
        TestBuilder() {
            super(RefreshTokenGenerator.class);
        }
    }

}