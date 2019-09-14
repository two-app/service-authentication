package com.two.authentication.passwords;


import com.two.authentication.users.UsersDao;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JooqTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
class PasswordDaoTest {

    private final Flyway flyway;
    private final PasswordDao passwordDao;
    private final UsersDao usersDao;

    @Autowired
    public PasswordDaoTest(Flyway flyway, DSLContext context) {
        this.flyway = flyway;
        this.passwordDao = new PasswordDao(context);
        this.usersDao = new UsersDao(context);
    }

    @BeforeEach
    void beforeEach() {
        this.flyway.clean();
        this.flyway.migrate();
    }

    @Nested
    class GetPassword {

        @Test
        @DisplayName("Retrieving the password for an unknown user should return an empty optional.")
        void unknownUser_EmptyOptionalPasswordReturned() {
            Optional<String> passwordOptional = passwordDao.getPassword(99);

            assertThat(passwordOptional).isNotPresent();
        }

        @Test
        @DisplayName("It should return the password of a stored user.")
        void knownUser_PasswordReturned() {
            usersDao.createUser(1, "TestPassword");

            Optional<String> passwordOptional = passwordDao.getPassword(1);

            assertThat(passwordOptional).isPresent().hasValue("TestPassword");
        }

    }

}