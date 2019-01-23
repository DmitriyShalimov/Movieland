package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.entity.Currency;
import com.shalimov.movieland.service.CountryService;
import com.shalimov.movieland.service.GenreService;
import com.shalimov.movieland.service.cache.CurrencyService;
import com.shalimov.movieland.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.*;

@Service
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;
    private final CurrencyService currencyService;
    private final List<Integer> moviesToDelete = new CopyOnWriteArrayList<>();
    private final Map<Integer, SoftReference<Movie>> movieCache = new ConcurrentHashMap<>();


    @Autowired
    public DefaultMovieService(MovieDao movieDao, CurrencyService currencyService) {
        this.movieDao = movieDao;
        this.currencyService = currencyService;
    }

    @Override
    public List<Movie> getAll(MovieRequest movieRequest) {
        return movieDao.getAll(movieRequest);
    }

    @Override
    public List<Movie> getRandomMovies() {
        return movieDao.getRandomMovies();
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest) {
        return movieDao.getMoviesByGenre(genreId, movieRequest);
    }

    @Override
    public Movie getMovieById(int movieId, Currency currency) {
        Movie movie;
        Movie copyMovie;
        SoftReference<Movie> movieSoftReference = movieCache.get(movieId);
        if (movieSoftReference != null) {
            movie = movieSoftReference.get();
            if (movie != null) {
                copyMovie = copy(movie);
                setRate(currency, movie);
                return copyMovie;
            }
        }
        movieCache.computeIfAbsent(movieId, id -> new SoftReference<>(getMovieFromDB(movieId)));
        movie = movieCache.get(movieId).get();
        if (movie == null) {
            movie = getMovieFromDB(movieId);
        }
        copyMovie = copy(movie);
        setRate(currency, copyMovie);
        return copyMovie;
    }

    private Movie copy(Movie movie) {
        Movie copyMovie = new Movie();
        copyMovie.setId(movie.getId());
        copyMovie.setNameRussian(movie.getNameRussian());
        copyMovie.setNameNative(movie.getNameNative());
        copyMovie.setDescription(movie.getDescription());
        List<Country> countries = movie.getCountries();
        copyMovie.setCountries(countries);
        copyMovie.setPicturePath(movie.getPicturePath());
        List<Genre> genres = movie.getGenres();
        copyMovie.setGenres(genres);
        copyMovie.setYearOfRelease(movie.getYearOfRelease());
        copyMovie.setPrice(movie.getPrice());
        copyMovie.setRating(movie.getRating());
        copyMovie.setAddDate(movie.getAddDate());
        copyMovie.setModifyDate(movie.getModifyDate());
        return copyMovie;
    }

    private void setRate(Currency currency, Movie copyMovie) {
        double rate = currencyService.getRate(currency);
        copyMovie.setPrice(copyMovie.getPrice() / rate);
    }

    @Transactional
    @Override
    public void editMovie(Movie movie) {
        movieDao.editMovie(movie);
        movieCache.put(movie.getId(), new SoftReference<>(movie));
    }

    @Transactional
    @Override
    public void addMovie(Movie movie) {
        int movieId = movieDao.addMovie(movie);
        movie.setId(movieId);
        movieCache.put(movieId, new SoftReference<>(movie));
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
    public List<Movie> getMoviesByMask(String mask, MovieRequest movieRequest) {
        return movieDao.getMoviesByMask(mask, movieRequest);
    }

    @Override
    public List<Movie> getMoviesForReport(ReportRequest reportRequest) {
        return movieDao.getMoviesForReport(reportRequest);
    }

    @Override
    @Scheduled(cron = "59 59 23 * * ?")
    @Transactional
    public void removeMovies() {
        movieDao.deleteMovie(moviesToDelete);
        moviesToDelete.clear();
    }

    private Movie getMovieFromDB(int movieId) {

        return movieDao.getMovieById(movieId);
    }
}
