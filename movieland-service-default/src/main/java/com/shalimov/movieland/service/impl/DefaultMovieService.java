package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.service.cache.CurrencyCacheService;
import com.shalimov.movieland.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;
    private final CurrencyCacheService currencyCache;
    private final CountryDao jdbcCountryDao;
    private final GenreDao jdbcGenreDao;
    private final List<Integer> moviesToDelete = new CopyOnWriteArrayList<>();

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
    public List<Movie> getRandomMovies() {
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

    @Transactional
    @Override
    public boolean editMovie(Movie movie, int[] genres, int[] countries) {
        jdbcGenreDao.removeAllGenresForMovie(movie.getId());
        jdbcCountryDao.removeAllCountriesForMovie(movie.getId());
        for (int genreId : genres) {
            if (!jdbcGenreDao.addGenreForMovie(genreId, movie.getId())) {
                return false;
            }
        }
        for (int countryId : countries) {
            if (!jdbcCountryDao.addCountryForMovie(countryId, movie.getId())) {
                return false;
            }
        }
        return movieDao.editMovie(movie);
    }

    @Override
    public boolean addMovie(Movie movie, int[] genres, int[] countries) {
        for (int genreId : genres) {
            if (!jdbcGenreDao.addGenreForMovie(genreId, movie.getId())) {
                return false;
            }
        }
        for (int countryId : countries) {
            if (!jdbcCountryDao.addCountryForMovie(countryId, movie.getId())) {
                return false;
            }
        }
        return movieDao.addMovie(movie);
    }

    @Override
    public void markMovieToDelete(int movieId) {
        moviesToDelete.add(movieId);
    }

    @Override
    public void unmarkMovieToDelete(int movieId) {
        moviesToDelete.removeIf(integer -> movieId == integer);
    }

    @Override
    public List<Movie> getMoviesByMask(String mask) {
        List<Movie> movies = movieDao.getMoviesByMask(mask);
        setGenresAndCountries(movies);
        return movies;
    }

    @Scheduled(cron = "59 59 23 * * ?")
    private void removeMovies() {
        for (Integer movieId : moviesToDelete) {
            movieDao.deleteMovie(movieId);
            jdbcGenreDao.removeAllGenresForMovie(movieId);
            jdbcCountryDao.removeAllCountriesForMovie(movieId);
        }
        moviesToDelete.clear();
    }
}
