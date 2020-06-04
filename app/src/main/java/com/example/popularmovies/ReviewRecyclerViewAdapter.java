package com.example.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewRecyclerViewAdapter extends
        RecyclerView.Adapter<ReviewRecyclerViewAdapter.ReviewViewHolder> {

    private ArrayList<Review> mDataSource;


    ReviewRecyclerViewAdapter(ArrayList<Review> dataSource) {
        this.mDataSource = dataSource;
    }

    void setDataSource(ArrayList<Review> dataSource) {
        mDataSource = dataSource;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewRecyclerViewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_view_holder, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewRecyclerViewAdapter.ReviewViewHolder holder, int position) {
        Review review = mDataSource.get(position);
        String author = review.getAuthor();
        String content = review.getContent();
        holder.mAuthorTextView.setText(author);
        holder.mContentTextView.setText(content);
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView mAuthorTextView;
        TextView mContentTextView;

        ReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = itemView.findViewById(R.id.review_author_tv);
            mContentTextView = itemView.findViewById(R.id.review_content_tv);
        }
    }
}