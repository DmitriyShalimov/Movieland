package com.shalimov.movieland.web.controller;

import com.shalimov.movieland.entity.User;
import com.shalimov.movieland.entity.UserType;
import com.shalimov.movieland.service.ReviewService;
import com.shalimov.movieland.web.annotation.ProtectedBy;
import com.shalimov.movieland.web.entity.UserHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/review")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @ProtectedBy(UserType.USER)
    @PostMapping
    public void review(@RequestParam int movieId, @RequestParam String text) {
        User user = UserHandler.getUser();
        reviewService.addReview(movieId, text, user.getId());
    }
}
