package com.example.amr5aled.baking;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    SharedPreferences preferences ;
    int index ;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

            database db= new database(context) ;
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase() ;
            ArrayList<Recipe> recipes=db.restore_recipe();
            Intent intent= new Intent(context,mywidgetservice.class) ;
            views.setRemoteAdapter(R.id.widget_list,intent);
            preferences=context.getSharedPreferences("mymostvisited",Context.MODE_PRIVATE);
            index=preferences.getInt("index",0) ;
            views.setTextViewText(R.id.recipe_name,recipes.get(index).getName());
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
}

