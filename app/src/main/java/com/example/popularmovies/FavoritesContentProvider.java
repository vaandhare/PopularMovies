package com.example.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class FavoritesContentProvider extends ContentProvider {
    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    FavoritesDbHelper mDBHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoritesContract.AUTHORITY,
                FavoritesContract.FavoritesEntry.TABLE_NAME, FAVORITES);
        uriMatcher.addURI(FavoritesContract.AUTHORITY,
                FavoritesContract.FavoritesEntry.TABLE_NAME + "/#", FAVORITES_WITH_ID);
        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDBHelper = new FavoritesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase db = mDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        if (match == FAVORITES) {
            returnCursor = db.query(FavoritesContract.FavoritesEntry.TABLE_NAME,
                    strings,
                    s,
                    strings1,
                    null,
                    null,
                    s1);
        } else {
            throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        returnCursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return returnCursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        if (match == FAVORITES) {
            long id = db.insert(FavoritesContract.FavoritesEntry.TABLE_NAME,
                    null, contentValues);
            if (id > 0) {
                returnUri = ContentUris.withAppendedId(FavoritesContract.CONTENT_URI, id);

            } else {
                throw new SQLException("Failed to insert" + uri);
            }
        } else {
            throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;
        //passing "1" as selection will clear the data base
        if (null == selection) selection = "1";
        if (sUriMatcher.match(uri) == FAVORITES) {
            numRowsDeleted = mDBHelper.getWritableDatabase().delete(
                    FavoritesContract.FavoritesEntry.TABLE_NAME,
                    selection,
                    selectionArgs);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsDeleted != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}