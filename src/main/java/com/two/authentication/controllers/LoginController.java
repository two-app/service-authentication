package com.two.authentication.controllers;

import com.two.authentication.passwords.PasswordService;
import com.two.authentication.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class LoginController {

    private final UsersService usersService;
    private final PasswordService passwordService;

    @Autowired
    public LoginController(UsersService usersService, PasswordService passwordService) {
        this.usersService = usersService;
        this.passwordService = passwordService;
    }

    @GetMapping("/login")
    void login(
            @RequestHeader("email") String email
    ) {
        this.usersService.getUser(email);
    }

}
