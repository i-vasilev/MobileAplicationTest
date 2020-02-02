package ru.vasilev.testtaskvasilev.data.IO.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ru.vasilev.testtaskvasilev.data.Album;
import ru.vasilev.testtaskvasilev.data.Photo;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Albums.db";
    private static final int DB_VERSION = 1;

    private static final String SQL_CREATES_ALBUM =
            "CREATE TABLE " + ApplicationContract.AlbumTable.TABLE_NAME + " (" +
                    ApplicationContract.AlbumTable._ID + " INTEGER PRIMARY KEY, " +
                    ApplicationContract.AlbumTable.USER_ID + " INTEGER, " +
                    ApplicationContract.AlbumTable.TITLE + " TEXT)";
    private static final String SQL_CRESTES_PHOTO_TABLE =
            "CREATE TABLE " + ApplicationContract.PhotoTable.TABLE_NAME + " (" +
                    ApplicationContract.PhotoTable._ID + " INTEGER PRIMARY KEY, " +
                    ApplicationContract.PhotoTable.ALBUM_ID + " INTEGER, " +
                    ApplicationContract.PhotoTable.THUMBNAIL_URL + " TEXT, " +
                    ApplicationContract.PhotoTable.URL + " TEXT, " +
                    ApplicationContract.PhotoTable.TITLE + " TEXT)";


    private static final String SQL_DELETE_ALBUM =
            "DROP TABLE IF EXISTS " + ApplicationContract.AlbumTable.TABLE_NAME;
    private static final String SQL_DELETE_PHOTO =
            "DROP TABLE IF EXISTS " + ApplicationContract.PhotoTable.TABLE_NAME;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATES_ALBUM);
        db.execSQL(SQL_CRESTES_PHOTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ALBUM);
        db.execSQL(SQL_DELETE_PHOTO);
        onCreate(db);
    }

    public List<Album> selectAlbums() {
        List<Album> albums = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ApplicationContract.AlbumTable.TITLE, ApplicationContract.AlbumTable._ID};
        Cursor cursor = db.query(ApplicationContract.AlbumTable.TABLE_NAME, columns,
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Album album = new Album();
                album.setTitle(cursor.getString(0));
                album.setId(cursor.getInt(1));
                albums.add(album);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return albums;
    }

    public void insertOrReplaceAlbum(Album album, List<Photo> photos) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ApplicationContract.AlbumTable.TITLE, album.getTitle());
        contentValues.put(ApplicationContract.AlbumTable.USER_ID, album.getUserId());
        contentValues.put(ApplicationContract.AlbumTable._ID, album.getId());
        db.replace(ApplicationContract.AlbumTable.TABLE_NAME, null, contentValues);
        for (Photo photo :
                photos) {
            contentValues.clear();
            contentValues.put(ApplicationContract.PhotoTable.ALBUM_ID, album.getId());
            contentValues.put(ApplicationContract.PhotoTable.THUMBNAIL_URL, photo.getThumbnailUrl());
            contentValues.put(ApplicationContract.PhotoTable.TITLE, photo.getTitle());
            contentValues.put(ApplicationContract.PhotoTable._ID, photo.getId());
            contentValues.put(ApplicationContract.PhotoTable.URL, photo.getUrl());
            db.replace(ApplicationContract.PhotoTable.TABLE_NAME, null, contentValues);
        }
        db.close();
    }

    public void removeAlbum(Album album) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = ApplicationContract.AlbumTable._ID + " = ?";
        String[] whereArgs = {String.valueOf(album.getId())};
        db.delete(ApplicationContract.AlbumTable.TABLE_NAME, where, whereArgs);
        where = ApplicationContract.PhotoTable.ALBUM_ID + " = ?";
        db.delete(ApplicationContract.PhotoTable.TABLE_NAME, where, whereArgs);
        db.close();
    }

    public List<Photo> selectPhotos(int albumId) {
        List<Photo> photos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ApplicationContract.PhotoTable.ALBUM_ID,
                ApplicationContract.PhotoTable.THUMBNAIL_URL,
                ApplicationContract.PhotoTable.TITLE,
                ApplicationContract.PhotoTable.URL};
        String selection = ApplicationContract.PhotoTable.ALBUM_ID + " = ?";
        String[] selectionArgs = {String.valueOf(albumId)};
        Cursor cursor = db.query(ApplicationContract.PhotoTable.TABLE_NAME,
                columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Photo photo = new Photo();
                photo.setAlbumId(cursor.getInt(0));
                photo.setThumbnailUrl(cursor.getString(1));
                photo.setTitle(cursor.getString(2));
                photo.setUrl(cursor.getString(3));
                photos.add(photo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return photos;
    }

    public Album getAlbumById(int albumId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ApplicationContract.AlbumTable.TITLE, ApplicationContract.AlbumTable._ID};
        String selection = ApplicationContract.AlbumTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(albumId)};
        Cursor cursor = db.query(ApplicationContract.AlbumTable.TABLE_NAME, columns,
                selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            Album album = new Album();
            album.setTitle(cursor.getString(0));
            album.setId(cursor.getInt(1));
            return album;
        }
        cursor.close();
        db.close();
        return null;
    }
}
