package com.shalimov.movieland.dao;



import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.entity.MovieRequest;

import java.util.List;

public interface MovieDao {
    List<Movie> getAll(MovieRequest movieRequest);

    List<Movie> getRandomMovies();

    List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest);

    Movie getMovieById(int movieId);

    boolean editMovie(Movie movie);

    boolean addMovie(Movie movie);

    void deleteMovie(Integer movieId);

    List<Movie> getMoviesByMask(String mask);
}
