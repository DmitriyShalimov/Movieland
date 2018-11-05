package com.shalimov.movieland.service;

public interface ReviewService {
    boolean addReview(int movieId, String text, int userId);
}
