package com.shalimov.movieland.entity;

public class MovieRequest {
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
