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
    public void enrich(Movie movie) {
        Future<List<Country>> countries = executorService.submit(() -> countryService.getCountryForMovie(movie.getId()));
        Future<List<Genre>> genres = executorService.submit(() -> genreService.getGenreForMovie(movie.getId()));
        try {
            movie.setCountries(countries.get(timeout, TimeUnit.SECONDS));
            movie.setGenres(genres.get(timeout, TimeUnit.SECONDS));
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            System.out.println(e.getMessage());
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
        tasks.add(() ->  genreService.enrich(movies, movieIds));
        tasks.add(() -> countryService.enrich(movies, movieIds));
        try {
            System.out.println("try");
            executorService.invokeAll(tasks.stream().map(Executors::callable).collect(Collectors.toList()), timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            logger.warn("Will never happen");
        }
    }
}

