package com.gilangkusumajati.popularmoviestage2.database;

import android.net.Uri;
import android.provider.BaseColumns;

import static com.gilangkusumajati.popularmoviestage2.database.DataProvider.BASE_CONTENT_URI;

/**
 * Created by Gilang Kusuma Jati on 7/30/17.
 */

public interface TrailerContract {

    String PATH_TASKS = "trailers";

    final class TrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "trailers";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_ISO_6391 = "iso6391";
        public static final String COLUMN_ISO_31661 = "iso31661";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";
    }
}
