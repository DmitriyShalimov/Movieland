package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DefaultMovieService implements MovieService {
    private  final MovieDao movieDao;
    private final CountryDao countryDao;
    private final GenreDao genreDao;

    @Autowired
    public DefaultMovieService(MovieDao movieDao, CountryDao countryDao, GenreDao genreDao) {
        this.movieDao = movieDao;
        this.countryDao = countryDao;
        this.genreDao = genreDao;
    }

    @Override
    public List<Movie> getAll() {
        List<Movie> movies = movieDao.getAll();
        updateMovies(movies);
        return movies;
    }

    @Override
    public List<Movie> getRandomMovies() {
        List<Movie> movies = movieDao.getRandomMovies();
        updateMovies(movies);
        return movies;
    }

    private void updateMovies(List<Movie> movies) {
        for (Movie movie : movies) {
            List<Country> countries = countryDao.getCountryForMovie(movie.getId());
            movie.setCountries(countries);
            List<Genre> genres = genreDao.getGenreForMovie(movie.getId());
            movie.setGenres(genres);
        }
    }


}
