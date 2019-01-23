package com.shalimov.movieland.dao;

public interface ReviewDao {
    void addReview(int movieId, String text, int userId);
}
