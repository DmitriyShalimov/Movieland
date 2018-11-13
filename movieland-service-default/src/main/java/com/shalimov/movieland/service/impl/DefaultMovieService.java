package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.service.cache.CurrencyCacheService;
import com.shalimov.movieland.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;
    private final CurrencyCacheService currencyCache;
    private final CountryDao jdbcCountryDao;
    private final GenreDao jdbcGenreDao;

    @Autowired
    public DefaultMovieService(MovieDao movieDao, CurrencyCacheService currencyCache, CountryDao jdbcCountryDao, GenreDao jdbcGenreDao) {
        this.movieDao = movieDao;
        this.currencyCache = currencyCache;
        this.jdbcCountryDao = jdbcCountryDao;
        this.jdbcGenreDao = jdbcGenreDao;
    }

    @Override
    public List<Movie> getAll(MovieRequest movieRequest) {
        List<Movie> movies = movieDao.getAll(movieRequest);
        setGenresAndCountries(movies);
        return movies;
    }

    @Override
    public List<Movie> getRandomMovies(MovieRequest movieRequest) {
        List<Movie> movies = movieDao.getRandomMovies();
        setGenresAndCountries(movies);
        return movies;

    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest) {
        List<Movie> movies = movieDao.getMoviesByGenre(genreId, movieRequest);
        setGenresAndCountries(movies);
        return movies;
    }

    private void setGenresAndCountries(List<Movie> movies) {
        for (Movie movie : movies) {
            List<Country> countries = jdbcCountryDao.getCountryForMovie(movie.getId());
            movie.setCountries(countries);
            List<Genre> genres = jdbcGenreDao.getGenreForMovie(movie.getId());
            movie.setGenres(genres);
        }
    }

    @Override
    public Movie getMovieById(int movieId, Currency currency) {
        double rate = 1;
        if (currency == Currency.USD) {
            rate = currencyCache.getRates().get("\"USD\"");
        } else if (currency == Currency.EUR) {
            rate = currencyCache.getRates().get("\"EUR\"");
        }
        Movie movie = movieDao.getMovieById(movieId);
        movie.setPrice(movie.getPrice() / rate);
        List<Country> countries = jdbcCountryDao.getCountryForMovie(movie.getId());
        movie.setCountries(countries);
        List<Genre> genres = jdbcGenreDao.getGenreForMovie(movie.getId());
        movie.setGenres(genres);
        return movie;
    }

    @Override
    public boolean editMovie(Movie movie) {
        return movieDao.editMovie(movie);
    }

    @Override
    public boolean addMovie(Movie movie) {
        return movieDao.addMovie(movie);
    }
}
