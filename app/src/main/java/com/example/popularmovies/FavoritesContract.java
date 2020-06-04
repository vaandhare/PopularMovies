package com.example.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesContract {

    // URI's used by contentProvider
    static final String AUTHORITY = "com.example.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    //below is what we use in our queries
    static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FavoritesEntry.TABLE_NAME).build();

    public static final class FavoritesEntry implements BaseColumns {

        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "movieTitle";
        public static final String COLUMN_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_AVERAGE_VOTE = "movieAverageVote";
        public static final String COLUMN_PLOT = "moviePlot";
        public static final String COLUMN_POSTER_PATH = "moviePosterPath";
        static final String TABLE_NAME = "favoritesTable";


    }
}