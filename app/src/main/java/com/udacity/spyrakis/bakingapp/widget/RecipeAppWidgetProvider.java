package com.udacity.spyrakis.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.spyrakis.bakingapp.R;
import com.udacity.spyrakis.bakingapp.provider.RecipiesContract;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppWidgetProvider extends AppWidgetProvider {


    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, RecipeAppWidgetProvider.class));
        context.sendBroadcast(intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.recipe_app_widget_provider
            );

            Uri uri = RecipiesContract.RecipeEntry.CONTENT_URI;
            Cursor cursor = context.getContentResolver().query(uri,
                    null,
                    null,
                    null,
                    RecipiesContract.RecipeEntry._ID + " ASC");
            int titlesIndex = cursor.getColumnIndex(RecipiesContract.RecipeEntry.TITLE);

//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget_provider);
            cursor.moveToFirst();
            views.setTextViewText(R.id.widgetTitleLabel, cursor.getString(titlesIndex));

            Intent intent = new Intent(context, RecipeRemoteViewsService.class);
            views.setRemoteAdapter(R.id.widgetListView, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, RecipeAppWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widgetListView);
        }
        super.onReceive(context, intent);
    }

}

