package com.shalimov.movieland.web.controller;

import com.shalimov.movieland.entity.User;
import com.shalimov.movieland.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpSession;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(path = "/review")
    public ResponseEntity review(@RequestParam int movieId, @RequestParam String text, HttpSession session) {
        if (((User) session.getAttribute("loggedUser")).getUserType() != null) {
            if (reviewService.addReview(movieId, text, ((User) session.getAttribute("loggedUser")).getId()))
                return ResponseEntity.ok().build();
            else
                return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
