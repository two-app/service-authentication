package com.two.authentication.passwords;

import com.two.authentication.exceptions.BadRequestException;
import dev.testbed.TestBed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PasswordServiceTest {

    private TestBuilder tb;

    @BeforeEach
    void setUp() {
        this.tb = new TestBuilder();
    }

    @Nested
    class IsPasswordValid {

        @Test
        @DisplayName("it should encrypt the password and match them")
        void encryptsAndMatchesPassword() {
            PasswordService passwordService = tb.whenGetPasswordReturn("DbPass").build();

            passwordService.isPasswordValid(1, "EntryPass");

            verify(tb.getDependency(PasswordEncoder.class)).matches("EntryPass", "DbPass");
        }

        @Test
        @DisplayName("it should retrieve the users password from the database")
        void retrievesPasswordFromDatabase() {
            PasswordService passwordService = tb.whenGetPasswordReturn("storedPassword").build();

            passwordService.isPasswordValid(1, "test");

            verify(tb.getDependency(PasswordDao.class)).getPassword(1);
        }

        @Test
        @DisplayName("it should throw a Bad Request Exception if the user does not exist.")
        void userNotExists() {
            PasswordService passwordService = tb.whenGetPasswordReturnEmptyOptional().build();

            assertThatThrownBy(() -> passwordService.isPasswordValid(1, "tp"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Unknown username or password.");
        }

        @Test
        @DisplayName("it should return false if the passwords do not match.")
        void passwordsNotMatch() {
            PasswordService passwordService = tb.whenGetPasswordReturn("TestPassword").build();

            boolean isValid = passwordService.isPasswordValid(1, "TestPasswordMismatch");

            assertThat(isValid).isFalse();
        }

    }

    class TestBuilder extends TestBed<PasswordService, TestBuilder> {
        TestBuilder() {
            super(PasswordService.class);
        }

        TestBuilder whenGetPasswordReturn(String password) {
            when(getDependency(PasswordDao.class).getPassword(anyInt())).thenReturn(Optional.of(password));
            return this;
        }

        TestBuilder whenGetPasswordReturnEmptyOptional() {
            when(getDependency(PasswordDao.class).getPassword(anyInt())).thenReturn(Optional.empty());
            return this;
        }
    }
}