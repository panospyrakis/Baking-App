package com.udacity.spyrakis.bakingapp.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by pspyrakis on 9/6/18.
 */
public class RecipiesContract {

    public static final String CONTENT_AUTHORITY = "com.udacity.spyrakis.bakingapp.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class RecipeEntry implements BaseColumns {
        // table name
        public static final String TABLE_RECIPIES = "recipies";
        // columns
        public static final String _ID = "_id";
        public static final String INGREDIENTS = "ingredients";
        public static final String TITLE = "title";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_RECIPIES).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_RECIPIES;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_RECIPIES;

        // for building URIs on insertion
        public static Uri buildRecipeUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
