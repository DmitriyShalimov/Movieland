package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.entity.Currency;
import com.shalimov.movieland.service.EnrichMovieService;
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
    private final EnrichMovieService parallelEnrichMovieService;

    @Autowired
    public DefaultMovieService(MovieDao movieDao, CurrencyService currencyService, EnrichMovieService parallelEnrichMovieService) {
        this.movieDao = movieDao;
        this.currencyService = currencyService;
        this.parallelEnrichMovieService = parallelEnrichMovieService;
    }

    @Override
    public List<Movie> getAll(MovieRequest movieRequest) {
        List<Movie> movies = movieDao.getAll(movieRequest);
        parallelEnrichMovieService.enrich(movies);
        return movies;
    }

    @Override
    public List<Movie> getRandomMovies() {
        List<Movie> movies = movieDao.getRandomMovies();
        parallelEnrichMovieService.enrich(movies);
        return movies;
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest) {
        List<Movie> movies = movieDao.getMoviesByGenre(genreId, movieRequest);
        parallelEnrichMovieService.enrich(movies);
        return movies;
    }

    @Override
    public Movie getMovieById(int movieId, Currency currency) {
        Movie movie;
        SoftReference<Movie> movieSoftReference = movieCache.get(movieId);
        if (movieSoftReference != null) {
            movie = movieSoftReference.get();
            if (movie != null) {
                return getsCopyMovie(currency, movie);
            }
        }
        movieCache.putIfAbsent(movieId, new SoftReference<>(getMovieFromDB(movieId)));
        return getsCopyMovie(currency,movieCache.get(movieId).get());
    }

    private Movie getsCopyMovie(Currency currency, Movie movie) {
        Movie copyMovie = new Movie();
        movie.setId(movie.getId());
        copyMovie.setNameRussian(movie.getNameRussian());
        copyMovie.setNameNative(movie.getNameNative());
        copyMovie.setDescription(movie.getDescription());
        List<Country> countries = movie.getCountries();
        movie.setCountries(countries);
        copyMovie.setPicturePath(movie.getPicturePath());
        List<Genre> genres = movie.getGenres();
        movie.setGenres(genres);
        copyMovie.setYearOfRelease(movie.getYearOfRelease());
        copyMovie.setPrice(movie.getPrice());
        copyMovie.setRating(movie.getRating());
        copyMovie.setAddDate(movie.getAddDate());
        copyMovie.setModifyDate(movie.getModifyDate());
        double rate = currencyService.getRate(currency);
        copyMovie.setPrice(copyMovie.getPrice() / rate);
        return copyMovie;
    }

    @Transactional
    @Override
    public void editMovie(Movie movie) {
        parallelEnrichMovieService.removeGenresAndCountriesForMovie(Collections.singletonList(movie.getId()));
        parallelEnrichMovieService.addGenresAndCountries(movie, movie.getId());
        movieDao.editMovie(movie);
        movieCache.put(movie.getId(), new SoftReference<>(movie));
    }

    @Transactional
    @Override
    public void addMovie(Movie movie) {
        int movieId = movieDao.addMovie(movie);
        parallelEnrichMovieService.addGenresAndCountries(movie, movieId);
        movieCache.put(movie.getId(), new SoftReference<>(movie));
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
        List<Movie> movies = movieDao.getMoviesByMask(mask, movieRequest);
        parallelEnrichMovieService.enrich(movies);
        return movies;
    }

    @Override
    public List<Movie> getMoviesForReport(ReportRequest reportRequest) {
        return movieDao.getMoviesForReport(reportRequest);
    }

    @Scheduled(cron = "59 59 23 * * ?")
    @Transactional
    void removeMovies() {
        movieDao.deleteMovie(moviesToDelete);
        parallelEnrichMovieService.removeGenresAndCountriesForMovie(moviesToDelete);
        moviesToDelete.clear();
    }

    private Movie getMovieFromDB(int movieId) {
        Movie movie;
        movie = movieDao.getMovieById(movieId);
        parallelEnrichMovieService.enrich(movie);
        return movie;
    }
}
