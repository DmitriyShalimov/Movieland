package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.entity.Movie;

import java.util.List;

public interface CountryService {
    List<Country> getAll();

    void removeAllCountriesForMovie(List<Integer> movies);

    List<Country> getCountryForMovie(int movieId);

    boolean enrich(List<Movie> movies, List<Integer> movieIds);

    void addCountriesForMovie(List<Country> countries, int movieId);
}
