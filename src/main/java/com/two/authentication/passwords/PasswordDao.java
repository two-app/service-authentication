package com.two.authentication.passwords;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.jooq.generated.Tables.USERS;

@Repository
public class PasswordDao {

    private final DSLContext ctx;

    @Autowired
    public PasswordDao(DSLContext ctx) {
        this.ctx = ctx;
    }

    /**
     * @return the password looked up by UID. Empty optional if the UID does not exist.
     */
    Optional<String> getPassword(int uid) {
        return this.ctx.select(USERS.PASSWORD)
                .from(USERS)
                .where(USERS.UID.eq(uid)).fetchOptionalInto(String.class);
    }
}
