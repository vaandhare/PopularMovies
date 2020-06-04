package com.example.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailedActivity extends AppCompatActivity implements TrailerRecyclerViewAdapter.ButtonTapped,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REVIEW_LOADER = 100;
    private static final int TRAILER_LOADER = 1000;

    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.poster_iv)
    ImageView mPosterImageView;
    @BindView(R.id.title_tv)
    TextView mTitleTextView;
    @BindView(R.id.plot_tv)
    TextView mPlotTextView;
    @BindView(R.id.date_tv)
    TextView mDateTextView;
    @BindView(R.id.reviews_header_tv)
    TextView mReviewsTextView;
    @BindView(R.id.trailers_header_tv)
    TextView mTrailersTextView;
    @BindView(R.id.vote_tv)
    TextView mVoteTextView;
    @BindView(R.id.ratingBar)
    RatingBar mVoteRatingBar;
    @BindView(R.id.review_recycler_view)
    RecyclerView mReviewRecyclerView;
    @BindView(R.id.trailer_recycler_view)
    RecyclerView mTrailerRecyclerView;
    @BindView(R.id.favorite_b)
    Button mFavoriteButton;
    @BindString(R.string.add_to_favorites_button_title)
    String mAddFavoritesTitle;
    @BindString(R.string.remove_from_favorites_button_title)
    String mRemoveFavoritesTitle;
    @BindString(R.string.movie_detail_Key)
    String mMovieDetailKey;
    @BindString(R.string.movie_id_Key)
    String mMovieIdKey;
    @BindString(R.string.share_type)
    String mShareType;
    @BindString(R.string.share_intent_title)
    String mShareTitle;
    @BindString(R.string.share_body)
    String mShareBody;
    @BindString(R.string.scroll_position_key)
    String mScrollPositionKey;

    private ArrayList<Review> mReviews;
    private ArrayList<Trailer> mTrailers;
    private ReviewRecyclerViewAdapter mReviewRecyclerViewAdapter;
    private TrailerRecyclerViewAdapter mTrailerRecyclerViewAdapter;
    private String mMovieTitle;
    private String mMovieId;
    private String mMoviePlot;
    private String mMovieReaseDate;
    private String mMovieAverageVote;
    private String mMoviePosterPath;
    private boolean mIsFavorite;
    private boolean mHasTrailers;
    private int[] mScrollPosition;
    private boolean mReviewsLoaded;
    private boolean mTrailersLoaded;
    public android.app.LoaderManager.LoaderCallbacks<ArrayList<Review>> reviewLoaderListener
            = new android.app.LoaderManager.LoaderCallbacks<ArrayList<Review>>() {

        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int i, Bundle bundle) {
            return new ReviewAsyncTaskLoader(DetailedActivity.this, bundle);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> reviews) {
            if (reviews != null) {
                if (reviews.size() > 0) {
                    mReviewsTextView.setVisibility(View.VISIBLE);
                    mReviews = reviews;
                    mReviewRecyclerViewAdapter.setDataSource(mReviews);
                }
            }
            mReviewsLoaded = true;
            if (mTrailersLoaded) {
                restoreScrollPosition();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Review>> loader) {
        }
    };
    public android.app.LoaderManager.LoaderCallbacks<ArrayList<Trailer>> trailerLoaderListener
            = new android.app.LoaderManager.LoaderCallbacks<ArrayList<Trailer>>() {

        @Override
        public Loader<ArrayList<Trailer>> onCreateLoader(int i, Bundle bundle) {
            return new TrailerAsyncTaskLoader(DetailedActivity.this, bundle);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> trailers) {
            if (trailers != null) {
                if (trailers.size() > 0) {
                    mTrailers = trailers;
                    mTrailerRecyclerViewAdapter.setDataSource(mTrailers);
                    mTrailersTextView.setVisibility(View.VISIBLE);
                    mHasTrailers = true;
                    invalidateOptionsMenu();

                }
            }
            mTrailersLoaded = true;
            if (mReviewsLoaded) {
                restoreScrollPosition();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {
        }
    };
    private Toast mFavoritesToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Movie movie = getIntent().getParcelableExtra(mMovieDetailKey);
        if (movie != null) {
            mMovieTitle = movie.getMovieTitle();
            mMovieId = movie.getMovieId();
            mMoviePlot = movie.getPlot();
            mMovieReaseDate = movie.getReleaseDate();
            mMovieAverageVote = movie.getAverageVote();
            mMoviePosterPath = movie.getPosterPath();
            getSupportLoaderManager().initLoader(1, null, this);
            setupTheRecyclerViews();
            updateTheUI();
            fetchJsonForReviews(movie.getMovieId());
            fetchJsonForTrailers(movie.getMovieId());
        }
    }

    private void setupTheRecyclerViews() {
        mReviews = new ArrayList<>();
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        reviewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewRecyclerViewAdapter = new ReviewRecyclerViewAdapter(mReviews);
        mReviewRecyclerView.setAdapter(mReviewRecyclerViewAdapter);
        mReviewRecyclerView.setNestedScrollingEnabled(false);
        mTrailers = new ArrayList<>();
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        trailerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerRecyclerViewAdapter = new TrailerRecyclerViewAdapter(mTrailers, this);
        mTrailerRecyclerView.setAdapter(mTrailerRecyclerViewAdapter);
        mTrailerRecyclerView.setNestedScrollingEnabled(false);
    }

    private void updateTheUI() {
        NetworkUtils.loadMoviePosterIntoImageView(this, mMoviePosterPath, mPosterImageView);
        mTitleTextView.setText(mMovieTitle);
        mPlotTextView.setText(mMoviePlot);
        mDateTextView.setText(ApplicationUtils.formattedDateWithLocal(mMovieReaseDate));
        mVoteTextView.setText(mMovieAverageVote);
        mVoteRatingBar.setRating(Float.parseFloat(mMovieAverageVote));
    }

    //AsyncTaskLoader for fetch of  reviews
    private void fetchJsonForReviews(String movieId) {
        Bundle bundle = new Bundle();
        bundle.putString(mMovieIdKey, movieId);
        android.app.LoaderManager loaderManager = getLoaderManager();
        Loader<String> loader = loaderManager.getLoader(REVIEW_LOADER);
        if (loader == null) {
            // If we don't have a loader lets create one
            loaderManager.initLoader(REVIEW_LOADER, bundle, reviewLoaderListener);
        } else {
            // other wise lets restart the loader we have
            loaderManager.restartLoader(REVIEW_LOADER, bundle, reviewLoaderListener);
        }
    }

    //AsyncTaskLoader for fetch of  trailers
    private void fetchJsonForTrailers(String movieId) {
        Bundle bundle = new Bundle();
        //Add a key value pair of the past in url string to the bundle
        bundle.putString(mMovieIdKey, movieId);
        android.app.LoaderManager loaderManager = getLoaderManager();
        Loader<String> loader = loaderManager.getLoader(TRAILER_LOADER);
        if (loader == null) {
            // If we don't have a loader lets create one
            loaderManager.initLoader(TRAILER_LOADER, bundle, trailerLoaderListener);
        } else {
            // other wise lets restart the loader we have
            loaderManager.restartLoader(TRAILER_LOADER, bundle, trailerLoaderListener);
        }
    }

    @Override
    public void trailerSelected(String videoKey) {
        NetworkUtils.getIntentForTrailer(this, videoKey);
    }

    @OnClick(R.id.favorite_b)
    public void onClick(View view) {
        if (mIsFavorite) {
            removeFromFavorites();
        } else {
            addToFavorites();
        }
        getSupportLoaderManager().initLoader(1, null, this);
    }

    private void removeFromFavorites() {
        getContentResolver().delete(FavoritesContract.CONTENT_URI,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?", new String[]{mMovieId});
        if (mFavoritesToast != null) {
            mFavoritesToast.cancel();
        }
        mFavoritesToast = Toast.makeText(this, "Movie removed from favorites", Toast.LENGTH_LONG);
        mFavoritesToast.show();
    }

    private void addToFavorites() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, mMovieId);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_TITLE, mMovieTitle);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_PLOT, mMoviePlot);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH, mMoviePosterPath);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE, mMovieReaseDate);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_AVERAGE_VOTE, mMovieAverageVote);
        getContentResolver().insert(FavoritesContract.CONTENT_URI, contentValues);
        if (mFavoritesToast != null) {
            mFavoritesToast.cancel();
        }
        mFavoritesToast = Toast.makeText(this, "Movie added to favorites", Toast.LENGTH_LONG);
        mFavoritesToast.show();
    }

    @NonNull
    @Override
    public androidx.loader.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri NAME_URI = FavoritesContract.CONTENT_URI;
        return new CursorLoader(this, NAME_URI, null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull androidx.loader.content.Loader<Cursor> loader, Cursor cursor) {
        ArrayList<String> movieIds = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToPosition(-1);
            try {
                while (cursor.moveToNext()) {
                    String movieId = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID));

                    movieIds.add(movieId);
                }
            } finally {
                if (movieIds.contains(mMovieId)) {
                    mIsFavorite = true;
                    mFavoriteButton.setText(mRemoveFavoritesTitle);

                } else {
                    mIsFavorite = false;
                    mFavoriteButton.setText(mAddFavoritesTitle);

                }
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<Cursor> loader) {

    }

    //Sharing
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        if (mHasTrailers) {
            menuItem.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            if (mTrailers.size() > 0) {
                //get first trailer
                Trailer trailer = mTrailers.get(0);
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType(mShareType);
                String bodyText = NetworkUtils.linkForTrailer(trailer.getVideoKey());
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mShareBody);
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, bodyText);
                startActivity(Intent.createChooser(shareIntent, mShareTitle));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(mScrollPositionKey,
                new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mScrollPosition = savedInstanceState.getIntArray(mScrollPositionKey);

    }

    // this is only called once the reviews and trailers are both loaded
    void restoreScrollPosition() {
        if (mScrollPosition != null)
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.scrollTo(mScrollPosition[0], mScrollPosition[1]);
                }
            });
    }

}


