package com.shalimov.movieland.service.entity;

import com.shalimov.movieland.entity.User;

public final class UserToken {
    private final String uuid;
    private final User user;
    private final long creationTime;

    public UserToken(String uuid, User user, long creationTime) {
        this.uuid = uuid;
        this.user = user;
        this.creationTime = creationTime;
    }

    public String getUuid() {
        return uuid;
    }

    public User getUser() {
        return user;
    }

    public long getCreationTime() {
        return creationTime;
    }
}
