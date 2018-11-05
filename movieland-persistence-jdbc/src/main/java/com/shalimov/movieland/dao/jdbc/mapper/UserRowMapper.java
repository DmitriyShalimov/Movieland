package com.shalimov.movieland.dao.jdbc.mapper;

import com.shalimov.movieland.entity.User;
import com.shalimov.movieland.entity.UserType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    public User mapRow(ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        String firstName=resultSet.getString("firstname");
        String lastName=resultSet.getString("lastname");
        user.setNickName(firstName+" "+lastName);
        user.setEmail(resultSet.getString("email"));
        user.setUserType(UserType.getTypeById(resultSet.getString("role")));
        user.setPassword(resultSet.getString("password"));
        user.setSalt(resultSet.getString("salt"));
        return user;
    }
}
