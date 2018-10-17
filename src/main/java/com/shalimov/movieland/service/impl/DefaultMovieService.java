package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.entity.Genre;
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
    private final CountryDao countryDao;
    private final GenreDao genreDao;

    @Autowired
    public DefaultMovieService(MovieDao movieDao, CountryDao countryDao, GenreDao genreDao) {
        this.movieDao = movieDao;
        this.countryDao = countryDao;
        this.genreDao = genreDao;
    }

    @Override
    public List<Movie> getAll(MovieFilter movieFilter) {
        List<Movie> movies = movieDao.getAll();
        updateMovies(movies,movieFilter);

        return movies;
    }

    @Override
    public List<Movie> getRandomMovies(MovieFilter movieFilter) {
        List<Movie> movies = movieDao.getRandomMovies();
        updateMovies(movies,movieFilter);
        return movies;
    }

    @Override
    public List<Movie> getMoviesByGenre(int id, MovieFilter movieFilter) {
        List<Movie> movies = movieDao.getMoviesByGenre(id);
        updateMovies(movies, movieFilter);
        return movies;
    }

    private void updateMovies(List<Movie> movies,MovieFilter movieFilter) {
        for (Movie movie : movies) {
            List<Country> countries = countryDao.getCountryForMovie(movie.getId());
            movie.setCountries(countries);
            List<Genre> genres = genreDao.getGenreForMovie(movie.getId());
            movie.setGenres(genres);
        }
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
