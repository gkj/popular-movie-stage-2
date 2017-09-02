package com.gilangkusumajati.popularmoviestage2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.gilangkusumajati.popularmoviestage2.database.MovieContract;
import com.gilangkusumajati.popularmoviestage2.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.imageview_cover)
    ImageView coverImage;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(MainActivity.MOVIE_ITEM_EXTRA)) {
                movie = intent.getParcelableExtra(MainActivity.MOVIE_ITEM_EXTRA);
            }
        } else if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(MainActivity.MOVIE_ITEM_EXTRA);
        }

        if (movie != null) {

            // check whether movie favorited or not
            Cursor cursor = null;
            try {
                Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getMovieId())).build();
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.getCount() == 1) {
                    if (cursor.moveToFirst()) {
                        movie.setRowId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
                    }
                } else {
                    movie.setRowId(-1);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            if (getSupportActionBar() != null) {
                ActionBar actionBar = getSupportActionBar();

                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(movie.getTitle());
                Picasso.with(this).load(movie.getFullBackdroprPath()).placeholder(R.drawable.default_cover_image).error(R.drawable.default_cover_image).into(coverImage);

                Bundle bundle = new Bundle();
                bundle.putParcelable(MainActivity.MOVIE_ITEM_EXTRA, movie);

                MovieDetailFragment fragment = new MovieDetailFragment();
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_view, fragment);
                fragmentTransaction.commit();
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movie != null) {
            outState.putParcelable(MainActivity.MOVIE_ITEM_EXTRA, movie);
        }
    }
}
