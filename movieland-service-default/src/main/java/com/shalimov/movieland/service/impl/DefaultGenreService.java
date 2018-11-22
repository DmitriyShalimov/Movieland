package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.entity.Movie;
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


    @Override
    public void removeAllGenresForMovie(List<Integer> movieId) {
        jdbcGenreDao.removeAllGenresForMovie(movieId);
    }

    @Override
    public List<Genre> getGenreForMovie(int movieId) {
        return jdbcGenreDao.getGenreForMovie(movieId);
    }

    @Override
    public void enrich(List<Movie> movies, List<Integer> movieIds) {
        jdbcGenreDao.enrich(movies, movieIds);
    }

    @Override
    public void addGenresForMovie(List<Genre> genres, int movieId) {
        jdbcGenreDao.addGenresForMovie(genres, movieId);
    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${cache.update.time}", initialDelayString = "${cache.update.time}")
    private void invalidate() {
        allGenres = jdbcGenreDao.getAll();
    }

}
