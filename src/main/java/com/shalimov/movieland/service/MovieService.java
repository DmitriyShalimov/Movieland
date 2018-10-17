package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAll();

    List<Movie> getRandomMovies();

    List<Movie> getMoviesByGenre(int id);
}
