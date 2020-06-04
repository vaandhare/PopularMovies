package com.example.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ReviewAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Review>> {

    private Bundle mBundle;

    ReviewAsyncTaskLoader(Context context, Bundle bundle) {
        super(context);
        mBundle = bundle;
    }

    @Override
    public ArrayList<Review> loadInBackground() {
        Context context = this.getContext();
        String movieId = mBundle.getString("movie_id");
        String result = "";
        URL url = NetworkUtils.buildUrlForMovies(movieId, "reviews");
        try {
            result = NetworkUtils.getResponseFromHttpUrl(url);//This just create a HTTPUrlConnection and return result in strings
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NetworkUtils.reviewsForMovie(result, context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}