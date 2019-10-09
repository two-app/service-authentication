package com.two.authentication.tokens;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.testbed.TestBed;
import org.hashids.Hashids;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class AccessTokenGeneratorTest {

    @Nested
    class CreateAccessToken {

        private DecodedJWT decodedAccessToken;

        @BeforeEach
        void createAccessToken() {
            this.decodedAccessToken = JWT.decode(new TestBuilder().build().createAccessToken(1, 2, 3));
        }

        @Nested
        class DecodedToken {

            @Test
            void hasAccessRole() {
                assertThat(decodedAccessToken.getClaim("role").asString()).isEqualTo("ACCESS");
            }

            @Test
            void hasUIDEqualToOne() {
                assertThat(decodedAccessToken.getClaim("uid").asInt()).isEqualTo(1);
            }

            @Test
            void PIDEqualToTwo() {
                assertThat(decodedAccessToken.getClaim("pid").asInt()).isEqualTo(2);
            }

            @Test
            void hasCIDEqualToThree() {
                assertThat(decodedAccessToken.getClaim("cid").asInt()).isEqualTo(3);
            }

            @Test
            void hasExpirationWithinTwoMinutes() {
                assertThat(decodedAccessToken.getExpiresAt()).isInSameMinuteAs(Date.from(Instant.now().plus(2, ChronoUnit.MINUTES)));
            }

        }

    }

    @Nested
    class CreateConnectToken {

        private TestBuilder tb;
        private DecodedJWT decodedConnectToken;

        @BeforeEach
        void createConnectToken() {
            this.tb = new TestBuilder().whenCreateConnectCodeReturn("TestConnectCode");
            this.decodedConnectToken = JWT.decode(this.tb.build().createConnectToken(1));
        }

        @Nested
        class DecodedToken {

            @Test
            void hasAccessRole() {
                assertThat(decodedConnectToken.getClaim("role").asString()).isEqualTo("ACCESS");
            }


            @Test
            void hasUIDEqualToOne() {
                assertThat(decodedConnectToken.getClaim("uid").asInt()).isEqualTo(1);
            }

            @Test
            void hasCorrectConnectCode() {
                assertThat(decodedConnectToken.getClaim("connectCode").asString()).isEqualTo("TestConnectCode");
            }

            @Test
            void hasExpirationWithinTwoMinutes() {
                assertThat(decodedConnectToken.getExpiresAt()).isInSameMinuteAs(Date.from(Instant.now().plus(2, ChronoUnit.MINUTES)));
            }
        }
    }

    class TestBuilder extends TestBed<AccessTokenGenerator, TestBuilder> {
        TestBuilder() {
            super(AccessTokenGenerator.class);
        }

        TestBuilder whenCreateConnectCodeReturn(String connectCode) {
            when(this.getDependency(Hashids.class).encode(anyLong())).thenReturn(connectCode);
            return this;
        }
    }

}
