package com.shalimov.movieland.service;

import com.shalimov.movieland.entity.Report;
import com.shalimov.movieland.entity.ReportRequest;
import com.shalimov.movieland.entity.ReportStatus;

public interface ReportService {
    Report getReport(int reportId);

    void scheduleReport(ReportRequest reportRequest);

    boolean deleteReport(String reportId);

    ReportStatus getStatus(String reportId);
}
