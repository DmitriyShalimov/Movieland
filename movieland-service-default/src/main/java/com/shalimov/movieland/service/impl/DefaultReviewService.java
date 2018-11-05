package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.ReviewDao;
import com.shalimov.movieland.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultReviewService implements ReviewService {
    private final ReviewDao reviewDao;

    @Autowired
    public DefaultReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @Override
    public boolean addReview(int movieId, String text, int userId) {
        return reviewDao.addReview(movieId, text, userId);
    }
}
