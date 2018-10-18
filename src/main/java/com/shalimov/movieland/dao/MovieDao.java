package com.shalimov.movieland.dao;

import com.shalimov.movieland.entity.Movie;

import java.util.List;

public interface MovieDao {
    List<Movie> getAll();

    List<Movie> getRandomMovies();

    List<Movie> getMoviesByGenre(int genreId);

    Movie getMovieById(int movieId);
}
