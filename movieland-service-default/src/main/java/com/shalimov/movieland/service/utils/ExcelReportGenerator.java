package com.shalimov.movieland.service.utils;

import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.service.MovieService;
import com.shalimov.movieland.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelReportGenerator {

    private final MovieService movieService;
    private final UserService userService;

    public ExcelReportGenerator(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }


    public Report createReport(ReportRequest reportRequest) {
        Workbook book = new HSSFWorkbook();
        ByteArrayOutputStream bookOutputStream = new ByteArrayOutputStream();
        Sheet sheet = book.createSheet(reportRequest.getDocumentName());
        int rowNum = 1;
        if (reportRequest.getType().equals(ReportRequestType.TOP_ACTIVE_USERS)) {
            setSheetHeader(sheet, reportRequest);
            List<User> users = userService.getTopUsers();
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                Cell userId = row.createCell(0);
                userId.setCellValue(user.getId());
                Cell email = row.createCell(1);
                email.setCellValue(user.getEmail());
                Cell reviewsCount = row.createCell(2);
                reviewsCount.setCellValue(user.getReviewCount());
                Cell averageRate = row.createCell(3);
                averageRate.setCellValue(user.getAverageRating());
            }
        } else {
            setSheetHeader(sheet, reportRequest);
            List<Movie> movies = movieService.getMoviesForReport(reportRequest);

            for (Movie movie : movies) {
                Row row = sheet.createRow(rowNum++);
                Cell id = row.createCell(0);
                id.setCellValue(movie.getId());
                Cell nameRussian = row.createCell(1);
                nameRussian.setCellValue(movie.getNameRussian());
                Cell nameNative = row.createCell(2);
                nameNative.setCellValue(movie.getNameNative());
                Cell description = row.createCell(3);
                description.setCellValue(movie.getDescription());
                Cell price = row.createCell(4);
                price.setCellValue(movie.getPrice());
                Cell genres = row.createCell(5);
                genres.setCellValue(String.valueOf(movie.getGenres()));
                Cell addDate = row.createCell(6);
                addDate.setCellValue(String.valueOf(movie.getAddDate()));
                Cell modifiedDate = row.createCell(7);
                modifiedDate.setCellValue(String.valueOf(movie.getModifyDate()));
                Cell rating = row.createCell(8);
                rating.setCellValue(movie.getRating());
                Cell reviewsCount = row.createCell(9);
                reviewsCount.setCellValue(movie.getReviewsCount());
            }
        }
        try {
            book.write(bookOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Report report = new Report();
        report.setBody(bookOutputStream.toByteArray());
        return report;
    }

    private void setSheetHeader(Sheet sheet, ReportRequest reportRequest) {
        Row row = sheet.createRow(0);
        if (reportRequest.getType().equals(ReportRequestType.TOP_ACTIVE_USERS)) {
            String[] headers = {"User id", "Email", "Reviews count", "Average rate"};
            for (int col = 0; col < 4; col++) {
                Cell cell = row.createCell(col);
                cell.setCellValue(headers[col]);
            }
        } else {
            String[] headers = {"Movie id", "NameRussian", "NameNative", "Description", "Price", "Genres",
                    "Add date", "Modified date", "Rating", "Reviews count"};
            for (int col = 0; col < 10; col++) {
                Cell cell = row.createCell(col);
                cell.setCellValue(headers[col]);
            }
        }
    }

}

