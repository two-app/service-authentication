package com.two.authentication.credentials;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.CredentialsRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.jooq.generated.Tables.CREDENTIALS;

@Repository
@AllArgsConstructor
public class CredentialsDao {

    private final DSLContext ctx;
    private static final Logger logger = LoggerFactory.getLogger(CredentialsDao.class);

    /**
     * <b>Warning: Do not use this method without going via a service that encodes the password.</b>
     * Do not, under any circumstances, store a non-encoded password.
     *
     * @param encodedCredentials the credentials containing the encoded password.
     * @see org.springframework.security.crypto.password.PasswordEncoder for encoding.
     * @throws DuplicateKeyException if a record with the same uid exists.
     */
    void storeCredentials(EncodedCredentials encodedCredentials) throws DuplicateKeyException {
        logger.info("Storing credentials in DB table 'CREDENTIALS'.");
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
        logger.info("Retrieving credentials for UID: " + uid + ".");
        return ctx.selectFrom(CREDENTIALS)
                .where(CREDENTIALS.UID.eq(uid))
                .fetchOptional()
                .map(u -> new EncodedCredentials(u.getUid(), u.getPassword()));
    }
}
