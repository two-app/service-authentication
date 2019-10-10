package com.two.authentication.credentials;

import com.two.http_api.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CredentialsService {

    private final PasswordEncoder passwordEncoder;
    private final CredentialsDao credentialsDao;
    private static final Logger logger = LoggerFactory.getLogger(CredentialsService.class);

    /**
     * @param user with credentials to encode and store.
     * @throws ResponseStatusException Bad Request if the credentials uid already exists.
     */
    void storeCredentials(User.WithCredentials user) throws ResponseStatusException {
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        EncodedCredentials encodedCredentials = new EncodedCredentials(user.getUser().getUid(), encodedPassword);
        logger.info("Encoded credentials.");

        try {
            this.credentialsDao.storeCredentials(encodedCredentials);
            logger.info("Successfully stored credentials.");
        } catch (DuplicateKeyException e) {
            logger.warn("Failed to store credentials. UID already exists.", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user already exists.", e);
        }
    }

    /**
     * @param user with credentials to encode and compare.
     * @return true if the users credentials are equal to those stored.
     * @throws ResponseStatusException if the user does not exist. The client requesting this validation should have
     *                                 the UID from the users service. If this is not present, the data is mismatched.
     */
    boolean validateCredentials(User.WithCredentials user) throws ResponseStatusException {
        int uid = user.getUser().getUid();

        logger.info("Validating credentials for UID {}.", uid);
        Optional<EncodedCredentials> encodedCredentials = this.credentialsDao.getCredentials(uid);

        if (encodedCredentials.isEmpty()) {
            logger.error("Credentials for UID {} do not exist.", uid);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "This user does not exist.");
        }

        logger.info("Credentials successfully retrieved for UID {}.", uid);
        logger.info("Encoding the raw password and comparing it with the encoded credentials.");
        return encodedCredentials.get().getEncodedPassword().equals(
                passwordEncoder.encode(user.getPassword())
        );
    }

}
