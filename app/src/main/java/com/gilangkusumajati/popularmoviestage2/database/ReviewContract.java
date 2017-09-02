package com.gilangkusumajati.popularmoviestage2.database;

import android.net.Uri;
import android.provider.BaseColumns;

import static com.gilangkusumajati.popularmoviestage2.database.DataProvider.BASE_CONTENT_URI;

/**
 * Created by Gilang Kusuma Jati on 7/30/17.
 */

public interface ReviewContract {
    String PATH_TASKS = "reviews";

    final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";
    }
}
