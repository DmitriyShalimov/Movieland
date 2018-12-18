package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.ReportDao;
import com.shalimov.movieland.entity.Report;
import com.shalimov.movieland.entity.ReportRequest;
import com.shalimov.movieland.entity.ReportStatus;
import com.shalimov.movieland.service.ReportService;
import com.shalimov.movieland.service.utils.EmailSender;
import com.shalimov.movieland.service.utils.ExcelReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DefaultReportService implements ReportService {

    private final ReportDao reportDao;
    private final ExcelReportGenerator excelReportGenerator;
    private final EmailSender emailSender;
    private ConcurrentLinkedQueue<ReportRequest> reports = new ConcurrentLinkedQueue<>();

    @Autowired
    public DefaultReportService(ReportDao reportDao, ExcelReportGenerator excelReportGenerator, EmailSender emailSender) {
        this.reportDao = reportDao;
        this.excelReportGenerator = excelReportGenerator;
        this.emailSender = emailSender;
    }

    @Override
    public void scheduleReport(ReportRequest reportRequest) {
        reports.offer(reportRequest);
    }

    @Override
    public boolean deleteReport(String reportId) {
        Optional<ReportRequest> reportRequest = reports.stream()
                .filter(tempReportRequest -> reportId.equals(tempReportRequest.getId()))
                .findFirst();
        if (reportRequest.isPresent()) {
            reportRequest.get().setReportStatus(ReportStatus.REMOVED);
            return true;
        } else {
            return reportDao.deleteReport(reportId);
        }
    }

    @Override
    public ReportStatus getStatus(String reportId) {
        Optional<ReportRequest> reportRequest = reports.stream()
                .filter(tempReportRequest -> reportId.equals(tempReportRequest.getId()))
                .findFirst();
        if (reportRequest.isPresent()) {
            return reportRequest.get().getReportStatus();
        } else {
            if (reportDao.isPresentReport(reportId)) {
                return ReportStatus.DONE;
            } else {
                return ReportStatus.REMOVED;
            }
        }
    }

    @Override
    public Report getReport(int reportId) {
        return reportDao.getReport(reportId);
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 0)
    private void processReports() {
        while (!reports.isEmpty()) {
            ReportRequest reportRequest = reports.poll();
            Report report = excelReportGenerator.createReport(reportRequest);
            if (reportRequest.getReportStatus().equals(ReportStatus.IN_PROGRESS)) {
                if (reportDao.saveReport(report)) {
                    reportRequest.setReportStatus(ReportStatus.DONE);
                }
                emailSender.sendEmail(reportRequest.getEmail(), reportRequest.getId(), reportRequest.getDocumentName());
            }
        }
    }

    @Scheduled(cron = "15 15 15 ? JUL MON")
    private void cleanReports() {
        reportDao.cleanReports();
    }
}
