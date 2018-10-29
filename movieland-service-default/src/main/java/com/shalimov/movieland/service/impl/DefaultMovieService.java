package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.dao.cache.CurrencyCacheDao;
import com.shalimov.movieland.entity.Currency;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.entity.MovieRequest;
import com.shalimov.movieland.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;
     private final CurrencyCacheDao currencyCache;

    @Autowired
    public DefaultMovieService(MovieDao movieDao,CurrencyCacheDao currencyCache) {
        this.movieDao = movieDao;
        this.currencyCache = currencyCache;
    }

    @Override
    public List<Movie> getAll(MovieRequest movieRequest) {
        return movieDao.getAll(movieRequest);
    }

    @Override
    public List<Movie> getRandomMovies(MovieRequest movieRequest) {
        return movieDao.getRandomMovies();

    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest) {
        return movieDao.getMoviesByGenre(genreId, movieRequest);
    }

    @Override
    public Movie getMovieById(int movieId, Currency currency) {
        double rate = 1;
        if (currency == Currency.USD) {
            rate = currencyCache.getRates().get("USD");
        } else if (currency == Currency.EUR) {
            rate = currencyCache.getRates().get("EUR");
        }
        Movie movie=movieDao.getMovieById(movieId);
        movie.setPrice(movie.getPrice()/rate);
        return movie;
    }
}
