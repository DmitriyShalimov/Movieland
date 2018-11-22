package com.shalimov.movieland.dao;


import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.entity.Movie;

import java.util.List;

public interface CountryDao {

    List<Country> getAll();

    List<Country> getCountryForMovie(int id);

    void addCountriesForMovie(List<Country> countries, int id);

    void removeAllCountriesForMovie(List<Integer> movies);

    void enrich(List<Movie> movies, List<Integer> movieIds);
}
