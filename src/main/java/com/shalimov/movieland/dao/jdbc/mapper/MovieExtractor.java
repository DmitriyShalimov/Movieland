package com.shalimov.movieland.dao.jdbc.mapper;

import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.entity.Movie;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MovieExtractor implements ResultSetExtractor<List<Movie>> {
    @Override
    public List<Movie> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Movie> movies = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            Movie movie = null;
            boolean isMoviePresent = false;
            for (Movie tempMovie : movies) {
                if (tempMovie.getId() == id) {
                    isMoviePresent = true;
                    movie = tempMovie;
                }
            }
            if (!isMoviePresent) {
                movie = new Movie();
                movie.setId(id);
                movie.setNameRussian(resultSet.getString("name_russian"));
                movie.setNameNative(resultSet.getString("name_native"));
                movie.setDescription(resultSet.getString("description"));
                movie.setPicturePath(resultSet.getString("picture_path"));
                movie.setPrice(resultSet.getDouble("price"));
                movie.setYearOfRelease(resultSet.getInt("year_of_release"));
                movie.setRating(resultSet.getDouble("rating"));
                movies.add(movie);
            }
            List<Genre> genres = movie.getGenres();
            if (genres == null) {
                genres = new ArrayList<>();
                movie.setGenres(genres);
            }
            Genre genre = new Genre();
            genre.setId(resultSet.getInt("genre_id"));
            genre.setTitle(resultSet.getString("genre_name"));
            if (!genres.contains(genre)) {
                genres.add(genre);
            }
            List<Country> countries = movie.getCountries();
            if (countries == null) {
                countries = new ArrayList<>();
                movie.setCountries(countries);
            }
            Country country = new Country();
            country.setId(resultSet.getInt("country_id"));
            country.setName(resultSet.getString("country_name"));
            if (!countries.contains(country)) {
                countries.add(country);
            }
        }
        return movies;
    }
}
