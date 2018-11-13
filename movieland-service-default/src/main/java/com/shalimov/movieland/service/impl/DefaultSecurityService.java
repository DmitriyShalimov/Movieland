package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.entity.User;
import com.shalimov.movieland.service.SecurityService;
import com.shalimov.movieland.service.UserService;
import com.shalimov.movieland.service.entity.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static java.lang.System.currentTimeMillis;

@Service
public class DefaultSecurityService implements SecurityService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final List<UserToken> userTokens = new CopyOnWriteArrayList<>();

    @Autowired
    public DefaultSecurityService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<User> authenticate(String email, String password) {
        Optional<User> optionalUser = userService.getByEmail(email);
        Optional<User> optionalCheckedUser = optionalUser.map(user -> checkPassword(user, password));
        optionalCheckedUser.ifPresent(this::saveToken);
        return optionalCheckedUser;
    }

    @Override
    public void logout(String token) {
        userTokens.removeIf(userToken -> userToken.getUuid().equals(token));
    }

    @Override
    public Optional<User> getUser(String token) {
        for (UserToken userToken : userTokens) {
            if (userToken.getUuid().equals(token))
                return Optional.of(userToken.getUser());
        }
        return Optional.empty();
    }

    private User checkPassword(User user, String password) {
        String expectedPassword = DigestUtils.sha1Hex(password + user.getSalt());
        if (user.getPassword().equals(expectedPassword)) {
            user.setPassword(null);
            user.setSalt(null);
            return user;
        } else {
            logger.info("User {} credentials are invalid", user.getNickName());
            return null;
        }
    }

    private void saveToken(User user) {
        UUID uuid = UUID.randomUUID();
        user.setUuid(uuid);
        UserToken userToken = new UserToken(uuid.toString(), user, currentTimeMillis());
        userTokens.add(userToken);
    }

    @Scheduled(fixedDelayString = "${session.cache.update.time}")
    private void invalidate() {
        userTokens.removeIf(userToken -> currentTimeMillis() - userToken.getCreationTime() > 7200000);
    }

}
