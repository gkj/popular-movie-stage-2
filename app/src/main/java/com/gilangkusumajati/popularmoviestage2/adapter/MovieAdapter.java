package com.gilangkusumajati.popularmoviestage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gilangkusumajati.popularmoviestage2.R;
import com.gilangkusumajati.popularmoviestage2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gilang Kusuma Jati on 6/27/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private final ListItemClickListener listItemClickListener;
    private List<Movie> movieData;

    public MovieAdapter(ListItemClickListener onClickListener) {
        this.listItemClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movieData.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movieData == null ? 0 : movieData.size();
    }

    public void setItem(List<Movie> movieData) {
        this.movieData = movieData;
        notifyDataSetChanged();
    }

    public List<Movie> getItem() {
        return movieData;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textview_title)
        TextView titleTextView;

        @BindView(R.id.imageview_poster)
        ImageView posterImageView;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        private void bind(Movie movie) {
            titleTextView.setText(movie.getOriginalTitle());
            Picasso.with(posterImageView.getContext()).load(movie.getFullPosterPath()).into(posterImageView);
        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onListItemClick(movieData.get(getAdapterPosition()));
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(Movie movie);
    }
}
