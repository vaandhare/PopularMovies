package com.example.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String mId;
    private String mMovieTitle;
    private String mReleaseDate;
    private String mAverageVote;
    private String mPlot;
    private String mPosterPath;

    Movie(String id, String movieTitle, String releaseDate, String averageVote, String plot, String posterPath) {
        this.mId = id;
        this.mMovieTitle = movieTitle;
        this.mReleaseDate = releaseDate;
        this.mAverageVote = averageVote;
        this.mPlot = plot;
        this.mPosterPath = posterPath;
    }

    private Movie(Parcel in) {
        mId = in.readString();
        mMovieTitle = in.readString();
        mReleaseDate = in.readString();
        mAverageVote = in.readString();
        mPlot = in.readString();
        mPosterPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    String getMovieId() {
        return mId;
    }

    String getMovieTitle() {
        return mMovieTitle;
    }

    String getReleaseDate() {
        return mReleaseDate;
    }

    String getAverageVote() {
        return mAverageVote;
    }

    String getPlot() {
        return mPlot;
    }

    String getPosterPath() {
        return mPosterPath;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mMovieTitle);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mAverageVote);
        parcel.writeString(mPlot);
        parcel.writeString(mPosterPath);
    }
}