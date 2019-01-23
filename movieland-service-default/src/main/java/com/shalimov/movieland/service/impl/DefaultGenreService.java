package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultGenreService implements GenreService {
    private final GenreDao jdbcGenreDao;
    private volatile List<Genre> allGenres;

    @Autowired
    public DefaultGenreService(GenreDao jdbcGenreDao) {
        this.jdbcGenreDao = jdbcGenreDao;
    }

    @Override
    public List<Genre> getAll() {
        return new ArrayList<>(allGenres);
    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${cache.update.time}", initialDelayString = "${cache.update.time}")
    private void invalidate() {
        allGenres = jdbcGenreDao.getAll();
    }

}
