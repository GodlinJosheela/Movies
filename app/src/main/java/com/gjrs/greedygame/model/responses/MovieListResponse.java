package com.gjrs.greedygame.model.responses;

import java.util.List;

import com.gjrs.greedygame.model.MovieNetworkLite;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MovieListResponse {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("results")
    @Expose
    private List<MovieNetworkLite> moviesResult;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<MovieNetworkLite> getMoviesResult() {
        return moviesResult;
    }


}
