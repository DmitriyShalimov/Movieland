package com.shalimov.movieland.dao.jpa.dto;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "movie")
public class MovieDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name_russian")
    private String nameRussian;
    @Column(name = "name_native")
    private String nameNative;
    @Column(name = "year_of_release")
    private int yearOfRelease;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private double price;
    @Column(name = "rating")
    private double rating;
    @Column(name = "picture_path")
    private String picturePath;
    @ManyToMany
    @JoinTable(name = "movie_genre",
            joinColumns = {@JoinColumn(name = "movie", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "genre", referencedColumnName = "id")})
    private List<GenreDto> genres;

    @ManyToMany
    @JoinTable(name = "movie_country",
            joinColumns = {@JoinColumn(name = "movie", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "country", referencedColumnName = "id")})
    private List<CountryDto> countries;

    public List<CountryDto> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryDto> countries) {
        this.countries = countries;
    }

    public List<GenreDto> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDto> genres) {
        this.genres = genres;
    }

    public MovieDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameRussian() {
        return nameRussian;
    }

    public void setNameRussian(String nameRussian) {
        this.nameRussian = nameRussian;
    }

    public String getNameNative() {
        return nameNative;
    }

    public void setNameNative(String nameNative) {
        this.nameNative = nameNative;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
