package com.gilangkusumajati.popularmoviestage2.api;

import com.gilangkusumajati.popularmoviestage2.model.MovieReview;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gilang Kusuma Jati on 7/29/17.
 */

public class MovieReviewResponse {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<MovieReview> results = null;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
    private int totalResults;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieReview> getResults() {
        return results;
    }

    public void setResults(List<MovieReview> MovieReviews) {
        this.results = MovieReviews;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
