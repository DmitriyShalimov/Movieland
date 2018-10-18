package com.shalimov.movieland.entity;

import java.util.Objects;

public class Genre {
    private int id;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Genre genre = (Genre) object;
        return id == genre.id &&
                Objects.equals(title, genre.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
