package com.example.hiroka.service;


import com.example.hiroka.user.User;

import java.util.Optional;

public interface AuthService {
    Optional<User> getUser(String username, String password);

    void login(String emailField, String passwordField);

    void register(String value, String value1);
}
