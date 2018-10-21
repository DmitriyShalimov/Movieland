package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.entity.Currency;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.entity.SortType;
import com.shalimov.movieland.filter.MovieFilter;
import com.shalimov.movieland.service.MovieService;
import com.shalimov.movieland.dao.cache.CurrencyCacheDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;
    private final CurrencyCacheDao currencyCache;

    @Autowired
    public DefaultMovieService(MovieDao movieDao, CurrencyCacheDao currencyCache) {
        this.movieDao = movieDao;
        this.currencyCache = currencyCache;
    }

    @Override
    public List<Movie> getAll(MovieFilter movieFilter) {
        List<Movie> movies = movieDao.getAll();
        sortMovies(movies, movieFilter);
        return movies;
    }

    @Override
    public List<Movie> getRandomMovies(MovieFilter movieFilter) {
        List<Movie> movies = movieDao.getRandomMovies();
        sortMovies(movies, movieFilter);
        return movies;
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, MovieFilter movieFilter) {
        List<Movie> movies = movieDao.getMoviesByGenre(genreId);
        sortMovies(movies, movieFilter);
        return movies;
    }

    @Override
    public Movie getMovieById(int movieId, Currency currency) {
        double rate = 1;
        if (currency == Currency.USD) {
            rate = currencyCache.getRates().get("USD");
        } else if (currency == Currency.EUR) {
            rate = currencyCache.getRates().get("EUR");
        }
        System.out.println(rate);
        return movieDao.getMovieById(movieId);
    }

    private void sortMovies(List<Movie> movies, MovieFilter movieFilter) {
        if (SortType.DESC.equals(movieFilter.getRatingOrder())) {
            movies.sort((movie1, movie2) -> Double.compare(movie2.getRating(), movie1.getRating()));
        }
        if (SortType.DESC.equals(movieFilter.getPriceOrder())) {
            movies.sort((movie1, movie2) -> Double.compare(movie2.getPrice(), movie1.getPrice()));
        }
        if (SortType.ASC.equals(movieFilter.getPriceOrder())) {
            movies.sort(Comparator.comparingDouble(Movie::getPrice));
        }
    }
}
