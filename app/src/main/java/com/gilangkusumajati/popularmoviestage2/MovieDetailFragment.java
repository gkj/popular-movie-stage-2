package com.gilangkusumajati.popularmoviestage2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gilangkusumajati.popularmoviestage2.adapter.MovieReviewAdapter;
import com.gilangkusumajati.popularmoviestage2.adapter.MovieTrailerAdapter;
import com.gilangkusumajati.popularmoviestage2.api.MovieAPIClient;
import com.gilangkusumajati.popularmoviestage2.api.MovieReviewResponse;
import com.gilangkusumajati.popularmoviestage2.api.MovieTrailerResponse;
import com.gilangkusumajati.popularmoviestage2.database.MovieContract;
import com.gilangkusumajati.popularmoviestage2.model.Movie;
import com.gilangkusumajati.popularmoviestage2.model.MovieReview;
import com.gilangkusumajati.popularmoviestage2.model.MovieTrailer;
import com.gilangkusumajati.popularmoviestage2.util.DBUtil;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment implements
        MovieTrailerAdapter.MovieTrailerListItemClickListener,
        MovieReviewAdapter.MovieReviewListItemClickListener,
        LoaderManager.LoaderCallbacks<MovieDetailFragment.ReviewAndTrailer> {

    private static final int LOADER_ID = 917;

    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;

    @BindView(R.id.imageview_poster)
    ImageView posterImageView;

    @BindView(R.id.textview_title)
    TextView titleTextView;

    @BindView(R.id.textview_release_date)
    TextView releaseDateTextView;

    @BindView(R.id.textview_overview)
    TextView overviewTextView;

    @BindView(R.id.textview_rating)
    TextView ratingTextView;

    @BindView(R.id.ratingbar)
    AppCompatRatingBar ratingBar;

    @BindView(R.id.textview_totalvote)
    TextView totalVoteTextView;

    @BindView(R.id.textview_no_review)
    TextView noReviewTextView;

    @BindView(R.id.textview_no_trailer)
    TextView noTrailerTextView;

    @BindView(R.id.recyclerview_trailer)
    RecyclerView trailerRecyclerView;

    @BindView(R.id.recyclerview_review)
    RecyclerView reviewRecyclerView;

    private MovieTrailerAdapter movieTrailerAdapter;
    private MovieReviewAdapter movieReviewAdapter;

    private Movie movie;
    private ReviewAndTrailer result;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();

            if (bundle.containsKey(MainActivity.MOVIE_ITEM_EXTRA)) {
                movie = bundle.getParcelable(MainActivity.MOVIE_ITEM_EXTRA);
            }
        }

        setHasOptionsMenu(true);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        trailerRecyclerView.setHasFixedSize(true);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        movieTrailerAdapter = new MovieTrailerAdapter(this);
        trailerRecyclerView.setAdapter(movieTrailerAdapter);

        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        movieReviewAdapter = new MovieReviewAdapter(this);
        reviewRecyclerView.setAdapter(movieReviewAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Picasso.with(getActivity()).load(movie.getFullPosterPath()).into(posterImageView);
        titleTextView.setText(movie.getTitle());
        releaseDateTextView.setText(movie.getReleaseDate());
        overviewTextView.setText(movie.getOverview());
        ratingTextView.setText(String.format("%s / 10", movie.getVoteAverage()));
        ratingBar.setRating((float) movie.getVoteAverage() / 2);
        totalVoteTextView.setText(String.valueOf(movie.getVoteCount()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    public void onMovieTrailerListItemClick(MovieTrailer movieTrailer) {
        openUrl(movieTrailer.getVideoTrailerUrl());
    }

    @Override
    public void onMovieReviewListItemClick(MovieReview movieReview) {
        openUrl(movieReview.getUrl());
    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailmenu, menu);

        MenuItem favoriteMenuItem = menu.findItem(R.id.action_favorite);
        favoriteMenuItem.setIcon(
                isMovieFavorited() ? R.drawable.ic_favorited : R.drawable.ic_unfavorited
        );

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getActivity().supportFinishAfterTransition();
                getActivity().onBackPressed();
                return true;
            case R.id.action_favorite:
                if (isMovieFavorited()) {
                    unfavoriteMovie(item);
                    showSnackbarMessage(getString(R.string.movie_unfavorited, movie.getOriginalTitle()));
                } else {
                    favoriteMovie(item);
                    showSnackbarMessage(getString(R.string.movie_favorited, movie.getOriginalTitle()));
                }
                return true;
            case R.id.action_share:
                shareMovie();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isMovieFavorited() {
        return movie.getRowId() != -1;
    }

    private void favoriteMovie(MenuItem item) {
        ContentResolver resolver = getActivity().getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, movie.hasVideo() ? 1 : 0);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());

        Integer[] temp = movie.getGenreIds().toArray(new Integer[movie.getGenreIds().size()]);
        String genreIds = Arrays.toString(temp);
        genreIds = genreIds.substring(1, genreIds.length() - 1);
        contentValues.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS, genreIds);

        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_ADULT, movie.getAdult() ? 1 : 0);
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        Uri uri = resolver.insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            String id = uri.getLastPathSegment();
            movie.setRowId(Integer.parseInt(id));
            item.setIcon(R.drawable.ic_favorited);
            resolver.notifyChange(uri, null);
        }

        if (result != null) {
            ContentResolver contentResolver = getActivity().getContentResolver();

            DBUtil.updateReviewsToDB(contentResolver, movie, result.reviews);
            DBUtil.updateTrailersToDB(contentResolver, movie, result.trailers);
        }
    }

    private void unfavoriteMovie(MenuItem item) {
        int id = movie.getMovieId();
        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();

        ContentResolver resolver = getActivity().getContentResolver();
        int totalMovieDeleted = resolver.delete(uri, null, null);

        if (totalMovieDeleted == 1) {
            movie.setRowId(-1);
            item.setIcon(R.drawable.ic_unfavorited);
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ContentResolver contentResolver = getActivity().getContentResolver();
                DBUtil.deleteReviewFromDB(contentResolver, movie);
                DBUtil.deleteTrailerFromDB(contentResolver, movie);

                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    private void shareMovie() {
        String mimeType = "text/plain";
        String title = movie.getTitle();
        String textToShare = movie.getOverview();

        ShareCompat.IntentBuilder
                .from(getActivity())
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(textToShare)
                .startChooser();
    }

    private void showReviews(List<MovieReview> reviews) {
        movieReviewAdapter.setItems(reviews);

        noReviewTextView.setVisibility(View.GONE);
        reviewRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showTrailers(List<MovieTrailer> trailers) {
        movieTrailerAdapter.setItems(trailers);

        noTrailerTextView.setVisibility(View.GONE);
        trailerRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<ReviewAndTrailer> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ReviewAndTrailer>(getActivity()) {

            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public ReviewAndTrailer loadInBackground() {

                ContentResolver contentResolver = getActivity().getContentResolver();
                ReviewAndTrailer result = new ReviewAndTrailer();

                // get trailers
                List<MovieTrailer> trailers;
                try {
                    Call<MovieTrailerResponse> call = MovieAPIClient.getInstance().getMovieTrailer(movie.getMovieId());
                    Response<MovieTrailerResponse> response = call.execute();

                    if (response != null && response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                        trailers = response.body().getResults();
                        if (movie.getRowId() != -1)
                            DBUtil.updateTrailersToDB(contentResolver, movie, trailers);
                    } else {
                        trailers = DBUtil.getTrailerFromDB(contentResolver, movie);
                    }
                } catch (Exception ex) {
                    trailers = DBUtil.getTrailerFromDB(contentResolver, movie);

                    if (trailers == null) {
                        result.trailerException = ex;
                    }
                }

                result.trailers = trailers;

                List<MovieReview> reviews;

                try {
                    Call<MovieReviewResponse> call = MovieAPIClient.getInstance().getMovieReview(movie.getMovieId());
                    Response<MovieReviewResponse> response = call.execute();

                    if (response != null && response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                        reviews = response.body().getResults();
                        if (movie.getRowId() != -1)
                            DBUtil.updateReviewsToDB(contentResolver, movie, reviews);
                    } else {
                        reviews = DBUtil.getReviewFromDB(contentResolver, movie);
                    }
                } catch (Exception ex) {
                    reviews = DBUtil.getReviewFromDB(contentResolver, movie);

                    if (reviews == null) {
                        result.reviewException = ex;
                    }
                }

                result.reviews = reviews;

                return result;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ReviewAndTrailer> loader, ReviewAndTrailer data) {
        List<MovieTrailer> trailers = data.trailers;
        List<MovieReview> reviews = data.reviews;

        if (trailers != null && !trailers.isEmpty()) {
            showTrailers(trailers);
        } else {
            String message = data.trailerException != null ?
                    getString(R.string.cannot_get_trailer) :
                    getString(R.string.no_trailers);
            noTrailerAvailable(message);

            if (data.trailerException != null)
                data.trailerException.printStackTrace();
        }

        if (reviews != null && !reviews.isEmpty()) {
            showReviews(reviews);
        } else {
            String message = data.reviewException != null ?
                    getString(R.string.cannot_get_review) :
                    getString(R.string.no_reviews);
            noReviewAvailable(message);

            if (data.reviewException != null)
                data.reviewException.printStackTrace();
        }

        result = data;
    }

    @Override
    public void onLoaderReset(Loader<ReviewAndTrailer> loader) {

    }

    private void noTrailerAvailable(String message) {
        noTrailerTextView.setText(message);
        trailerRecyclerView.setVisibility(View.GONE);
        noTrailerTextView.setVisibility(View.VISIBLE);
    }

    private void noReviewAvailable(String message) {
        noReviewTextView.setText(message);
        reviewRecyclerView.setVisibility(View.GONE);
        noReviewTextView.setVisibility(View.VISIBLE);
    }

    private void showSnackbarMessage(@NonNull final String message) {
        Snackbar snackbar = Snackbar.make(layoutRoot, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();

        int primaryColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        snackBarView.setBackgroundColor(primaryColor);
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    class ReviewAndTrailer {
        List<MovieReview> reviews;
        List<MovieTrailer> trailers;
        Exception reviewException;
        Exception trailerException;
    }

}
