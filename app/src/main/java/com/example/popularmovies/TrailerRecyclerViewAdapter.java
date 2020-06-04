package com.example.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<com.example.popularmovies.TrailerRecyclerViewAdapter.TrailerViewHolder> {

    private final ButtonTapped mButtonTapped;
    private ArrayList<Trailer> mDataSource;

    TrailerRecyclerViewAdapter(ArrayList<Trailer> dataSource, ButtonTapped buttonTapped) {
        mDataSource = dataSource;
        mButtonTapped = buttonTapped;
    }

    void setDataSource(ArrayList<Trailer> dataSource) {
        mDataSource = dataSource;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerRecyclerViewAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_view_holder, parent, false);
        return new TrailerRecyclerViewAdapter.TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerRecyclerViewAdapter.TrailerViewHolder holder, int position) {
        Trailer trailer = mDataSource.get(position);
        String name = trailer.getName();
        holder.mTrailerButton.setText(name);
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    public interface ButtonTapped {
        void trailerSelected(String videoKey);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button mTrailerButton;

        TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerButton = itemView.findViewById(R.id.trailer_button);
            mTrailerButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Trailer trailer = mDataSource.get(position);
            mButtonTapped.trailerSelected(trailer.getVideoKey());
        }
    }

}