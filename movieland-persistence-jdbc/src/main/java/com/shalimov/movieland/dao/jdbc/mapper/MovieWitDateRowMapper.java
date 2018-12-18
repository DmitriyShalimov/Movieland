package com.shalimov.movieland.dao.jdbc.mapper;

import com.shalimov.movieland.entity.Movie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieWitDateRowMapper  implements RowMapper<Movie> {
    @Override
    public Movie mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Movie movie = new Movie();
        movie.setId(resultSet.getInt("id"));
        movie.setReviewsCount(resultSet.getInt("rewiew_count"));
        movie.setNameRussian(resultSet.getString("name_russian"));
        movie.setNameNative(resultSet.getString("name_native"));
        movie.setDescription(resultSet.getString("description"));
        movie.setPicturePath(resultSet.getString("picture_path"));
        movie.setPrice(resultSet.getDouble("price"));
        movie.setYearOfRelease(resultSet.getInt("year_of_release"));
        movie.setRating(resultSet.getDouble("rating"));
        movie.setAddDate(resultSet.getTimestamp("add_date").toLocalDateTime());
        movie.setModifyDate(resultSet.getTimestamp("modify_date").toLocalDateTime());
        return movie;
    }
}