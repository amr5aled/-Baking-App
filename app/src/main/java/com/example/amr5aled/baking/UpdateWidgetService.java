package com.example.amr5aled.baking;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

/**
 * Created by mohamed on 19/04/18.
 */

public class UpdateWidgetService extends IntentService {

    public UpdateWidgetService() {
        super("Current Thread");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        new UpdateAsynkTask().execute();
    }
    class UpdateAsynkTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), NewAppWidget.class));
            if (appWidgetIds.length > 0)
                new NewAppWidget().onUpdate(getApplicationContext(), appWidgetManager, appWidgetIds);
            return null;
        }
    }
}
