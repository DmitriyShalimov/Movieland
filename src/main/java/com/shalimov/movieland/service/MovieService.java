package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.Currency;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.filter.MovieFilter;

import java.util.List;

public interface MovieService {
    List<Movie> getAll(MovieFilter movieFilter);

    List<Movie> getRandomMovies(MovieFilter movieFilter);

    List<Movie> getMoviesByGenre(int genreId, MovieFilter movieFilter);

    Movie getMovieById(int movieId, Currency currency);
}
