package com.two.authentication.credentials;

import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@JooqTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
class CredentialsDaoTest {

    private final Flyway flyway;
    private final DSLContext ctx;
    private final CredentialsDao credentialsDao;

    @Autowired
    public CredentialsDaoTest(Flyway flyway, DSLContext ctx) {
        this.flyway = flyway;
        this.ctx = ctx;
        this.credentialsDao = new CredentialsDao(ctx);
    }

    @BeforeEach
    void setup() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @DisplayName("it should store the credentials")
    void storesCredentials() {
        EncodedCredentials encodedCredentials = new EncodedCredentials(1, "test-encoded-password");

        this.credentialsDao.storeCredentials(encodedCredentials);
        Optional<EncodedCredentials> record = this.credentialsDao.getCredentials(1);

        assertThat(record).isPresent().contains(encodedCredentials);
    }

    @Test
    @DisplayName("it should return an empty optional when getting unknown credentials")
    void unknownCredentialsReturnsEmptyOptional() {
        assertThat(this.credentialsDao.getCredentials(3)).isEmpty();
    }

    @Test
    @DisplayName("it should throw a duplicate key exception if the uid is not unique")
    void errorsOnUniqueConstraint() {
        EncodedCredentials encodedCredentials = new EncodedCredentials(1, "test-encoded-password");
        this.credentialsDao.storeCredentials(encodedCredentials);

        assertThatThrownBy(() -> this.credentialsDao.storeCredentials(encodedCredentials))
                .isInstanceOf(DuplicateKeyException.class);
    }

}