package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.ReportDao;
import com.shalimov.movieland.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcReportDao implements ReportDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private String saveReportSQL = "INSERT INTO report (report) VALUES (:report);";
    private String getReportSQL = "SELECT report FROM report WHERE id=:reportId;";
    private String cleanReportsSQL = "DELETE FROM report;";
    private String deleteReportSql = "DELETE FROM report WHERE report_id =:ireportId;";
    private String getReportIdSQL = "SELECT id FROM report WHERE report_id=:reportId;";


    @Autowired
    public JdbcReportDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public boolean saveReport(Report report) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("report", report.getInputStream());
        return namedParameterJdbcTemplate.update(saveReportSQL, params)==1;
    }

    @Override
    public Report getReport(int reportId) {
        MapSqlParameterSource params = new MapSqlParameterSource("reportId", reportId);
        Report report = new Report();
        report.setBody(namedParameterJdbcTemplate.queryForObject(getReportSQL, params, byte[].class));
        return report;
    }

    @Override
    public boolean deleteReport(String reportId) {
        return namedParameterJdbcTemplate.update(deleteReportSql, new MapSqlParameterSource("reportId", reportId)) == 1;
    }

    @Override
    public boolean isPresentReport(String reportId) {
        MapSqlParameterSource params = new MapSqlParameterSource("reportId", reportId);
        return namedParameterJdbcTemplate.queryForObject(getReportIdSQL, params, Integer.class)==1;
    }

    @Override
    public void cleanReports() {
        namedParameterJdbcTemplate.update(cleanReportsSQL, new MapSqlParameterSource());
    }
}
