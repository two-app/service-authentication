package com.two.authentication.users;

import com.two.http_api.api.UserServiceApi;
import com.two.http_api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private UserServiceApi userServiceApi;

    @Autowired
    public UsersService(UserServiceApi userServiceApi) {
        this.userServiceApi = userServiceApi;
    }

    public User getUser(String email) {
        ResponseEntity<User> response = this.userServiceApi.getUser(email);

        return response.getBody();
    }

}
