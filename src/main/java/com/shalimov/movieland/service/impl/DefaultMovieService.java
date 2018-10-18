package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.entity.SortType;
import com.shalimov.movieland.filter.MovieFilter;
import com.shalimov.movieland.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;

    @Autowired
    public DefaultMovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    @Override
    public List<Movie> getAll(MovieFilter movieFilter) {
        List<Movie> movies = movieDao.getAll();
        sortMovies(movies, movieFilter);
        return movies;
    }

    @Override
    public List<Movie> getRandomMovies(MovieFilter movieFilter) {
        List<Movie> movies = movieDao.getRandomMovies();
        sortMovies(movies, movieFilter);
        return movies;
    }

    @Override
    public List<Movie> getMoviesByGenre(int id, MovieFilter movieFilter) {
        List<Movie> movies = movieDao.getMoviesByGenre(id);
        sortMovies(movies, movieFilter);
        return movies;
    }

    private void sortMovies(List<Movie> movies, MovieFilter movieFilter) {
        if (SortType.DESC.equals(movieFilter.getRating())) {
            movies.sort((movie1, movie2) -> Double.compare(movie2.getRating(), movie1.getRating()));
        }
        if (SortType.DESC.equals(movieFilter.getPrice())) {
            movies.sort((movie1, movie2) -> Double.compare(movie2.getPrice(), movie1.getPrice()));
        }
        if (SortType.ASC.equals(movieFilter.getPrice())) {
            movies.sort(Comparator.comparingDouble(Movie::getPrice));
        }
    }
}
