package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import static com.example.popularmovies.MainActivity.MOVIE_OBJECT;
import static com.example.popularmovies.MovieAdapter.buildBackdropImageUrl;
import static com.example.popularmovies.MovieAdapter.buildPosterImageUrl;

public class DetailedActivity extends AppCompatActivity {

    private final Context context = DetailedActivity.this;
    private Movie detailMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView posterImg = findViewById(R.id.img_poster);
        ImageView backdropImg = findViewById(R.id.img_backdrop);
        TextView releaseDate = findViewById(R.id.tv_releaseDate);
        TextView userRating = findViewById(R.id.tv_userRating);
        TextView synopsisText = findViewById(R.id.tv_synopsisText);

        Intent extraIntent = getIntent();
        if (extraIntent != null) {
            if (extraIntent.hasExtra(MOVIE_OBJECT)) {
                detailMovie = extraIntent.getParcelableExtra(MOVIE_OBJECT);
            }
        }

        Glide.with(context).load(buildPosterImageUrl(detailMovie.getPosterPath())).into(posterImg);

        Glide.with(context).load(buildBackdropImageUrl(detailMovie.getBackdropPath())).into(backdropImg);

        releaseDate.setText(detailMovie.getReleaseDate());
        userRating.setText(String.valueOf(detailMovie.getVoteAverage()));
        synopsisText.setText(detailMovie.getOverview());

        setTitle(detailMovie.getOriginalTitle());

    }
}



