package com.gilangkusumajati.popularmoviestage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gilangkusumajati.popularmoviestage2.R;
import com.gilangkusumajati.popularmoviestage2.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gilang Kusuma Jati on 7/30/17.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    private final MovieTrailerListItemClickListener listItemClickListener;
    private List<MovieTrailer> items;

    public MovieTrailerAdapter(MovieTrailerListItemClickListener onClickListener) {
        this.listItemClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.video_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieTrailer movie = items.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<MovieTrailer> items) {
        if (this.items == null)
            this.items = new ArrayList<>();

        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageview_trailer)
        ImageView trailerImageView;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        private void bind(MovieTrailer movieVideo) {
            Picasso.with(trailerImageView.getContext()).load(movieVideo.getThumbnailUrl()).into(trailerImageView);
        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onMovieTrailerListItemClick(items.get(getAdapterPosition()));
        }
    }

    public interface MovieTrailerListItemClickListener {
        void onMovieTrailerListItemClick(MovieTrailer movieTrailer);
    }
}
