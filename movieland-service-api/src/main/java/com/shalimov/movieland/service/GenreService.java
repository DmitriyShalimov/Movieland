package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.entity.Movie;

import java.util.List;

public interface GenreService {
    List<Genre> getAll();

    void removeAllGenresForMovie(List<Integer> movieId);

    List<Genre> getGenreForMovie(int movieId);

    void enrich(List<Movie> movies, List<Integer> movieIds);

    void addGenresForMovie(List<Genre> genres, int movieId);
}
