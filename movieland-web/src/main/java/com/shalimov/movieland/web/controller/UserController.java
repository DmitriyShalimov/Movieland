package com.shalimov.movieland.web.controller;

import com.shalimov.movieland.entity.User;
import com.shalimov.movieland.service.ReviewService;
import com.shalimov.movieland.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SecurityService securityService;
    private final ReviewService reviewService;

    @Autowired
    public UserController(SecurityService securityService, ReviewService reviewService) {
        this.securityService = securityService;
        this.reviewService = reviewService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<User> doLogin(@RequestParam String email, @RequestParam String password, HttpSession session) {

        Optional<User> optionalUser = securityService.authenticate(email, password, session);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            session.setAttribute("loggedUser", user);
            logger.info("User {} logged in", user.getNickName());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(path = "/review")
    public ResponseEntity review(@RequestParam int movieId, @RequestParam String text, HttpSession session) {
        if (reviewService.addReview(movieId, text, ((User) session.getAttribute("loggedUser")).getId()))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value = "/logout")
    public void logout(HttpSession session) {
        session.removeAttribute("loggedUser");
        securityService.logout(session);
    }

}
