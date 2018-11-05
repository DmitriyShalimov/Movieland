package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getByEmail(String email);

}
