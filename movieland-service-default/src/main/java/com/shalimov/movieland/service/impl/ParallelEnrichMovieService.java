package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.service.CountryService;
import com.shalimov.movieland.service.EnrichMovieService;
import com.shalimov.movieland.service.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ParallelEnrichMovieService implements EnrichMovieService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CountryService countryService;
    private final GenreService genreService;
    private final ExecutorService executorService;
    @Value("${executor.timeout}")
    private long timeout;

    @Autowired
    public ParallelEnrichMovieService(CountryService countryService, GenreService genreService, ExecutorService executorService) {
        this.countryService = countryService;
        this.genreService = genreService;
        this.executorService = executorService;
    }

    @Override
    public void removeGenresAndCountriesForMovie(List<Integer> movies) {
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(() -> genreService.removeAllGenresForMovie(movies));
        tasks.add(() -> countryService.removeAllCountriesForMovie(movies));
        try {
            executorService.invokeAll(tasks.stream().map(Executors::callable).collect(Collectors.toList()), timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addGenresAndCountries(Movie movie, int movieId) {
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(() -> genreService.addGenresForMovie(movie.getGenres(), movieId));
        tasks.add(() -> countryService.addCountriesForMovie(movie.getCountries(), movieId));
        try {
            executorService.invokeAll(tasks.stream().map(Executors::callable).collect(Collectors.toList()), timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enrich(Movie movie) {
        Future<List<Country>> countries = executorService.submit(() -> countryService.getCountryForMovie(movie.getId()));
        Future<List<Genre>> genres = executorService.submit(() -> genreService.getGenreForMovie(movie.getId()));
        try {
            movie.setCountries(countries.get(timeout, TimeUnit.SECONDS));
            movie.setGenres(genres.get(timeout, TimeUnit.SECONDS));
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            logger.warn("Enrichment was canceled by timeout for movie with id {}", movie.getId());
        }
    }

    @Override
    public void enrich(List<Movie> movies) {
        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : movies) {
            movieIds.add(movie.getId());
        }
        List<Runnable> tasks = new ArrayList<>();
        System.out.println(new ThreadLocal<>().get());
        tasks.add(() -> enrichCountry(movies, movieIds));
        tasks.add(() -> enrichGenre(movies, movieIds));
        try {
            executorService.invokeAll(tasks.stream().map(Executors::callable).collect(Collectors.toList()), timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enrichCountry(List<Movie> movies, List<Integer> movieIds) {
        if (Thread.currentThread().isInterrupted()) {
            logger.warn("Enrichment was canceled by timeout");
        } else {
            countryService.enrich(movies, movieIds);
        }
    }

    private void enrichGenre(List<Movie> movies, List<Integer> movieIds) {
        if (Thread.currentThread().isInterrupted()) {
            logger.warn("Enrichment was canceled by timeout");
        } else {
            genreService.enrich(movies, movieIds);
        }
    }
}

