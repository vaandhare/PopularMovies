package com.example.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MoviesAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    MoviesAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        Context context = this.getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String fetchMoviesString = sharedPreferences.getString(context.getResources().getString(R.string.fetch_movie_key), context.getResources().getString(R.string.fetch_by_most_popular));
        // do the backGround work on another thread
        URL url = NetworkUtils.buildUrlForMovies(null, fetchMoviesString);
        String result = "";
        try {
            result = NetworkUtils.getResponseFromHttpUrl(url);//This just create a HTTPUrlConnection and return result in strings
        } catch (IOException e) {
            e.printStackTrace();
        }

        return NetworkUtils.moviesFromJson(result, context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
