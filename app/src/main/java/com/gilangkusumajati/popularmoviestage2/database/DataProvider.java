package com.gilangkusumajati.popularmoviestage2.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Created by Gilang Kusuma Jati on 9/1/17.
 */

public class DataProvider extends ContentProvider {

    private static final String AUTHORITY = "com.gilangkusumajati.popularmoviestage2.database.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;
    public static final int REVIEWS = 200;
    public static final int REVIEW_WITH_MOVIE_ID = 201;
    public static final int TRAILERS = 300;
    public static final int TRAILER_WITH_MOVIE_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, MovieContract.PATH_TASKS, MOVIES);
        uriMatcher.addURI(AUTHORITY, MovieContract.PATH_TASKS + "/#", MOVIE_WITH_ID);
        uriMatcher.addURI(AUTHORITY, ReviewContract.PATH_TASKS, REVIEWS);
        uriMatcher.addURI(AUTHORITY, ReviewContract.PATH_TASKS + "/#", REVIEW_WITH_MOVIE_ID);
        uriMatcher.addURI(AUTHORITY, TrailerContract.PATH_TASKS, TRAILERS);
        uriMatcher.addURI(AUTHORITY, TrailerContract.PATH_TASKS + "/#", TRAILER_WITH_MOVIE_ID);

        return uriMatcher;
    }

    private DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        String[] selectionArguments;
        String movieId;

        switch (match) {
            case MOVIES:
                retCursor =  db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_WITH_ID:
                movieId = uri.getLastPathSegment();
                selectionArguments = new String[] {movieId};

                retCursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            case REVIEW_WITH_MOVIE_ID:
                movieId = uri.getLastPathSegment();
                selectionArguments = new String[] {movieId};

                retCursor = db.query(ReviewContract.ReviewEntry.TABLE_NAME,
                        projection,
                        ReviewContract.ReviewEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            case TRAILER_WITH_MOVIE_ID:
                movieId = uri.getLastPathSegment();
                selectionArguments = new String[] {movieId};

                retCursor = db.query(TrailerContract.TrailerEntry.TABLE_NAME,
                        projection,
                        TrailerContract.TrailerEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Unsupported Operation");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;

        switch (match) {
            case MOVIES:
                id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case REVIEWS:
                id = db.insert(ReviewContract.ReviewEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(ReviewContract.ReviewEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case TRAILERS:
                id = db.insert(TrailerContract.TrailerEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(TrailerContract.TrailerEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int rowDeleted;
        String movieId;

        switch (match) {
            case MOVIE_WITH_ID:
                movieId = uri.getLastPathSegment();
                rowDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{movieId});
                break;
            case REVIEW_WITH_MOVIE_ID:
                movieId = uri.getLastPathSegment();
                rowDeleted = db.delete(ReviewContract.ReviewEntry.TABLE_NAME, ReviewContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?", new String[]{movieId});
                break;
            case TRAILER_WITH_MOVIE_ID:
                movieId = uri.getLastPathSegment();
                rowDeleted = db.delete(TrailerContract.TrailerEntry.TABLE_NAME, TrailerContract.TrailerEntry.COLUMN_MOVIE_ID + " = ?", new String[]{movieId});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Unsupported Operation");
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsInserted;
        switch (sUriMatcher.match(uri)) {

            case REVIEWS:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ReviewContract.ReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            case TRAILERS:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TrailerContract.TrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
