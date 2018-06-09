package com.udacity.spyrakis.bakingapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by pspyrakis on 9/6/18.
 */
public class RecipeDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = RecipeDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 12;

    public RecipeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " +
                RecipiesContract.RecipeEntry.TABLE_RECIPIES + "(" + RecipiesContract.RecipeEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipiesContract.RecipeEntry.TITLE +
                " TEXT NOT NULL, " +
                RecipiesContract.RecipeEntry.INGREDIENTS +
                " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
    }

    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipiesContract.RecipeEntry.TABLE_RECIPIES);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                RecipiesContract.RecipeEntry.TABLE_RECIPIES + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}
