package com.shalimov.movieland.dao;


import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.entity.Movie;

import java.util.List;

public interface GenreDao {
    List<Genre> getAll();

}
