package com.shalimov.movieland.web.controller;

import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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

    @PostMapping(path = "")
    public ResponseEntity addMovie(@RequestParam String nameRussian, @RequestParam String nameNative, @RequestParam int yearOfRelease,
                                   @RequestParam String description, @RequestParam String picturePath,
                                   @RequestParam double rating, @RequestParam double price, HttpSession session) {
        if (((User) session.getAttribute("loggedUser")).getUserType().equals(UserType.ADMIN)) {
            Movie movie = new Movie(nameRussian, nameNative, yearOfRelease, description, price, rating, picturePath);
            if (movieService.addMovie(movie))
                return ResponseEntity.ok().build();
            else
                return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/{movieId}")
    public ResponseEntity editMovie(@PathVariable int movieId, @RequestParam String nameRussian, @RequestParam String nameNative, @RequestParam int yearOfRelease,
                                    @RequestParam String description, @RequestParam String picturePath,
                                    @RequestParam double rating, @RequestParam double price, HttpSession session) {
        if (((User) session.getAttribute("loggedUser")).getUserType().equals(UserType.ADMIN)) {
            Movie movie = new Movie(nameRussian, nameNative, yearOfRelease, description, price, rating, picturePath);
            movie.setId(movieId);
            if (movieService.editMovie(movie))
                return ResponseEntity.ok().build();
            else
                return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
