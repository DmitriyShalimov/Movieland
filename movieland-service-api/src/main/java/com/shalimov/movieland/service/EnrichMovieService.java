package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.Movie;

import java.util.List;

public interface EnrichMovieService {
    void enrich(List<Movie> movies);

    void enrich(Movie movie);
}
