package com.shalimov.movieland.controller;

import com.shalimov.movieland.entity.Genre;
import com.shalimov.movieland.service.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GenreController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }
    @GetMapping(value = "/v1/genre")
    @ResponseBody
    public List<Genre> getAllMovies() {
        logger.info("Retrieving all genres");
        List<Genre> genres = genreService.getAll();
        logger.info("Genres  are {}", genres);
        return genres;
    }
}

