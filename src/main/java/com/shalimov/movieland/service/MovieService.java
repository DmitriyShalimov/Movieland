package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.filter.MovieFilter;

import java.util.List;

public interface MovieService {
    List<Movie> getAll(MovieFilter movieFilter);

    List<Movie> getRandomMovies(MovieFilter movieFilter);

    List<Movie> getMoviesByGenre(int id, MovieFilter movieFilter);
}
