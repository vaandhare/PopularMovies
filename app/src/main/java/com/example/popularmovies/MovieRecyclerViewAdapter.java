package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private final ListItemClickListener mListItemClickListener;
    private ArrayList<Movie> mDataSource;
    private Context mContext;

    MovieRecyclerViewAdapter(ArrayList<Movie> dataSource, Context context, ListItemClickListener listItemClickListener) {
        this.mDataSource = dataSource;
        this.mContext = context;
        this.mListItemClickListener = listItemClickListener;
    }

    void setDataSource(ArrayList<Movie> dataSource) {
        mDataSource = dataSource;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_items, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie details = mDataSource.get(position);
        String posterPath = details.getPosterPath();
        NetworkUtils.loadMoviePosterIntoImageView(mContext, posterPath, holder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }


    public interface ListItemClickListener {
        void onMovieItemClick(Movie movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mMoviePoster;

        MovieViewHolder(View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.poster_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListItemClickListener.onMovieItemClick(mDataSource.get(position));
        }
    }
}