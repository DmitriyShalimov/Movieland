package com.shalimov.movieland.web.controller;

import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.service.ReportService;
import com.shalimov.movieland.web.annotation.ProtectedBy;
import com.shalimov.movieland.web.entity.UserHandler;
import com.shalimov.movieland.web.util.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/report")
public class ReportController {

    private final ReportService reportService;

    private final JsonParser parser;

    @Autowired
    public ReportController(ReportService reportService, JsonParser parser) {
        this.reportService = reportService;
        this.parser = parser;
    }

    @PostMapping()
    @ProtectedBy(UserType.ADMIN)
    public ResponseEntity createReport(@RequestBody String json) {
        User user = UserHandler.getUser();
        ReportRequest reportRequest = parser.jsonToReportRequest(json);
        reportRequest.setEmail(user.getEmail());
        reportService.scheduleReport(reportRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{reportId}/**")
    @ProtectedBy(UserType.ADMIN)
    public void getReport(HttpServletResponse response, @PathVariable int reportId) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        Report report = reportService.getReport(reportId);
        response.getOutputStream().write(report.getBody());
    }

    @PutMapping(value = "/{reportId}")
    @ProtectedBy(UserType.ADMIN)
    public ReportStatus checkStatus(@PathVariable String reportId) {
        return reportService.getStatus(reportId);
    }

    @DeleteMapping(value = "/{reportId}")
    @ProtectedBy(UserType.ADMIN)
    public ResponseEntity deleteReport(@PathVariable String reportId) {
        if (reportService.deleteReport(reportId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
