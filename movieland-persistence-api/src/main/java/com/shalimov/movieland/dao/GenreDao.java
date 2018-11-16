package com.shalimov.movieland.dao;



import com.shalimov.movieland.entity.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getAll();

    List<Genre> getGenreForMovie(int id);

    boolean addGenreForMovie(int genreId, int movieId);

    void removeAllGenresForMovie(int movieId);
}
