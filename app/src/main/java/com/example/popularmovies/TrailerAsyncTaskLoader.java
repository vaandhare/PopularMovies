package com.example.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class TrailerAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Trailer>> {

    private Bundle mBundle;

    TrailerAsyncTaskLoader(Context context, Bundle bundle) {
        super(context);
        mBundle = bundle;
    }

    @Override
    public ArrayList<Trailer> loadInBackground() {
        Context context = this.getContext();
        String movieId = mBundle.getString("movie_id");
        String result = "";
        URL url = NetworkUtils.buildUrlForMovies(movieId, "videos");
        try {
            result = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NetworkUtils.trailersForMovie(result, context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
