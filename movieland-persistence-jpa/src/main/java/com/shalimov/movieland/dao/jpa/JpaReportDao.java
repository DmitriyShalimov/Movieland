package com.shalimov.movieland.dao.jpa;

import com.shalimov.movieland.dao.ReportDao;
import com.shalimov.movieland.entity.Report;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class JpaReportDao implements ReportDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public JpaReportDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void cleanReports() {
        Query<Report> reportQuery = getCurrentSession().createQuery("select r from report r", Report.class);
        List<Report> reports = reportQuery.getResultList();
        for (Report report : reports) {
            getCurrentSession().remove(report);
        }
    }

    @Override
    public boolean saveReport(Report report) {
        getCurrentSession().persist(report);
        return getCurrentSession().contains(report);
    }

    @Override
    public Report getReport(int reportId) {
        return getCurrentSession().find(Report.class, reportId);
    }

    @Override
    public boolean deleteReport(String reportId) {
        Report report = getCurrentSession().find(Report.class, reportId);
        getCurrentSession().remove(report);
        return !getCurrentSession().contains(report);
    }

    @Override
    public boolean isPresentReport(String reportId) {
        return getCurrentSession().find(Report.class, reportId) != null;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
