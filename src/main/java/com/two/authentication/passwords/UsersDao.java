package com.two.authentication.passwords;

import org.jooq.DSLContext;
import org.jooq.generated.tables.records.UsersRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.jooq.generated.Tables.USERS;

@Repository
public class UsersDao {

    private final DSLContext ctx;

    @Autowired
    UsersDao(DSLContext ctx) {
        this.ctx = ctx;
    }

    /**
     * <b>Warning: Do not use this method without going via a service that encrypts the password.</b>
     * Do not, under any circumstances, store a non-encrypted password.
     *
     * @see PasswordService for encryption.
     * @param uid the users ID.
     * @param encodedPassword the users password, which must be encrypted.
     */
    void createUser(int uid, String encodedPassword) {
        UsersRecord usersRecord = ctx.newRecord(USERS);
        usersRecord.setUid(uid);
        usersRecord.setPassword(encodedPassword);
        usersRecord.store();
    }

}
