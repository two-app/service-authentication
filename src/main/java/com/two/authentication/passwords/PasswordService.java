package com.two.authentication.passwords;

import com.two.authentication.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordService {

    private final PasswordDao passwordDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordService(PasswordDao passwordDao, PasswordEncoder passwordEncoder) {
        this.passwordDao = passwordDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param rawPassword non-encoded string password.
     * @return true if the users password matches that in the database.
     */
    public boolean isPasswordValid(int uid, String rawPassword) {
        Optional<String> storedPassword = this.passwordDao.getPassword(uid);

        if (storedPassword.isEmpty()) {
            throw new BadRequestException("Unknown username or password.");
        }

        return this.passwordEncoder.matches(rawPassword, storedPassword.get());
    }

}
