package com.two.authentication.credentials;

import com.two.authentication.exceptions.BadRequestException;
import com.two.http_api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialsService {

    private final PasswordEncoder passwordEncoder;
    private final CredentialsDao credentialsDao;

    @Autowired
    public CredentialsService(PasswordEncoder passwordEncoder, CredentialsDao credentialsDao) {
        this.passwordEncoder = passwordEncoder;
        this.credentialsDao = credentialsDao;
    }

    /**
     * @param credentials to encode and store.
     * @throws BadRequestException if the credentials uid already exists.
     */
    void storeCredentials(User.Credentials credentials) throws BadRequestException {
        String encodedPassword = this.passwordEncoder.encode(credentials.getRawPassword());
        EncodedCredentials encodedCredentials = new EncodedCredentials(credentials.getUid(), encodedPassword);

        try {
            this.credentialsDao.storeCredentials(encodedCredentials);
        } catch (DuplicateKeyException e) {
            throw new BadRequestException("This user already exists.");
        }
    }

    Optional<EncodedCredentials> getCredentials(int uid) {
        return this.credentialsDao.getCredentials(uid);
    }

}
