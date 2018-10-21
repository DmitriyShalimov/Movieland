package com.shalimov.movieland.filter;

import com.shalimov.movieland.entity.SortType;

public class MovieFilter {
   private SortType priceOrder;
   private SortType ratingOrder;

    public SortType getPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(SortType priceOrder) {
        this.priceOrder = priceOrder;
    }

    public SortType getRatingOrder() {
        return ratingOrder;
    }

    public void setRatingOrder(SortType ratingOrder) {
        this.ratingOrder = ratingOrder;
    }
}
