package com.shalimov.movieland.dao.jdbc.mapper;

import com.shalimov.movieland.entity.Movie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MovieRowMapper implements RowMapper<Movie> {
    @Override
    public Movie mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Movie movie = new Movie();
        movie.setId(resultSet.getInt("id"));
        movie.setNameRussian(resultSet.getString("name_russian"));
        movie.setNameNative(resultSet.getString("name_native"));
        movie.setDescription(resultSet.getString("description"));
        movie.setPicturePath(resultSet.getString("picture_path"));
        movie.setPrice(resultSet.getDouble("price"));
        movie.setYearOfRelease(resultSet.getInt("year_of_release"));
        movie.setRating(resultSet.getDouble("rating"));
        movie.setCountries(new ArrayList<>());
        movie.setGenres(new ArrayList<>());
        return movie;
    }
}