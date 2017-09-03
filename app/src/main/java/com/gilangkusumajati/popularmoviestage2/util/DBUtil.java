package com.gilangkusumajati.popularmoviestage2.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gilangkusumajati.popularmoviestage2.database.MovieContract;
import com.gilangkusumajati.popularmoviestage2.database.ReviewContract;
import com.gilangkusumajati.popularmoviestage2.database.TrailerContract;
import com.gilangkusumajati.popularmoviestage2.model.Movie;
import com.gilangkusumajati.popularmoviestage2.model.MovieReview;
import com.gilangkusumajati.popularmoviestage2.model.MovieTrailer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Gilang Kusuma Jati on 9/2/17.
 */

public class DBUtil {

    private static final String TAG = DBUtil.class.getSimpleName();

    public static List<Movie> getFavoritedMovies(@NonNull final ContentResolver contentResolver) {

        Cursor cursor = contentResolver.query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return null;

        int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
        int movieIdIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        int voteCountIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT);
        int videoIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VIDEO);
        int voteAverageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
        int titleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int popularityIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY);
        int posterPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        int originalLanguageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE);
        int originalTitleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
        int genreIdsIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_GENRE_IDS);
        int backdropPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH);
        int adultIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ADULT);
        int overviewIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        int releaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

        List<Movie> movies = new ArrayList<>();

        while (cursor.moveToNext()) {
            Movie movie = new Movie();

            movie.setRowId(cursor.getInt(idIndex));
            movie.setMovieId(cursor.getInt(movieIdIndex));
            movie.setVoteCount(cursor.getInt(voteCountIndex));
            movie.setHasVideo(cursor.getInt(videoIndex) == 1);
            movie.setVoteAverage(cursor.getDouble(voteAverageIndex));
            movie.setTitle(cursor.getString(titleIndex));
            movie.setPopularity(cursor.getDouble(popularityIndex));
            movie.setPosterPath(cursor.getString(posterPathIndex));
            movie.setOriginalLanguage(cursor.getString(originalLanguageIndex));
            movie.setOriginalTitle(cursor.getString(originalTitleIndex));

            String genreIds = cursor.getString(genreIdsIndex);
            String[] temp = genreIds.split(",");
            List<Integer> list = new ArrayList<>();
            for (String s : temp) {
                list.add(Integer.parseInt(s.trim()));
            }
            movie.setGenreIds(list);

            movie.setBackdropPath(cursor.getString(backdropPathIndex));
            movie.setAdult(cursor.getInt(adultIndex) == 1);
            movie.setOverview(cursor.getString(overviewIndex));
            movie.setReleaseDate(cursor.getString(releaseDateIndex));

            movies.add(movie);
        }

        cursor.close();

        return movies;
    }

    public static List<MovieTrailer> getTrailerFromDB(@NonNull final ContentResolver resolver, @NonNull final Movie movie) {
        Uri uri = TrailerContract.TrailerEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getMovieId())).build();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        List<MovieTrailer> trailers = null;

        try {
            if (cursor != null && cursor.getCount() > 0) {

                int idIndex = cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_TRAILER_ID);
                int iso6391Index = cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_ISO_6391);
                int iso31661Index = cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_ISO_31661);
                int keyIndex = cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_KEY);
                int nameIndex = cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_NAME);
                int siteIndex = cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_SITE);
                int sizeIndex = cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_SIZE);
                int typeIndex = cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_TYPE);

                trailers = new ArrayList<>();

                while (cursor.moveToNext()) {
                    MovieTrailer trailer = new MovieTrailer();

                    trailer.setTrailerId(cursor.getString(idIndex));
                    trailer.setIso6391(cursor.getString(iso6391Index));
                    trailer.setIso31661(cursor.getString(iso31661Index));
                    trailer.setKey(cursor.getString(keyIndex));
                    trailer.setName(cursor.getString(nameIndex));
                    trailer.setSite(cursor.getString(siteIndex));
                    trailer.setSize(cursor.getInt(sizeIndex));
                    trailer.setType(cursor.getString(typeIndex));

                    trailers.add(trailer);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return trailers;
    }

    public static void deleteTrailerFromDB(@NonNull final ContentResolver resolver, @NonNull final Movie movie) {

        Uri deleteUri = TrailerContract.TrailerEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getMovieId())).build();

        // delete previous reviews
        int trailerDeleted = resolver.delete(deleteUri, null, null);
        Log.d(TAG, "Trailer Deleted: " + trailerDeleted);
    }

    public static void updateTrailersToDB(@NonNull final ContentResolver resolver,
                                          @NonNull final Movie movie,
                                          @NonNull final List<MovieTrailer> trailers) {
        Log.d(TAG, "Updating trailers in DB");
        Uri deleteUri = TrailerContract.TrailerEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getMovieId())).build();

        // delete previous trailers
        int trailerDeleted = resolver.delete(deleteUri, null, null);
        Log.d(TAG, "Trailer Deleted: " + trailerDeleted);

        //return null if empty
        if (trailers == null || trailers.isEmpty())
            return;

        ContentValues[] values = new ContentValues[trailers.size()];
        for (int i = 0; i < trailers.size(); i++) {

            MovieTrailer trailer = trailers.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(TrailerContract.TrailerEntry.COLUMN_MOVIE_ID, movie.getMovieId());
            contentValues.put(TrailerContract.TrailerEntry.COLUMN_TRAILER_ID, trailer.getTrailerId());
            contentValues.put(TrailerContract.TrailerEntry.COLUMN_ISO_6391, trailer.getIso6391());
            contentValues.put(TrailerContract.TrailerEntry.COLUMN_ISO_31661, trailer.getIso31661());
            contentValues.put(TrailerContract.TrailerEntry.COLUMN_KEY, trailer.getKey());
            contentValues.put(TrailerContract.TrailerEntry.COLUMN_NAME, trailer.getName());
            contentValues.put(TrailerContract.TrailerEntry.COLUMN_SITE, trailer.getSite());
            contentValues.put(TrailerContract.TrailerEntry.COLUMN_SIZE, trailer.getSize());
            contentValues.put(TrailerContract.TrailerEntry.COLUMN_TYPE, trailer.getType());

            values[i] = contentValues;
        }

        // insert new data
        int reviewInserted = resolver.bulkInsert(TrailerContract.TrailerEntry.CONTENT_URI, values);
        Log.d(TAG, "Trailer Inserted : " + reviewInserted);
    }

    public static List<MovieReview> getReviewFromDB(@NonNull final ContentResolver resolver,
                                                    @NonNull final Movie movie) {

        Uri uri = ReviewContract.ReviewEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getMovieId())).build();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        List<MovieReview> reviews = null;

        try {
            if (cursor != null && cursor.getCount() > 0) {

                int idIndex = cursor.getColumnIndex(ReviewContract.ReviewEntry.COLUMN_REVIEW_ID);
                int authorIndex = cursor.getColumnIndex(ReviewContract.ReviewEntry.COLUMN_AUTHOR);
                int contentIndex = cursor.getColumnIndex(ReviewContract.ReviewEntry.COLUMN_CONTENT);
                int urlIndex = cursor.getColumnIndex(ReviewContract.ReviewEntry.COLUMN_URL);

                reviews = new ArrayList<>();

                while (cursor.moveToNext()) {
                    MovieReview review = new MovieReview();

                    review.setId(cursor.getString(idIndex));
                    review.setAuthor(cursor.getString(authorIndex));
                    review.setContent(cursor.getString(contentIndex));
                    review.setUrl(cursor.getString(urlIndex));

                    reviews.add(review);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return reviews;
    }

    public static void deleteReviewFromDB(@NonNull final ContentResolver resolver, @NonNull final Movie movie) {
        Uri deleteUri = ReviewContract.ReviewEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getMovieId())).build();

        // delete previous reviews
        int reviewDeleted = resolver.delete(deleteUri, null, null);
        Log.d(TAG, "Review Deleted: " + reviewDeleted);
    }

    public static void updateReviewsToDB(@NonNull final ContentResolver resolver,
                                         @NonNull final Movie movie,
                                         @NonNull final List<MovieReview> reviews) {
        Log.d(TAG, "Updating reviews in DB");
        Uri deleteUri = ReviewContract.ReviewEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getMovieId())).build();

        // delete previous reviews
        int reviewDeleted = resolver.delete(deleteUri, null, null);
        Log.d(TAG, "Review Deleted: " + reviewDeleted);

        if (reviews == null || reviews.isEmpty())
            return;

        ContentValues[] values = new ContentValues[reviews.size()];
        for (int i = 0; i < reviews.size(); i++) {

            MovieReview review = reviews.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(ReviewContract.ReviewEntry.COLUMN_MOVIE_ID, movie.getMovieId());
            contentValues.put(ReviewContract.ReviewEntry.COLUMN_REVIEW_ID, review.getId());
            contentValues.put(ReviewContract.ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
            contentValues.put(ReviewContract.ReviewEntry.COLUMN_CONTENT, review.getContent());
            contentValues.put(ReviewContract.ReviewEntry.COLUMN_URL, review.getUrl());

            values[i] = contentValues;
        }

        // insert new data
        int reviewInserted = resolver.bulkInsert(ReviewContract.ReviewEntry.CONTENT_URI, values);
        Log.d(TAG, "Review Inserted : " + reviewInserted);
    }

    public static Uri favoriteMovie(@NonNull final ContentResolver resolver, @NonNull final Movie movie) {
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

        return resolver.insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
    }

    public static int unfavoriteMovie(@NonNull final ContentResolver resolver, @NonNull final Movie movie) {
        int id = movie.getMovieId();
        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        return resolver.delete(uri, null, null);
    }
}
