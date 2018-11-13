package com.shalimov.movieland.web.entity;

import com.shalimov.movieland.entity.User;

public class UserHandler {
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();
    public static User getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }
}
