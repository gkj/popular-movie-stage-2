package com.gilangkusumajati.popularmoviestage2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gilangkusumajati.popularmoviestage2.database.MovieContract.MovieEntry;
import com.gilangkusumajati.popularmoviestage2.database.ReviewContract.ReviewEntry;
import com.gilangkusumajati.popularmoviestage2.database.TrailerContract.TrailerEntry;

/**
 * Created by Gilang Kusuma Jati on 7/30/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "moviesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    // Constructor
    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_MOVIES = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MovieEntry.COLUMN_VIDEO + " INTEGER, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MovieEntry.COLUMN_TITLE + " TEXT, " +
                MovieEntry.COLUMN_POPULARITY + " REAL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieEntry.COLUMN_GENRE_IDS + " TEXT, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MovieEntry.COLUMN_ADULT + " INTEGER, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT" +
                ");";

        db.execSQL(CREATE_TABLE_MOVIES);

        final String CREATE_TABLE_REVIEWS = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                ReviewEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_AUTHOR + " TEXT, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT, " +
                ReviewEntry.COLUMN_URL + " TEXT" +
                ");";

        db.execSQL(CREATE_TABLE_REVIEWS);

        final String CREATE_TABLE_TRAILERS = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                TrailerEntry.COLUMN_TRAILER_ID + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_ISO_6391 + " TEXT, " +
                TrailerEntry.COLUMN_ISO_31661 + " TEXT, " +
                TrailerEntry.COLUMN_KEY + " TEXT, " +
                TrailerEntry.COLUMN_NAME + " TEXT, " +
                TrailerEntry.COLUMN_SITE + " TEXT, " +
                TrailerEntry.COLUMN_SIZE + " INTEGER, " +
                TrailerEntry.COLUMN_TYPE + " TEXT" +
                ");";

        db.execSQL(CREATE_TABLE_TRAILERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        onCreate(db);
    }
}
