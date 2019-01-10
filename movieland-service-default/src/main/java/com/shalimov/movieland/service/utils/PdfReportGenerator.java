package com.shalimov.movieland.service.utils;

import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.service.MovieService;
import com.shalimov.movieland.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfReportGenerator {
    private final MovieService movieService;
    private final UserService userService;

    public PdfReportGenerator(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    public Report createReport(ReportRequest reportRequest) {
                List<Movie> movies = movieService.getMoviesForReport(reportRequest);
        Report report = new Report();
        return report;
    }
}
