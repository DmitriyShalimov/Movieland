package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultCountryService implements CountryService {
    private final CountryDao jdbcCountryDao;
    private volatile List<Country> allCountries;

    @Autowired
    public DefaultCountryService(CountryDao jdbcCountryDao) {
        this.jdbcCountryDao = jdbcCountryDao;
    }

    @Override
    public List<Country> getAll() {
        return new ArrayList<>(allCountries);
    }

    @Override
    public void removeAllCountriesForMovie(List<Integer> movies) {
        jdbcCountryDao.removeAllCountriesForMovie(movies);
    }

    @Override
    public List<Country> getCountryForMovie(int movieId) {
        return jdbcCountryDao.getCountryForMovie(movieId);
    }

    @Override
    public void enrich(List<Movie> movies, List<Integer> movieIds) {
        jdbcCountryDao.enrich(movies, movieIds);
    }

    @Override
    public void addCountriesForMovie(List<Country> countries, int movieId) {
        jdbcCountryDao.addCountriesForMovie(countries, movieId);
    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${cache.update.time}", initialDelayString = "${cache.update.time}")
    private void invalidate() {

        allCountries = jdbcCountryDao.getAll();
    }
}
