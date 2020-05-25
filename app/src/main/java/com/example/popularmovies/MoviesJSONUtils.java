package com.example.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class MoviesJSONUtils {
    private static final String TAG = MoviesJSONUtils.class.getSimpleName();
    private static final String ID = "id";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String POSTER_IMG_PATH = "poster_path";
    private static final String BACKDROP_IMG_PATH = "backdrop_path";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String RESULTS = "results";

    private MoviesJSONUtils() {
    }

    static ArrayList<Movie> parseJson(String movieJson) {

        ArrayList<Movie> moviesArrayList = new ArrayList<>();

        try{
            JSONObject root = new JSONObject(movieJson);

            if (root.has(RESULTS)) {
                JSONArray movieArray = root.getJSONArray(RESULTS);

                for (int i = 0; i < movieArray.length(); i++) {
                    JSONObject currentMovie = movieArray.getJSONObject(i);

                    int id = currentMovie.getInt(ID);
                    String originalTitle = currentMovie.optString(ORIGINAL_TITLE);
                    String posterPath = currentMovie.optString(POSTER_IMG_PATH);
                    String backDropPath = currentMovie.optString(BACKDROP_IMG_PATH);
                    String overview = currentMovie.optString(OVERVIEW);
                    double voteAverage = currentMovie.optDouble(VOTE_AVERAGE);
                    String releaseDate = currentMovie.optString(RELEASE_DATE);

                    Movie movie = new Movie(id, originalTitle, posterPath, backDropPath, overview, voteAverage, releaseDate);
                    moviesArrayList.add(movie);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moviesArrayList;
    }

}