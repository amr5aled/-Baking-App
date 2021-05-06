package com.example.amr5aled.baking;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

public class MainActivity extends AppCompatActivity implements RecipesAdapter.OnItemClick{
    private static final String RECIPES_KEY = "recipes_key";
    SharedPreferences.Editor editor;
    ArrayList<Recipe> recipeArrayList;
    RecyclerView recipesRecyclerView;
    RecipesAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipesRecyclerView = (RecyclerView) findViewById(R.id.recycler_recipes);
        recipesRecyclerView.setHasFixedSize(true);
        recipeArrayList = new ArrayList<>();

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            recipesRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        else {
            recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_data();
            }
        });

        if(savedInstanceState == null)
            get_data();
        else
            recipeArrayList = savedInstanceState.getParcelableArrayList(RECIPES_KEY);

        adapter = new RecipesAdapter(this,recipeArrayList);
        adapter.setOnItemClick(this);
        recipesRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPES_KEY,recipeArrayList);
    }

    private void get_data() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            new getbakingdata().execute();
        } else {
            database db = new database(getApplicationContext());
            recipeArrayList = db.restore_recipe();
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(int position) {
        editor = getSharedPreferences("mymostvisited", MODE_PRIVATE).edit();
        editor.putInt("index", position);
        editor.apply();
        Intent updateService = new Intent(this,UpdateWidgetService.class);
        startService(updateService);
        Intent intent = new Intent(getApplicationContext(), stepsListActivity.class);
        intent.putExtra("recipe_ID", position + 1);
        startActivity(intent);
    }

    class getbakingdata extends AsyncTask<Void, Void, Void> {

        HttpsURLConnection httpsURLConnection = null;
        BufferedReader bufferedReader;
        InputStream inputStream;
        StringBuffer stringBuffer;
        String json;
        String strurl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        @Override
        protected void onPreExecute() {
            recipeArrayList.clear();
            swipeRefreshLayout.setRefreshing(true);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(strurl);
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.connect();

                inputStream = httpsURLConnection.getInputStream();
                if (inputStream == null)
                    return null;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuffer.append(line + "\n");

                json = stringBuffer.toString();


                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {


                    ArrayList<ingredients> list1 = new ArrayList<>();
                    ArrayList<steps> list2 = new ArrayList<>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONArray ingerd = jsonObject.getJSONArray("ingredients");
                    int id = jsonObject.getInt("id");
                    for (int j = 0; j < ingerd.length(); j++) {
                        JSONObject jsonObject1 = ingerd.getJSONObject(j);
                        list1.add(new ingredients(jsonObject1.getDouble("quantity"), jsonObject1.getString("measure"), jsonObject1.getString("ingredient"), id));
                    }
                    JSONArray st = jsonObject.getJSONArray("steps");
                    for (int j = 0; j < st.length(); j++) {

                        JSONObject jsonObject1 = st.getJSONObject(j);
                        list2.add(new steps(jsonObject1.getInt("id"), jsonObject1.getString("shortDescription"), jsonObject1.getString("description"), jsonObject1.getString("videoURL"), id, jsonObject1.getString("thumbnailURL")));

                    }

                    recipeArrayList.add(new Recipe(jsonObject.getInt("id"), jsonObject.getInt("servings"), jsonObject.getString("name"), jsonObject.getString("image"), list1, list2));
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
            swipeRefreshLayout.setRefreshing(false);
            try {
                if (!recipeArrayList.isEmpty()) {
                    adapter.notifyDataSetChanged();
                    database db = new database(getApplicationContext());
                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                    db.delete_it();
                    db.insert_data_recipe(recipeArrayList);
                } else {
                    database db = new database(getApplicationContext());
                    SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
                    recipeArrayList = db.restore_recipe();
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                FirebaseCrash.report(e);
            }
        }
    }
}


