package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.User;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public interface SecurityService {
    Optional<User> authenticate(String email, String password, HttpSession session);

  void logout(HttpSession session);
}
