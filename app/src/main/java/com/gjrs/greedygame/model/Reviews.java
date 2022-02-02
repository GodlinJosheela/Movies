package com.gjrs.greedygame.model;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel(Parcel.Serialization.BEAN)
public class Reviews {

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("updated_at")
    private String date;

    @SerializedName("author_details")
    private Author authorDetails;

    public String getDate() {
        return date;
    }

    public Author getAuthorDetails() {
        return authorDetails;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

}
