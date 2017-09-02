package com.gilangkusumajati.popularmoviestage2.api;

import com.gilangkusumajati.popularmoviestage2.model.MovieTrailer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gilang Kusuma Jati on 7/30/17.
 */

public class MovieTrailerResponse {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<MovieTrailer> results = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieTrailer> getResults() {
        return results;
    }

    public void setResults(List<MovieTrailer> results) {
        this.results = results;
    }
}
