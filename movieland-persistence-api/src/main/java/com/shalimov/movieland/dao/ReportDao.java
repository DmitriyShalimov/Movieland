package com.shalimov.movieland.dao;

import com.shalimov.movieland.entity.Report;

public interface ReportDao {
    void cleanReports();

    boolean saveReport(Report report);

    Report getReport(int reportId);

    boolean deleteReport(String reportId);

    boolean isPresentReport(String reportId);
}
