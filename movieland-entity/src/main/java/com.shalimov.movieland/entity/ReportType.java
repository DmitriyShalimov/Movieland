package com.shalimov.movieland.entity;

public enum ReportType {
    PDF("pdf"), EXCEL("excel");
    private String id;

    ReportType(String id) {
        this.id = id;
    }

    public static ReportType getTypeById(String id) {
        for (ReportType reportType : ReportType.values()) {
            if (reportType.getId().equals(id)) {
                return reportType;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }
}
