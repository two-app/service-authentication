package com.two.authentication.credentials;

import com.two.authentication.exceptions.BadRequestException;
import com.two.authentication.passwords.PasswordService;
import com.two.http_api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialsService {

    private final PasswordService passwordService;
    private CredentialsDao credentialsDao;

    @Autowired
    public CredentialsService(PasswordService passwordService, CredentialsDao credentialsDao) {
        this.passwordService = passwordService;
        this.credentialsDao = credentialsDao;
    }

    public void storeCredentials(User.Credentials credentials) {
        String encodedPassword = this.passwordService.encodePassword(credentials.getRawPassword());
        EncodedCredentials encodedCredentials = new EncodedCredentials(credentials.getUid(), encodedPassword);

        try {
            this.credentialsDao.storeCredentials(encodedCredentials);
        } catch (DuplicateKeyException e) {
            throw new BadRequestException("This user already exists.");
        }
    }

    public Optional<EncodedCredentials> getCredentials(int uid) {
        return this.credentialsDao.getCredentials(uid);
    }

}
