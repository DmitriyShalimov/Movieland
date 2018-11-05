package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.entity.User;
import com.shalimov.movieland.service.SecurityService;
import com.shalimov.movieland.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static java.lang.System.currentTimeMillis;

@Service
@PropertySource("classpath:service.application.properties")
public class DefaultSecurityService implements SecurityService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final Map<UUID, HttpSession> sessions = new HashMap<>();

    @Autowired
    public DefaultSecurityService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<User> authenticate(String email, String password, HttpSession session) {
        Optional<User> optionalUser = userService.getByEmail(email);
        optionalUser.map(user -> checkPassword(optionalUser.get(), password));
        optionalUser.ifPresent(user -> saveSession(optionalUser.get(), session));
        return optionalUser;
    }

    @Override
    public void logout(HttpSession session) {
        sessions.remove(session);
        session.invalidate();
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

    private void saveSession(User user, HttpSession session) {
        UUID uuid = UUID.randomUUID();
        user.setUuid(uuid);
        sessions.put(uuid, session);
    }

    @Scheduled(fixedDelayString = "${cache.update.time}")
    private void invalidate() {
      Iterator<HttpSession> iterator= sessions.values().iterator();
      while (iterator.hasNext()){
          HttpSession session= iterator.next();
          if(currentTimeMillis()-session.getCreationTime()>7200000) {
              session.invalidate();
              iterator.remove();
          }
      }
    }

}
