package com.gjrs.greedygame.model;

import com.google.gson.annotations.SerializedName;

public class Author {

    @SerializedName("name")
    private String name;
    @SerializedName("avatar_path")
    private String avatarPath;
    @SerializedName("rating")
    private String rating;

    public String getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getAvatarPath() {
        return avatarPath;
    }
}
