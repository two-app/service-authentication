package com.two.authentication.credentials;

import com.two.authentication.exceptions.BadRequestException;
import com.two.authentication.passwords.PasswordService;
import com.two.http_api.model.User;
import dev.testbed.TestBed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

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

    @Nested
    class StoreCredentials {
        @Test
        @DisplayName("it should encode the credential password")
        void encodesPassword() {
            CredentialsService credentialsService = tb.whenEncodePasswordReturn("encoded").build();

            credentialsService.storeCredentials(new User.Credentials(1, "raw"));

            verify(tb.getDependency(PasswordService.class)).encodePassword("raw");
            verify(tb.getDependency(CredentialsDao.class)).storeCredentials(new EncodedCredentials(1, "encoded"));
        }

        @Test
        @DisplayName("it should throw a BadRequestException if the user exists")
        void userExists() {
            CredentialsService credentialsService = tb.whenStoreCredentialsThrowDuplicateKeyException().build();

            assertThatThrownBy(() -> credentialsService.storeCredentials(new User.Credentials(1, "a")))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("This user already exists.");
        }
    }

    @Nested
    class GetCredentials {
        @Test
        @DisplayName("it should return the credentials")
        void returnsCredentials() {
            EncodedCredentials encodedCredentials = new EncodedCredentials(1, "encoded");
            CredentialsService credentialsService = tb.whenGetCredentialsReturn(of(encodedCredentials)).build();

            Optional<EncodedCredentials> storedCredentials = credentialsService.getCredentials(1);

            assertThat(storedCredentials).isPresent().contains(encodedCredentials);
        }
    }

    class TestBuilder extends TestBed<CredentialsService, TestBuilder> {
        TestBuilder() {
            super(CredentialsService.class);
        }

        TestBuilder whenEncodePasswordReturn(String encodedPassword) {
            when(getDependency(PasswordService.class).encodePassword(anyString())).thenReturn(encodedPassword);
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