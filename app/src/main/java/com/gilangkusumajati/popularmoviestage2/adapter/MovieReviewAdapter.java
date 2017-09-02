package com.gilangkusumajati.popularmoviestage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.gilangkusumajati.popularmoviestage2.R;
import com.gilangkusumajati.popularmoviestage2.model.MovieReview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gilang Kusuma Jati on 7/30/17.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    private final MovieReviewListItemClickListener listItemClickListener;
    private List<MovieReview> items;

    public MovieReviewAdapter(MovieReviewListItemClickListener onClickListener) {
        this.listItemClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieReview movie = items.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<MovieReview> items) {
        if (this.items == null)
            this.items = new ArrayList<>();

        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textview_author)
        TextView authorTextView;

        @BindView(R.id.textview_content)
        TextView contentTextView;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        private void bind(MovieReview movieReview) {
            authorTextView.setText(movieReview.getAuthor());
            contentTextView.setText(movieReview.getContent());
        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onMovieReviewListItemClick(items.get(getAdapterPosition()));
        }
    }

    public interface MovieReviewListItemClickListener {
        void onMovieReviewListItemClick(MovieReview movieReview);
    }
}
