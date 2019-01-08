package com.shalimov.movieland.web.controller;

import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.service.MovieService;
import com.shalimov.movieland.web.annotation.ProtectedBy;
import com.shalimov.movieland.web.util.JsonParser;
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
    private final JsonParser jsonParser;

    @Autowired
    public MovieController(MovieService movieService, JsonParser jsonParser) {
        this.movieService = movieService;
        this.jsonParser = jsonParser;
    }

    @GetMapping
    public List<Movie> getAllMovies(@RequestParam(value = "rating", required = false) SortType ratingOrder,
                                    @RequestParam(value = "price", required = false) SortType priceOrder,
                                    @RequestParam(value = "page", defaultValue = "1") int page) {
        logger.info("Retrieving all movies");
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceOrder(priceOrder);
        movieRequest.setRatingOrder(ratingOrder);
        movieRequest.setPage(page);
        List<Movie> movies = movieService.getAll(movieRequest);
        logger.info("Movies  are {}", movies);
        return movies;
    }

    @GetMapping(value = "/random")
    public List<Movie> getRandomMovies() {
        logger.info("Retrieving Random movies");
        List<Movie> movies = movieService.getRandomMovies();
        logger.info("Movies  are {}", movies);
        return movies;
    }

    @GetMapping(value = "/genre/{genreId}")
    public List<Movie> getMoviesByGenre(@PathVariable int genreId, @RequestParam(value = "rating", required = false) SortType ratingOrder,
                                        @RequestParam(value = "price", required = false) SortType priceOrder,
                                        @RequestParam(value = "page", defaultValue = "1") int page) {
        logger.info("Retrieving all movies from genre with id {}", genreId);
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceOrder(priceOrder);
        movieRequest.setRatingOrder(ratingOrder);
        movieRequest.setPage(page);
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

    @ProtectedBy(UserType.ADMIN)
    @PostMapping
    public void addMovie(@RequestBody String movieRequest) {
        Movie movie = jsonParser.parse(movieRequest);
        movieService.addMovie(movie);
    }

    @ProtectedBy(UserType.ADMIN)
    @PutMapping(path = "/{movieId}")
    public void editMovie(@RequestBody String movieRequest, @PathVariable int movieId) {
        Movie movie = jsonParser.parse(movieRequest);
        movie.setId(movieId);
        movieService.editMovie(movie);
    }

    @ProtectedBy(UserType.ADMIN)
    @DeleteMapping(value = "/{movieId}")
    public void markMovieToDelete(@PathVariable int movieId) {
        logger.info("Mark to delete movie with id {}", movieId);
        movieService.markMovieToDelete(movieId);
        logger.info("Movie  is marked");
    }

    @ProtectedBy(UserType.ADMIN)
    @PostMapping(value = "/{movieId}/unmark")
    public void unmarkMovieToDelete(@PathVariable int movieId) {
        logger.info("Unmark to delete movie with id {}", movieId);
        movieService.unmarkMovieToDelete(movieId);
        logger.info("Movie  is unmarked");
    }

    @GetMapping(value = "/search")
    public List<Movie> getMoviesByMask(@RequestParam String mask, @RequestParam(value = "page", defaultValue = "1") int page) {
        logger.info("Retrieving movies by mask");
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPage(page);
        List<Movie> movies = movieService.getMoviesByMask(mask, movieRequest);
        logger.info("Movies  are {}", movies);
        return movies;
    }
}
