package com.shalimov.movieland.dao.jdbc.mapper;

import com.shalimov.movieland.entity.User;
import com.shalimov.movieland.entity.UserType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserWithRatingRowMapper  implements RowMapper<User> {
    public User mapRow(ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        String firstName = resultSet.getString("firstname");
        String lastName = resultSet.getString("lastname");
        user.setNickName(firstName + " " + lastName);
        user.setEmail(resultSet.getString("email"));
        user.setAverageRating(Double.parseDouble(resultSet.getString("average_rating")));
        user.setReviewCount(Integer.parseInt(resultSet.getString("review_count")));
        return user;
    }
}