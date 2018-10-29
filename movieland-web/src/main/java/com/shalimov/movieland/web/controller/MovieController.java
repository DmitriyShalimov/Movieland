package com.shalimov.movieland.web.controller;

import com.shalimov.movieland.entity.Currency;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.entity.MovieRequest;
import com.shalimov.movieland.entity.SortType;
import com.shalimov.movieland.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/movie")
public class MovieController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(value = "")
    public List<Movie> getAllMovies(@RequestParam(value = "rating", required = false) SortType ratingOrder, @RequestParam(value = "price", required = false) SortType priceOrder) {
        logger.info("Retrieving all movies");
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceOrder(priceOrder);
        movieRequest.setRatingOrder(ratingOrder);
        List<Movie> movies = movieService.getAll(movieRequest);
        logger.info("Movies  are {}", movies);
        return movies;
    }

    @GetMapping(value = "/random")
    public List<Movie> getRandomMovies(@RequestParam(value = "rating", required = false) SortType ratingOrder, @RequestParam(value = "price", required = false) SortType priceOrder) {
        logger.info("Retrieving Random movies");
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceOrder(priceOrder);
        movieRequest.setRatingOrder(ratingOrder);
        List<Movie> movies = movieService.getRandomMovies(movieRequest);
        logger.info("Movies  are {}", movies);
        return movies;
    }

    @GetMapping(value = "/genre/{genreId}")
    public List<Movie> getMoviesByGenre(@PathVariable int genreId, @RequestParam(value = "rating", required = false) SortType ratingOrder, @RequestParam(value = "price", required = false) SortType priceOrder) {
        logger.info("Retrieving all movies from genre with id {}", genreId);
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceOrder(priceOrder);
        movieRequest.setRatingOrder(ratingOrder);
        List<Movie> movies = movieService.getMoviesByGenre(genreId, movieRequest);
        logger.info("Movies  are {}", movies);
        return movies;
    }

    @GetMapping(value = "/{movieId}")
    public Movie getMovieById(@PathVariable int movieId, @RequestParam(value = "currency", defaultValue = "USD") Currency currency) {
        logger.info("Retrieving movie with id {}", movieId);
        Movie movie = movieService.getMovieById(movieId, currency);
        logger.info("Movie  is {}", movie);
        return movie;
    }

}
