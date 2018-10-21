package com.shalimov.movieland.dao.cache;

import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

public class CachedGenreDao implements GenreDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private GenreDao genreDao;
    private List<Genre> allGenres;

    @Override
    public List<Genre> getAll() {
        return new ArrayList<>(allGenres);
    }

    @Scheduled(fixedDelay = 4 * 60 * 60 * 1000, initialDelay = 0)
    private void invalidate() {
        logger.info("Update genre cache");
        allGenres = genreDao.getAll();
    }

    public void setGenreDao(GenreDao genreDao) {
        this.genreDao = genreDao;
    }
}

