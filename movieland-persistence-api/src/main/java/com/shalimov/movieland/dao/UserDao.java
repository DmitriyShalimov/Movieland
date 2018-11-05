package com.shalimov.movieland.dao;

import com.shalimov.movieland.entity.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> getByEmail(String email);
}
