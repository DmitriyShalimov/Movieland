package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.service.GenreService;

import java.util.List;

public class DefaultGenreService implements GenreService {
    private GenreDao genreDao;

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    public void setGenreDao(GenreDao genreDao) {
        this.genreDao = genreDao;
    }
}
