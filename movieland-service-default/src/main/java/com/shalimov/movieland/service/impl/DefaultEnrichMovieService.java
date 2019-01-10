package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.service.CountryService;
import com.shalimov.movieland.service.EnrichMovieService;
import com.shalimov.movieland.service.GenreService;


import java.util.ArrayList;
import java.util.List;


public class DefaultEnrichMovieService implements EnrichMovieService {

    private final CountryService countryService;
    private final GenreService genreService;

    public DefaultEnrichMovieService(CountryService countryService, GenreService genreService) {
        this.countryService = countryService;
        this.genreService = genreService;
    }


    @Override
    public void enrich(List<Movie> movies) {
        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : movies) {
            movieIds.add(movie.getId());
        }
        countryService.enrich(movies, movieIds);
        genreService.enrich(movies, movieIds);
    }

    @Override
    public void enrich(Movie movie) {
        List<Country> countries = countryService.getCountryForMovie(movie.getId());
        movie.setCountries(countries);
        List<Genre> genres = genreService.getGenreForMovie(movie.getId());
        movie.setGenres(genres);
    }
}
