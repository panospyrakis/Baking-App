package com.udacity.spyrakis.bakingapp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by pspyrakis on 9/6/18.
 */
public class RecipeProvider extends ContentProvider {
    private static final String LOG_TAG = RecipeProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RecipeDBHelper mOpenHelper;

    // Codes for the UriMatcher //////
    private static final int RECIPE = 100;
    private static final int RECIPE_WITH_ID = 200;
    ////////

    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipiesContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, RecipiesContract.RecipeEntry.TABLE_RECIPIES, RECIPE);
        matcher.addURI(authority, RecipiesContract.RecipeEntry.TABLE_RECIPIES + "/#", RECIPE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate(){
        mOpenHelper = new RecipeDBHelper(getContext());

        return true;
    }

    @Override
    public String getType(@NonNull Uri uri){
        final int match = sUriMatcher.match(uri);

        switch (match){
            case RECIPE:{
                return RecipiesContract.RecipeEntry.CONTENT_DIR_TYPE;
            }
            case RECIPE_WITH_ID:{
                return RecipiesContract.RecipeEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            // All recipes selected
            case RECIPE:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipiesContract.RecipeEntry.TABLE_RECIPIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            // Individual recipe based on Id selected
            case RECIPE_WITH_ID:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipiesContract.RecipeEntry.TABLE_RECIPIES,
                        projection,
                        RecipiesContract.RecipeEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            default:{
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case RECIPE: {
                long _id = db.insert(RecipiesContract.RecipeEntry.TABLE_RECIPIES, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = RecipiesContract.RecipeEntry.buildRecipeUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch(match){
            case RECIPE:
                numDeleted = db.delete(
                        RecipiesContract.RecipeEntry.TABLE_RECIPIES, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        RecipiesContract.RecipeEntry.TABLE_RECIPIES + "'");
                break;
            case RECIPE_WITH_ID:
                numDeleted = db.delete(RecipiesContract.RecipeEntry.TABLE_RECIPIES,
                        RecipiesContract.RecipeEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        RecipiesContract.RecipeEntry.TABLE_RECIPIES + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch(match){
            case RECIPE:
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                int numInserted = 0;
                try{
                    for(ContentValues value : values){
                        if (value == null){
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try{
                            _id = db.insertOrThrow(RecipiesContract.RecipeEntry.TABLE_RECIPIES,
                                    null, value);
                        }catch(SQLiteConstraintException e) {
                            Log.w(LOG_TAG, "Attempting to insert " +
                                    value.getAsString(
                                            RecipiesContract.RecipeEntry._ID)
                                    + " but value is already in database.");
                        }
                        if (_id != -1){
                            numInserted++;
                        }
                    }
                    if(numInserted > 0){
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                if (numInserted > 0){
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(sUriMatcher.match(uri)){
            case RECIPE:{
                numUpdated = db.update(RecipiesContract.RecipeEntry.TABLE_RECIPIES,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case RECIPE_WITH_ID: {
                numUpdated = db.update(RecipiesContract.RecipeEntry.TABLE_RECIPIES,
                        contentValues,
                        RecipiesContract.RecipeEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

}