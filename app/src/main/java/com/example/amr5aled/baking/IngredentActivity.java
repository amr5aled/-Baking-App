package com.example.amr5aled.baking;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class IngredentActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.ingred_rec);
        int recipe_ID = getIntent().getIntExtra(stepsDetailFragment.ARG_ITEM_RECIPE, 1);
        database db = new database(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(new IngredentAdapter(db.restore_ingred(recipe_ID)));


    }

}
