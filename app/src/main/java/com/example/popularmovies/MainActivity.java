package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterClickListener {
    public static final String MOVIE_OBJECT = "movie_object";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    public ArrayList<Movie> moviesArrayList;
    private RecyclerView recyclerView;
    ImageView errorImageView;
    TextView errorTextView;
    private static MovieAdapter movieAdapter;

    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            } else {
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnected();
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorImageView = findViewById(R.id.img_networkError);
        errorTextView = findViewById(R.id.tv_noInternet);
        recyclerView = findViewById(R.id.recyclerView);
        Context context = this;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setGridLayoutManager(context, 2);
        } else {
            setGridLayoutManager(context, 4);
        }

        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(context, moviesArrayList, (MovieAdapter.MovieAdapterClickListener) context);
        recyclerView.setAdapter(movieAdapter);

        checkAndCall(context, POPULAR);
    }

    private void setGridLayoutManager(Context context, int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void checkAndCall(Context context, String sortByParameter) {
        if (isNetworkConnected(context)) {
            makeMoviesQuery(sortByParameter);
        } else {
            showErrorImageView();
        }
    }

    private void makeMoviesQuery(String sortByParameter) {
        showRecyclerView();
        new MoviesAsyncTasks().execute(sortByParameter);
    }

    private void showRecyclerView() {
        errorImageView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorImageView() {
        errorImageView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.popularSort:
                checkAndCall(this, POPULAR);
                setTitle("Popular Movies");
                return true;
            case R.id.TopRatedSort:
                checkAndCall(this, TOP_RATED);
                setTitle("Top Rated Movies");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(Movie currentMovie) {
        Intent MainToDetailIntent = new Intent(MainActivity.this, DetailedActivity.class);
        MainToDetailIntent.putExtra(MOVIE_OBJECT, currentMovie);
        startActivity(MainToDetailIntent);
    }


    private static class MoviesAsyncTasks extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            String sortByParameter = strings[0];
            URL moviesQueryUrl = NetworkUtils.buildUrl(sortByParameter);

            String JsonResponse;
            try {
                switch (sortByParameter) {
                    case (POPULAR):
                        moviesQueryUrl = NetworkUtils.buildUrl(POPULAR);
                        break;
                    case (TOP_RATED):
                        moviesQueryUrl = NetworkUtils.buildUrl(TOP_RATED);
                        break;
                    default:
                        throw new UnsupportedOperationException("Unknown URL " + moviesQueryUrl);
                }
                JsonResponse = NetworkUtils.makeHttpRequest(moviesQueryUrl);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return MoviesJSONUtils.parseJson(JsonResponse);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            if (movies != null) {
                movieAdapter.setMovieData(movies);
            }
        }
    }
}