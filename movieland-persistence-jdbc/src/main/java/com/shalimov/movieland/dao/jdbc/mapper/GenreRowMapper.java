package com.shalimov.movieland.dao.jdbc.mapper;

import com.shalimov.movieland.entity.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreRowMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        int id=resultSet.getInt("id");
        String title=resultSet.getString("name");
        Genre genre = new Genre(id, title);
        return genre;
    }
}
