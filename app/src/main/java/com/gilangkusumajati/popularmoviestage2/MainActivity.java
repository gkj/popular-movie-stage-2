package com.gilangkusumajati.popularmoviestage2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.gilangkusumajati.popularmoviestage2.adapter.MovieAdapter;
import com.gilangkusumajati.popularmoviestage2.api.MovieAPIClient;
import com.gilangkusumajati.popularmoviestage2.api.MovieResponse;
import com.gilangkusumajati.popularmoviestage2.model.Movie;
import com.gilangkusumajati.popularmoviestage2.model.MovieMenu;
import com.gilangkusumajati.popularmoviestage2.util.DBUtil;
import com.gilangkusumajati.popularmoviestage2.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<List<Movie>> {

    public static final String MOVIE_ITEM_EXTRA = "movie_item_extra";
    private static final int ONLINE_MOVIE_LOADER_ID = 999;

    private static final int LANDSCAPE_COLUMN = 4;
    private static final int POTRAIT_COLUMN = 2;

    @BindView(R.id.textview_error_message)
    TextView errorMessageTextView;

    @BindView(R.id.recyclerview_movie)
    RecyclerView movieRecyclerView;

    @BindView(R.id.pb_indicator)
    ProgressBar indicatorProgressBar;

    private MovieAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Context context = this;
        int totalColumn = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                LANDSCAPE_COLUMN : POTRAIT_COLUMN;
        GridLayoutManager layoutManager = new GridLayoutManager(context, totalColumn);
        movieRecyclerView.setLayoutManager(layoutManager);
        movieRecyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this);
        movieRecyclerView.setAdapter(movieAdapter);

        if (savedInstanceState != null) {
            ArrayList<Movie> movieList = savedInstanceState.getParcelableArrayList(MOVIE_ITEM_EXTRA);
            movieAdapter.setItem(movieList);
            hideProgressIndicator();
        } else {
            getSupportLoaderManager().initLoader(ONLINE_MOVIE_LOADER_ID, null, this);
        }

        MovieMenu menu = PreferenceUtil.getSelectedMenu(this);
        setTitle(menu.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        MovieMenu menu = MovieMenu.FAVORITE;

        switch (id) {
            case R.id.action_popular_movie:
                menu = MovieMenu.POPULAR;
                break;
            case R.id.action_top_rated_movie:
                menu = MovieMenu.TOP_RATED;
                break;
            case R.id.action_favorited_movie:
                menu = MovieMenu.FAVORITE;
                break;
        }

        setSelectedMenu(menu);
        fetchMovie();

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieAdapter != null) {
            List<Movie> movieList = movieAdapter.getItem();
            if (movieList != null && !movieList.isEmpty()) {
                outState.putParcelableArrayList(MOVIE_ITEM_EXTRA, new ArrayList<>(movieList));
            }
        }
    }

    @Override
    public void onListItemClick(Movie movie) {
        openMovieDetail(movie);
    }

    private void openMovieDetail(Movie movie) {
        if (movie != null) {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MOVIE_ITEM_EXTRA, movie);
            startActivity(intent);
        }
    }

    private void fetchMovie() {
        MovieMenu menu = PreferenceUtil.getSelectedMenu(this);
        setTitle(menu.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<MovieResponse> githubSearchLoader = loaderManager.getLoader(ONLINE_MOVIE_LOADER_ID);
        if (githubSearchLoader == null) {
            loaderManager.initLoader(ONLINE_MOVIE_LOADER_ID, null, this);
        } else {
            loaderManager.restartLoader(ONLINE_MOVIE_LOADER_ID, null, this);
        }
    }

    public void showProgressIndicator() {
        indicatorProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressIndicator() {
        indicatorProgressBar.setVisibility(View.GONE);
    }

    private void setSelectedMenu(MovieMenu menu) {
        PreferenceUtil.setSelectedMenu(this, menu);
        setTitle(menu.toString());
    }

    private void showList() {
        errorMessageTextView.setVisibility(View.INVISIBLE);
        indicatorProgressBar.setVisibility(View.INVISIBLE);
        movieRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String errorMessage) {
        movieRecyclerView.setVisibility(View.INVISIBLE);
        errorMessageTextView.setText(errorMessage);
        errorMessageTextView.setVisibility(View.VISIBLE);
        indicatorProgressBar.setVisibility(View.GONE);
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            @Override
            protected void onStartLoading() {
                showProgressIndicator();
                forceLoad();
            }

            @Override
            public List<Movie> loadInBackground() {
                try {
                    MovieMenu movieMenu = PreferenceUtil.getSelectedMenu(MainActivity.this);

                    if (movieMenu == MovieMenu.FAVORITE) {
                        return DBUtil.getMovies(getContentResolver());

                    } else {
                        Call<MovieResponse> call = movieMenu == MovieMenu.POPULAR ?
                                MovieAPIClient.getInstance().getPopularMovie() :
                                MovieAPIClient.getInstance().getTopRatedMovie();
                        Response<MovieResponse> response = call.execute();
                        if (response != null && response.isSuccessful() && response.body() != null) {
                            return response.body().getResults();
                        } else {
                            return null;
                        }
                    }

                } catch (Exception ex) {
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        hideProgressIndicator();
        if (null == data) {
            MovieMenu menu = PreferenceUtil.getSelectedMenu(MainActivity.this);
            String message = menu == MovieMenu.FAVORITE ?
                    getString(R.string.no_favorite_movie) :
                    getString(R.string.cannot_get_movie);
            showErrorMessage(message);
        } else {
            movieAdapter.setItem(data);
            showList();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}
