package com.gjrs.greedygame.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Objects;

@Parcel(Parcel.Serialization.BEAN)
public final class MovieNetworkLite implements IMovie {

    @SerializedName("vote_average")
    private float voterAverage;

    @SerializedName("poster_path")
    private String moviePoster;

    private String title;

    @SerializedName("original_language")
    private String language;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("id")
    private int movieId;

    MovieNetworkLite() {
    }

    public MovieNetworkLite(float voterAverage, String moviePoster, int movieId) {
        this.voterAverage = voterAverage;
        this.moviePoster = moviePoster;
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public float getVoterAverage() {
        return voterAverage;
    }

    @Override
    public String getMoviePoster() {
        return moviePoster;
    }

    @Override
    public int getMovieId() {
        return movieId;
    }

    @Override
    public String toString() {
        return "MovieNetworkLite{" +
                "voterAverage=" + voterAverage +
                ", moviePoster='" + moviePoster + '\'' +
                ", movieId=" + movieId +
                '}';
    }

    @Override
    public boolean equals(Object vo) {
        if (this == vo) return true;
        if (vo == null || getClass() != vo.getClass()) return false;
        MovieNetworkLite vthat = (MovieNetworkLite) vo;
        return movieId == vthat.movieId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId);
    }
}
