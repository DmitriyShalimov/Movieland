package com.shalimov.movieland.dao.jpa.dto;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "country")
public class CountryDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    @Column(name = "name")
    private  String title;

    @ManyToMany(mappedBy = "countries", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<MovieDto> movies = new ArrayList<>();

    public List<MovieDto> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieDto> movies) {
        this.movies = movies;
    }

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
}
