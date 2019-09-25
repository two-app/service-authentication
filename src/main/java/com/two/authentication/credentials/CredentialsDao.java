package com.two.authentication.credentials;

import com.two.authentication.passwords.PasswordService;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.CredentialsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.jooq.generated.Tables.CREDENTIALS;

@Repository
public class CredentialsDao {

    private final DSLContext ctx;

    @Autowired
    public CredentialsDao(DSLContext ctx) {
        this.ctx = ctx;
    }

    /**
     * <b>Warning: Do not use this method without going via a service that encodes the password.</b>
     * Do not, under any circumstances, store a non-encoded password.
     *
     * @param encodedCredentials the credentials containing the encoded password.
     * @see PasswordService for encoding.
     * @throws DuplicateKeyException if a record with the same uid exists.
     */
    void storeCredentials(EncodedCredentials encodedCredentials) throws DuplicateKeyException {
        CredentialsRecord record = ctx.newRecord(CREDENTIALS);
        record.setUid(encodedCredentials.getUid());
        record.setPassword(encodedCredentials.getEncodedPassword());
        record.store();
    }

    /**
     * @param uid to lookup the credentials for.
     * @return the encoded credentials if a record exists with that uid.
     */
    Optional<EncodedCredentials> getCredentials(int uid) {
        return ctx.selectFrom(CREDENTIALS)
                .where(CREDENTIALS.UID.eq(uid))
                .fetchOptional()
                .map(u -> new EncodedCredentials(u.getUid(), u.getPassword()));
    }
}
