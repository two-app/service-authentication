package com.two.authentication.passwords;

import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@JooqTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
class UsersDaoTest {

    private final Flyway flyway;
    private final UsersDao usersDao;

    @Autowired
    public UsersDaoTest(Flyway flyway, DSLContext dslContext) {
        this.flyway = flyway;
        this.usersDao = new UsersDao(dslContext);
    }

    // TODO write tests when there is a GetUser method

}