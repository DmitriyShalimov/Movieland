package com.shalimov.movieland.dao;

public interface ReviewDao {
    boolean addReview(int movieId, String text, int userId);
}
