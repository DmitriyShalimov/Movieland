package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.Currency;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.entity.MovieRequest;

import java.util.List;

public interface MovieService {
    List<Movie> getAll(MovieRequest movieRequest);

    List<Movie> getRandomMovies();

    List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest);

    Movie getMovieById(int movieId, Currency currency);

    void editMovie(Movie movie);

    void addMovie(Movie movie);

    void markMovieToDelete(int movieId);

    void unmarkMovieToDelete(int movieId);

    List<Movie> getMoviesByMask(String mask, MovieRequest movieRequest);

}
