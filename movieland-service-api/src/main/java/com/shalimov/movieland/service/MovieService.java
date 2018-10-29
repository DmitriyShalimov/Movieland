package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.Currency;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.entity.MovieRequest;

import java.util.List;

public interface MovieService {
    List<Movie> getAll(MovieRequest movieRequest);

    List<Movie> getRandomMovies(MovieRequest movieRequest);

    List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest);

    Movie getMovieById(int movieId, Currency currency);
}
