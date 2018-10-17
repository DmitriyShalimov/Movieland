package com.shalimov.movieland.controller;

import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<Movie> getAllMovies() {
        logger.info("Retrieving all movies");
        List<Movie> movies = movieService.getAll();
        logger.info("Movies  are {}", movies);
        return movies;
    }

    @GetMapping(value = "/v1/movie/random")
    @ResponseBody
    public List<Movie> getRandomMovies() {
        logger.info("Retrieving Random movies");
        List<Movie> movies = movieService.getRandomMovies();
        logger.info("Movies  are {}", movies);
        return movies;
    }
}
