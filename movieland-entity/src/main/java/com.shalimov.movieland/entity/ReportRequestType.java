package com.shalimov.movieland.entity;

public enum ReportRequestType {
    ALL_MOVIES("allMovies"), ADDED_DURING_PERIOD("addedDuringPeriod"), TOP_ACTIVE_USERS("topActiveUsers");
    private String id;

    ReportRequestType(String id) {
        this.id = id;
    }

    public static ReportRequestType getReportRequestTypeById(String id) {
        for (ReportRequestType reportRequestType : ReportRequestType.values()) {
            if (reportRequestType.getId().equals(id)) {
                return reportRequestType;
            }
        }
        throw new IllegalArgumentException("No reportType found for id: " + id);
    }

    public String getId() {
        return id;
    }
}
