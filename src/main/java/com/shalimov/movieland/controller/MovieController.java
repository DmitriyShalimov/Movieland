package com.shalimov.movieland.controller;

import com.shalimov.movieland.entity.Currency;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.entity.SortType;
import com.shalimov.movieland.filter.MovieFilter;
import com.shalimov.movieland.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(value = "/v1/movie")
    @ResponseBody
    public List<Movie> getAllMovies(@RequestParam(value = "rating", required = false) SortType ratingOrder, @RequestParam(value = "price", required = false) SortType priceOrder) {
        logger.info("Retrieving all movies");
        MovieFilter movieFilter = new MovieFilter();
        movieFilter.setPriceOrder(priceOrder);
        movieFilter.setRatingOrder(ratingOrder);
        List<Movie> movies = movieService.getAll(movieFilter);
        logger.info("Movies  are {}", movies);
        return movies;
    }

    @GetMapping(value = "/v1/movie/random")
    @ResponseBody
    public List<Movie> getRandomMovies(@RequestParam(value = "rating", required = false) SortType ratingOrder, @RequestParam(value = "price", required = false) SortType priceOrder) {
        logger.info("Retrieving Random movies");
        MovieFilter movieFilter = new MovieFilter();
        movieFilter.setPriceOrder(priceOrder);
        movieFilter.setRatingOrder(ratingOrder);
        List<Movie> movies = movieService.getRandomMovies(movieFilter);
        logger.info("Movies  are {}", movies);
        return movies;
    }

    @GetMapping(value = "/v1/movie/genre/{genreId}")
    @ResponseBody
    public List<Movie> getMoviesByGenre(@PathVariable int genreId, @RequestParam(value = "rating", required = false) SortType ratingOrder, @RequestParam(value = "price", required = false) SortType priceOrder) {
        logger.info("Retrieving all movies from genre with id {}", genreId);
        MovieFilter movieFilter = new MovieFilter();
        movieFilter.setPriceOrder(priceOrder);
        movieFilter.setRatingOrder(ratingOrder);
        List<Movie> movies = movieService.getMoviesByGenre(genreId, movieFilter);
        logger.info("Movies  are {}", movies);
        return movies;
    }
    @GetMapping(value = "/v1/movie/{movieId}")
    @ResponseBody
    public Movie getMovieById(@PathVariable int movieId,@RequestParam(value = "currency", required = false) Currency currency) {
        logger.info("Retrieving movie with id {}", movieId);
       if (currency==null){
           currency=Currency.USD;
       }
        Movie movie = movieService.getMovieById(movieId,currency);
        logger.info("Movie  is {}", movie);
        return movie;
    }

}
