package com.shalimov.movieland.web.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.web.entity.MovieRequestDto;
import com.shalimov.movieland.web.entity.ReportRequestDto;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
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

    public ReportRequest jsonToReportRequest(String json) {
        ReportRequest reportRequest=new ReportRequest();
        try {
            ReportRequestDto reportRequestDto=OBJECT_MAPPER.readValue(json, ReportRequestDto.class);
            reportRequest.setStartDate(LocalDateTime.parse(reportRequestDto.getStartDate()));
            reportRequest.setEndDate(LocalDateTime.parse(reportRequestDto.getEndDate()));
            reportRequest.setType( ReportRequestType.getReportRequestTypeById(reportRequestDto.getType()));
            reportRequest.setDocumentName(reportRequestDto.getDocumentName());
            reportRequest.setId(UUID.randomUUID().toString());
            reportRequest.setReportStatus(ReportStatus.IN_PROGRESS);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while converting json", e);
        }
        return reportRequest;
    }
}
