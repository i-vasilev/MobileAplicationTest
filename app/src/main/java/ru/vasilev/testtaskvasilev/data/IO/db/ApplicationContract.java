package ru.vasilev.testtaskvasilev.data.IO.db;

import android.provider.BaseColumns;

public final class ApplicationContract {
    private ApplicationContract() {
    }

    public static class AlbumTable implements BaseColumns {
        public static final String TABLE_NAME = "album";

        public static final String TITLE = "title";
        public static final String USER_ID = "userId";
    }

    public static class PhotoTable implements BaseColumns {
        public static final String TABLE_NAME = "photo";
        public static final String TITLE = "title";
        public static final String ALBUM_ID = "albumId";
        public static final String URL = "url";
        public static final String THUMBNAIL_URL = "thumbnailUrl";
    }
}
