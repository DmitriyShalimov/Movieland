package com.shalimov.movieland.entity;

public class MovieRequest {
    private SortType priceOrder;
    private SortType ratingOrder;
    private int page;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
