package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.service.CountryService;
import com.shalimov.movieland.service.GenreService;
import com.shalimov.movieland.service.cache.CurrencyService;
import com.shalimov.movieland.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;
    private final CurrencyService currencyService;
    private final CountryService countryService;
    private final GenreService genreService;
    private final List<Integer> moviesToDelete = new CopyOnWriteArrayList<>();

    @Autowired
    public DefaultMovieService(MovieDao movieDao, CurrencyService currencyService, CountryService countryService, GenreService genreService) {
        this.movieDao = movieDao;
        this.currencyService = currencyService;
        this.countryService = countryService;

        this.genreService = genreService;
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
        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : movies) {
            movieIds.add(movie.getId());
            movie.setCountries(new ArrayList<>());
            movie.setGenres(new ArrayList<>());
        }
        countryService.enrich(movies, movieIds);
        genreService.enrich(movies, movieIds);
    }

    @Override
    public Movie getMovieById(int movieId, Currency currency) {
        double rate = 1;
        if (currency == Currency.USD) {
            rate = currencyService.getRate("USD");
        } else if (currency == Currency.EUR) {
            rate = currencyService.getRate("EUR");
        }
        Movie movie = movieDao.getMovieById(movieId);
        movie.setPrice(movie.getPrice() / rate);
        List<Country> countries = countryService.getCountryForMovie(movie.getId());
        movie.setCountries(countries);
        List<Genre> genres = genreService.getGenreForMovie(movie.getId());
        movie.setGenres(genres);
        return movie;
    }

    @Transactional
    @Override
    public void editMovie(Movie movie) {
        genreService.removeAllGenresForMovie(Collections.singletonList(movie.getId()));
        countryService.removeAllCountriesForMovie(Collections.singletonList(movie.getId()));
        genreService.addGenresForMovie(movie.getGenres(), movie.getId());
        countryService.addCountriesForMovie(movie.getCountries(), movie.getId());
        movieDao.editMovie(movie);

    }

    @Transactional
    @Override
    public void addMovie(Movie movie) {
        int movieId = movieDao.addMovie(movie);
        genreService.addGenresForMovie(movie.getGenres(), movieId);
        countryService.addCountriesForMovie(movie.getCountries(), movieId);
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
        setGenresAndCountries(movies);
        return movies;
    }

    @Scheduled(cron = "59 59 23 * * ?")
    @Transactional
    void removeMovies() {
        movieDao.deleteMovie(moviesToDelete);
        genreService.removeAllGenresForMovie(moviesToDelete);
        countryService.removeAllCountriesForMovie(moviesToDelete);
        moviesToDelete.clear();
    }
}
