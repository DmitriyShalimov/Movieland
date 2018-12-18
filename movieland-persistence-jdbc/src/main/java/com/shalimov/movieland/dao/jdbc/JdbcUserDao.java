package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.UserDao;
import com.shalimov.movieland.dao.jdbc.mapper.UserRowMapper;
import com.shalimov.movieland.dao.jdbc.mapper.UserWithRatingRowMapper;
import com.shalimov.movieland.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserDao implements UserDao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final RowMapper<User> USER_ROW_MAPPER = new UserRowMapper();
    private static final RowMapper<User> USER_WITH_RATING_ROW_MAPPER = new UserWithRatingRowMapper();


    private static final String GET_USER_BY_EMAIL = "SELECT id,firstname,lastName,email, password,salt,role FROM \"user\"  WHERE email=:email";
    private static final String getTopUsers = "SELECT id,firstname,lastName,email, average_rating,review_count FROM \"user\"";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcUserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        logger.info("Start getting user {}", email);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);
        try {
            User user = namedParameterJdbcTemplate.queryForObject(GET_USER_BY_EMAIL, params, USER_ROW_MAPPER);
            logger.info("User {} was found", email);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            logger.info("User {} was not found", email);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getTopUsers() {
        logger.info("start receiving top users");
        return namedParameterJdbcTemplate.query(getTopUsers, USER_WITH_RATING_ROW_MAPPER);
    }

}
