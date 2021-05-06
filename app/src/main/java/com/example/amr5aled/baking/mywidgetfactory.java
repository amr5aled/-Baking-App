package com.example.amr5aled.baking;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;



public class mywidgetfactory implements RemoteViewsService.RemoteViewsFactory {
    Context context ;
    ArrayList<Recipe> recipeArrayList,recipeArrayList1 ;
    SharedPreferences preferences ;
    int index ;
    mywidgetfactory(Context context)
    {
        this.context=context ;
        preferences=context.getSharedPreferences("mymostvisited",Context.MODE_PRIVATE);
        index=preferences.getInt("index",0) ;
    }
    @Override
    public void onCreate() {
        onDataSetChanged();
    }
    @Override
    public void onDataSetChanged() {
        index=preferences.getInt("index",1) ;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            new getbakingdata().execute();

        Thread timer = new Thread() {
            public void run(){
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    FirebaseCrash.report(e);
                    }
            }
        };
        timer.start();
        try {
            database db = new database(context);
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            recipeArrayList1=new ArrayList<>();
            recipeArrayList1 = db.restore_recipe();
        }catch (Exception e)
        {
            FirebaseCrash.report(e);
        }

    }
    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipeArrayList1.get(index).getIngredient_list().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews =new RemoteViews(context.getPackageName(),R.layout.listitem);
        remoteViews.setTextViewText(R.id.ingreds,recipeArrayList1.get(index).getIngredient_list().get(position).getIngredient());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    class getbakingdata extends AsyncTask<Void,Void,Void>
    {

        HttpsURLConnection httpsURLConnection = null;
        BufferedReader bufferedReader ;
        InputStream inputStream ;
        StringBuffer stringBuffer ;
        String json ;
        String strurl="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        @Override
        protected void onPreExecute() {
            recipeArrayList=new ArrayList<>();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(strurl);
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.connect();

                inputStream=httpsURLConnection.getInputStream() ;
                if(inputStream==null)
                    return null;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream)) ;
                stringBuffer =new StringBuffer();
                String line ;
                while ((line=bufferedReader.readLine())!=null)
                    stringBuffer.append(line+"\n");
                json=stringBuffer.toString() ;
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ArrayList <ingredients> list1 = new ArrayList<>();
                    ArrayList <steps> list2 = new ArrayList<>();
                    JSONObject jsonObject=jsonArray.getJSONObject(i) ;
                    JSONArray ingerd = jsonObject.getJSONArray("ingredients");
                    int id =jsonObject.getInt("id") ;
                    for (int j = 0; j < ingerd.length(); j++) {
                        JSONObject jsonObject1 = ingerd.getJSONObject(j) ;
                        list1.add(new ingredients(jsonObject1.getDouble("quantity"),jsonObject1.getString("measure"),jsonObject1.getString("ingredient"),id));
                    }
                    JSONArray st = jsonObject.getJSONArray("steps");
                    for (int j = 0; j <st.length(); j++) {

                        JSONObject jsonObject1 = st.getJSONObject(j) ;
                        list2.add( new steps(jsonObject1.getInt("id"),jsonObject1.getString("shortDescription"),jsonObject1.getString("description"),jsonObject1.getString("videoURL"),id,jsonObject1.getString("thumbnailURL")));

                    }

                    recipeArrayList.add(new Recipe(jsonObject.getInt("id"),jsonObject.getInt("servings"),jsonObject.getString("name"),jsonObject.getString("image"),list1,list2));
                }


            } catch (MalformedURLException e) {
                FirebaseCrash.report(e);
            } catch (IOException e) {
                FirebaseCrash.report(e);
            } catch (JSONException e) {
                FirebaseCrash.report(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            database db = new database(context);
            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase() ;
            db.delete_it();
            db.insert_data_recipe(recipeArrayList) ;
        }
    }
}
