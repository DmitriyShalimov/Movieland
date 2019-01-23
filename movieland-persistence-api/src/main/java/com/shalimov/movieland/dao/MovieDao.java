package com.shalimov.movieland.dao;



import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.entity.MovieRequest;
import com.shalimov.movieland.entity.ReportRequest;

import java.util.List;

public interface MovieDao {
    List<Movie> getAll(MovieRequest movieRequest);

    List<Movie> getRandomMovies();

    List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest);

    Movie getMovieById(int movieId);

    void editMovie(Movie movie);

    int addMovie(Movie movie);

    void deleteMovie(List<Integer> movieId);

    List<Movie> getMoviesByMask(String mask, MovieRequest movieRequest);

    List<Movie> getMoviesForReport(ReportRequest reportRequest);
}
