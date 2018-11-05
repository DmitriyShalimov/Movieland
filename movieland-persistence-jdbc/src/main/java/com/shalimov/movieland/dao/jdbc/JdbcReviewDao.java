package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.ReviewDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcReviewDao implements ReviewDao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String ADD_NEW_REVIEW_SQL = "INSERT INTO review (movie,\"text\",\"user\") VALUES(:movie, :text, :user);";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcReviewDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public boolean addReview(int movieId, String text, int userId) {
        logger.info("Start upload review for user with id: {}", userId);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("movie", movieId);
        params.addValue("text", text);
        params.addValue("user", userId);
        int result = namedParameterJdbcTemplate.update(ADD_NEW_REVIEW_SQL, params);
        logger.info("Review  saved");
        return result == 1;
    }
}
