package com.shalimov.movieland.web.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.web.entity.MovieRequestDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonParser {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Movie parse(String movieRequest) {
        Movie movie = new Movie();
        try {
            MovieRequestDto dto = OBJECT_MAPPER.readValue(movieRequest, MovieRequestDto.class);
            movie.setNameRussian(dto.getNameRussian());
            movie.setNameNative(dto.getNameNative());
            movie.setDescription(dto.getDescription());
            movie.setPicturePath(dto.getPicturePath());
            movie.setYearOfRelease(dto.getYearOfRelease());
            movie.setPrice(dto.getPrice());
            movie.setRating(dto.getRating());
            List<Country> countries = new ArrayList<>();
            for (int id : dto.getCountries()) {
                Country country = new Country(id, "");
                countries.add(country);
            }
            movie.setCountries(countries);
            List<Genre> genres = new ArrayList<>();
            for (int id : dto.getGenres()) {
                Genre genre = new Genre(id, "");
                genres.add(genre);
            }
            movie.setGenres(genres);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while converting json", e);
        }
        return movie;
    }
}
