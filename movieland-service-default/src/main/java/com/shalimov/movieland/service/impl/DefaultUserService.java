package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.UserDao;
import com.shalimov.movieland.entity.User;
import com.shalimov.movieland.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultUserService implements UserService {
    private final UserDao userDao;

    @Autowired
    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public List<User> getTopUsers() {
        return userDao.getTopUsers();
    }
}
