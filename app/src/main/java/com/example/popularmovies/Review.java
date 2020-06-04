package com.example.popularmovies;

public class Review {

    private String mAuthor;
    private String mContent;


    public Review(String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    String getAuthor() {
        return mAuthor;
    }

    String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}