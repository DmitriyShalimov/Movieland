package com.shalimov.movieland.dao;



import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.entity.Movie;

import java.util.List;

public interface GenreDao {
    List<Genre> getAll();

    List<Genre> getGenreForMovie(int id);

    void removeAllGenresForMovie(List<Integer>  movieIds);

    void enrich(List<Movie> movies, List<Integer> movieIds);

    void addGenresForMovie(List<Genre> genres, int movieId);
}
