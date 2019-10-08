package com.two.authentication.credentials;

import com.two.authentication.exceptions.BadRequestException;
import com.two.http_api.model.User;
import dev.testbed.TestBed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CredentialsServiceTest {

    private TestBuilder tb;

    @BeforeEach
    void setup() {
        this.tb = new TestBuilder();
    }

    private User user = new User(1, null, null, "gerry@two.com", 22, "Gerry");
    private User.WithCredentials userWithCredentials = new User.WithCredentials(user, "rawPassword");

    @Nested
    class StoreCredentials {
        @Test
        @DisplayName("it should encode the credential password")
        void encodesPassword() {
            CredentialsService credentialsService = tb.whenEncodePasswordReturn("encoded").build();

            credentialsService.storeCredentials(userWithCredentials);

            verify(tb.getDependency(PasswordEncoder.class)).encode("rawPassword");
            verify(tb.getDependency(CredentialsDao.class)).storeCredentials(new EncodedCredentials(1, "encoded"));
        }

        @Test
        @DisplayName("it should throw a BadRequestException if the user exists")
        void userExists() {
            CredentialsService credentialsService = tb.whenStoreCredentialsThrowDuplicateKeyException().build();

            assertThatThrownBy(() -> credentialsService.storeCredentials(userWithCredentials))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("This user already exists.");
        }
    }

    @Nested
    class ValidateCredentials {
        @Test
        @DisplayName("it should return true if the credentials exist and the passwords are equal")
        void validCredentials() {
            EncodedCredentials encodedCredentials = new EncodedCredentials(1, "encodedPass");
            CredentialsService credentialsService = tb.whenGetCredentialsReturn(of(encodedCredentials))
                    .whenEncodePasswordReturn("encodedPass")
                    .build();

            boolean isCredentialsValid = credentialsService.validateCredentials(userWithCredentials);

            assertThat(isCredentialsValid).isTrue();
        }

        @Test
        @DisplayName("it should return false if the credentials exist but are not equal")
        void invalidCredentials() {
            EncodedCredentials encodedCredentials = new EncodedCredentials(1, "encodedPass");
            CredentialsService credentialsService = tb.whenGetCredentialsReturn(of(encodedCredentials))
                    .whenEncodePasswordReturn("wrongEncodedPass")
                    .build();

            boolean isCredentialsValid = credentialsService.validateCredentials(userWithCredentials);

            assertThat(isCredentialsValid).isFalse();
        }

        @Test
        @DisplayName("it should throw an internal server error response exception if the credentials do note xist")
        void credentialsDoNotExist() {
            CredentialsService credentialsService = tb.whenGetCredentialsReturn(empty()).build();

            assertThatThrownBy(() -> credentialsService.validateCredentials(userWithCredentials))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    class TestBuilder extends TestBed<CredentialsService, TestBuilder> {
        TestBuilder() {
            super(CredentialsService.class);
        }

        TestBuilder whenEncodePasswordReturn(String encodedPassword) {
            when(getDependency(PasswordEncoder.class).encode(anyString())).thenReturn(encodedPassword);
            return this;
        }

        TestBuilder whenStoreCredentialsThrowDuplicateKeyException() {
            doThrow(new DuplicateKeyException("")).when(getDependency(CredentialsDao.class)).storeCredentials(any());
            return this;
        }

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        TestBuilder whenGetCredentialsReturn(Optional<EncodedCredentials> encodedCredentials) {
            when(getDependency(CredentialsDao.class).getCredentials(anyInt())).thenReturn(encodedCredentials);
            return this;
        }
    }

}