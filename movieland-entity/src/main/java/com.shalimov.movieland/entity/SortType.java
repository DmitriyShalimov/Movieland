package com.shalimov.movieland.entity;

public enum SortType {
    ASC("asc"), DESC("desc");
    private String id;

    SortType(String id) {
        this.id = id;
    }

    public static SortType getTypeById(String id) {
        for (SortType sortType : SortType.values()) {
            if (sortType.getId().equals(id)) {
                return sortType;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }
}
