package com.shalimov.movieland.dao.jpa;

import com.shalimov.movieland.dao.ReviewDao;
import com.shalimov.movieland.entity.Review;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class JpaReviewDao implements ReviewDao {


    private final SessionFactory sessionFactory;

    @Autowired
    public JpaReviewDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addReview(int movieId, String text, int userId) {
        Review review = new Review();
        review.setUser(userId);
        getCurrentSession().persist(review);
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
