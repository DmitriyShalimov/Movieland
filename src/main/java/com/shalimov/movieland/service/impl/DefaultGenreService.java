package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.service.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultGenreService implements GenreService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final GenreDao genreDao;
    private List<Genre> allGenres;

    @Autowired
    public DefaultGenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> getAll() {
        return new ArrayList<>(allGenres);
    }

    @Scheduled(fixedDelay = 4 * 60 * 60 * 1000, initialDelay = 0)
    private void invalidate() {
        logger.info("Update genre cache");
        allGenres = genreDao.getAll();
    }


}
