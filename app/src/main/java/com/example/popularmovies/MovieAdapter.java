package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<Movie> moviesArrayList;
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    final private MovieAdapterClickListener onClickListener;

    public interface MovieAdapterClickListener {
        void onListItemClick(Movie currentMovie);
    }

    MovieAdapter(Context context, ArrayList<Movie> moviesArrayList, MovieAdapterClickListener onClickListener) {
        this.context = context;
        this.moviesArrayList = moviesArrayList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_items;
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemViewHolder = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieViewHolder(itemViewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        context = holder.movieListItem.getContext();
        Movie currentMovie = moviesArrayList.get(position);
        Glide.with(context)
                .load(buildPosterImageUrl(currentMovie.getPosterPath()))
                .centerCrop()
                .into(holder.movieListItem);
    }

    @Override
    public int getItemCount() {
        return moviesArrayList == null ? 0 : moviesArrayList.size();
    }

    void setMovieData(ArrayList<Movie> movieData) {
        moviesArrayList = movieData;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView movieListItem;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieListItem = itemView.findViewById(R.id.moviePoster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Movie currentMovie = moviesArrayList.get(clickedPosition);
            onClickListener.onListItemClick(currentMovie);
        }
    }

    static String buildPosterImageUrl(String filepath) {
        return BASE_IMAGE_URL + "w185/" + filepath;
    }

    static String buildBackdropImageUrl(String filepath) {
        return BASE_IMAGE_URL + "w500/" + filepath;
    }
}