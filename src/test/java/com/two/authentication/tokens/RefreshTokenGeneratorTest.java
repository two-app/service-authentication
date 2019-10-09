package com.two.authentication.tokens;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.testbed.TestBed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenGeneratorTest {

    private DecodedJWT decodedRefreshToken;

    @BeforeEach
    void createRefreshToken() {
        this.decodedRefreshToken = JWT.decode(new TestBuilder().build().createRefreshToken(1));
    }

    @Nested
    class DecodedToken {

        @Test
        void hasRefreshRole() {
            assertThat(decodedRefreshToken.getClaim("role").asString()).isEqualTo("REFRESH");
        }

        @Test
        void hasUIDEqualToOne() {
            assertThat(decodedRefreshToken.getClaim("uid").asInt()).isEqualTo(1);
        }

    }

    class TestBuilder extends TestBed<RefreshTokenGenerator, TestBuilder> {
        TestBuilder() {
            super(RefreshTokenGenerator.class);
        }
    }

}