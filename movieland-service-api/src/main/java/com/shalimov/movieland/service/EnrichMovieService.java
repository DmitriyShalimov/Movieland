package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.Movie;

import java.util.List;

public interface EnrichMovieService {
    void removeGenresAndCountriesForMovie(List<Integer> movies);

    void enrich(List<Movie> movies);

    void addGenresAndCountries(Movie movie, int movieId);

    void enrich(Movie movie);
}
