package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.User;

import java.util.Optional;

public interface SecurityService {
    Optional<User> authenticate(String email, String password);

    void logout(String token);

    Optional<User> getUser(String token);
}
