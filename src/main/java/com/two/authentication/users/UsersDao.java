package com.two.authentication.users;

import com.two.authentication.passwords.PasswordService;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.UsersRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.jooq.generated.Tables.USERS;

@Repository
public class UsersDao {

    private final DSLContext ctx;

    @Autowired
    public UsersDao(DSLContext ctx) {
        this.ctx = ctx;
    }

    /**
     * <b>Warning: Do not use this method without going via a service that encodes the password.</b>
     * Do not, under any circumstances, store a non-encoded password.
     *
     * @see PasswordService for encoding.
     * @param encodedPassword the users password, which must be encoded.
     */
    public void createUser(int uid, String encodedPassword) {
        UsersRecord usersRecord = ctx.newRecord(USERS);
        usersRecord.setUid(uid);
        usersRecord.setPassword(encodedPassword);
        usersRecord.store();
    }

}
