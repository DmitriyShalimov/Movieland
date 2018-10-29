package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultGenreService implements GenreService {
    private final GenreDao cachedGenreDao;

    @Autowired
    public DefaultGenreService(GenreDao cachedGenreDao) {
        this.cachedGenreDao = cachedGenreDao;
    }

    @Override
    public List<Genre> getAll() {
        return cachedGenreDao.getAll();
    }


}
