package com.two.authentication.credentials;

import com.two.authentication.exceptions.BadRequestException;
import com.two.http_api.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CredentialsService {

    private final PasswordEncoder passwordEncoder;
    private final CredentialsDao credentialsDao;
    private static final Logger logger = LoggerFactory.getLogger(CredentialsService.class);

    /**
     * @param credentials to encode and store.
     * @throws BadRequestException if the credentials uid already exists.
     */
    void storeCredentials(User.Credentials credentials) throws BadRequestException {
        String encodedPassword = this.passwordEncoder.encode(credentials.getRawPassword());
        EncodedCredentials encodedCredentials = new EncodedCredentials(credentials.getUid(), encodedPassword);
        logger.info("Encoded credentials.");

        try {
            this.credentialsDao.storeCredentials(encodedCredentials);
            logger.info("Successfully stored credentials.");
        } catch (DuplicateKeyException e) {
            logger.warn("Failed to store credentials. UID already exists.", e);
            throw new BadRequestException("This user already exists.");
        }
    }

    Optional<EncodedCredentials> getCredentials(int uid) {
        logger.info("Retrieving credentials for UID: " + uid);
        Optional<EncodedCredentials> encodedCredentials = this.credentialsDao.getCredentials(uid);

        encodedCredentials.ifPresentOrElse(
                cr -> logger.info("Credentials successfully retrieved for UID: " + cr.getUid() + "."),
                () -> logger.info("Credentials for UID: '" + uid + "' do not exist.")
        );

        return encodedCredentials;
    }

}
