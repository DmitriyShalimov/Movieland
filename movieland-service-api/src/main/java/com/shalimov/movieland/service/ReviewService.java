package com.shalimov.movieland.service;

public interface ReviewService {
    void addReview(int movieId, String text, int userId);
}
