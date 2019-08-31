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

        private AccessToken accessToken;
        private DecodedJWT decodedAccessToken;

        @BeforeEach
        void createAccessToken() {
            this.accessToken = new TestBuilder().build().createAccessToken(1, 2, 3);
            this.decodedAccessToken = JWT.decode(this.accessToken.getAccessToken());
        }

        @Nested
        class DecodedToken {

            @Test
            void hasAccessRole() {
                assertThat(decodedAccessToken.getClaim("role").asString()).isEqualTo("ACCESS");
            }

            @Test
            void hasUserIdEqualToOne() {
                assertThat(decodedAccessToken.getClaim("userId").asInt()).isEqualTo(1);
            }

            @Test
            void hasPartnerIdEqualToTwo() {
                assertThat(decodedAccessToken.getClaim("partnerId").asInt()).isEqualTo(2);
            }

            @Test
            void hasCoupleIdEqualToThree() {
                assertThat(decodedAccessToken.getClaim("coupleId").asInt()).isEqualTo(3);
            }

            @Test
            void hasExpirationWithinTwoMinutes() {
                assertThat(decodedAccessToken.getExpiresAt()).isInSameMinuteAs(Date.from(Instant.now().plus(2, ChronoUnit.MINUTES)));
            }

        }

        @Nested
        class AccessTokenObj {

            @Test
            void hasUserIdEqualToOne() {
                assertThat(accessToken.getUserId()).isEqualTo(1);
            }

            @Test
            void hasPartnerIdEqualToTwo() {
                assertThat(accessToken.getPartnerId()).isEqualTo(2);
            }

            @Test
            void hasCoupleIdEqualToThree() {
                assertThat(accessToken.getCoupleId()).isEqualTo(3);
            }

        }

    }

    @Nested
    class CreateConnectToken {

        private TestBuilder testBuilder;
        private ConnectToken connectToken;
        private DecodedJWT decodedConnectToken;

        @BeforeEach
        void createConnectToken() {
            this.testBuilder = new TestBuilder().whenCreateConnectCodeReturn("TestConnectCode");
            this.connectToken = this.testBuilder.build().createConnectToken(1);
            this.decodedConnectToken = JWT.decode(this.connectToken.getConnectToken());
        }

        @Nested
        class DecodedToken {

            @Test
            void hasAccessRole() {
                assertThat(decodedConnectToken.getClaim("role").asString()).isEqualTo("ACCESS");
            }


            @Test
            void hasUserIdEqualToOne() {
                assertThat(decodedConnectToken.getClaim("userId").asInt()).isEqualTo(1);
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

        @Nested
        class ConnectTokenObj {

            @Test
            void hasUserIdEqualToOne() {
                assertThat(connectToken.getUserId()).isEqualTo(1);
            }

            @Test
            void hasCorrectConnectCode() {
                assertThat(connectToken.getConnectCode()).isEqualTo("TestConnectCode");
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
