package com.example.popularmovies;

public class Trailer {

    private String mName;
    private String mVideoKey;

    Trailer(String name, String videoKey) {
        mName = name;
        mVideoKey = videoKey;
    }

    String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    String getVideoKey() {
        return mVideoKey;
    }
}