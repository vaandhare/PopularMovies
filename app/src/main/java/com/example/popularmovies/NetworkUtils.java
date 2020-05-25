package com.example.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String API_KEY = "PRIVATE API KEY";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_PARAMETER = "api_key";


    static URL buildUrl(String sortByParameter) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortByParameter)
                .appendQueryParameter(API_PARAMETER, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "buildUrl: ", e);
        }
        Log.d(TAG, "buildUrl() called with: sortByParameter = [" + sortByParameter + "]" + "Built URL is: " + url);
        return url;
    }

    static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        InputStream inputStream = null;

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            httpURLConnection.connect();


            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHttpRequest: Problem retrieving Json results", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
