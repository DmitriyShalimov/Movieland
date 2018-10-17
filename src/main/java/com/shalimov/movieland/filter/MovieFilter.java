package com.shalimov.movieland.filter;

import com.shalimov.movieland.entity.SortType;

public class MovieFilter {
   private SortType price;
   private SortType rating;

    public SortType getPrice() {
        return price;
    }

    public void setPrice(SortType price) {
        this.price = price;
    }

    public SortType getRating() {
        return rating;
    }

    public void setRating(SortType rating) {
        this.rating = rating;
    }
}
