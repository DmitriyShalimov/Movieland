package com.shalimov.movieland.dao.cache;

import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class CachedGenreDao implements GenreDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final GenreDao jdbcGenreDao;
    private volatile List<Genre> allGenres;

    @Autowired
    public CachedGenreDao(GenreDao jdbcGenreDao) {
        this.jdbcGenreDao = jdbcGenreDao;
    }

    @Override
    public List<Genre> getAll() {
        return new ArrayList<>(allGenres);
    }

    @Override
    public List<Genre> getGenreForMovie(int id) {
        return null;
    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${cache.update.time}", initialDelayString = "${cache.update.time}")
    private void invalidate() {
        logger.info("Update genre cache");
        allGenres = jdbcGenreDao.getAll();
    }


}

