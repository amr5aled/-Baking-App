package com.example.amr5aled.baking;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class stepsListActivity extends AppCompatActivity {


    ArrayList<steps> stepsArrayList ;
    public static boolean mTwoPane;
    int index,recipe_ID;
    String desc ;
    public View view;
    ArrayList<ingredients>ingredientsArrayList ;
    SimpleItemRecyclerViewAdapter adapter ;
   // ListView listView;
    RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent =getIntent();
        index=intent.getIntExtra("index",1) ;
        recipe_ID=intent.getIntExtra("recipe_ID",1) ;
        desc=intent.getStringExtra("desc");
        recyclerView = (RecyclerView) findViewById(R.id.steps_list);
       // listView= (ListView) findViewById(R.id.ingreds_list);
        database db = new database(getApplicationContext()) ;
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase() ;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.steps_detail_container) != null) {
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        database db = new database(getApplicationContext()) ;
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase() ;
        stepsArrayList=db.restore_steps(recipe_ID) ;
         adapter = new SimpleItemRecyclerViewAdapter(stepsArrayList);
       recyclerView.setAdapter(adapter);

    }



    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<steps> mValues;

        public SimpleItemRecyclerViewAdapter(List<steps> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.steps_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mIdView.setText(""+mValues.get(position).getId());
            holder.mContentView.setText(mValues.get(position).getDescription());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index=position;
                   selectFragment(getApplicationContext()) ;
                }
            });
        }
        private void selectFragment(Context c) {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(stepsDetailFragment.ARG_ITEM_ID,""+mValues.get(index).getId() );
                arguments.putString(stepsDetailFragment.ARG_ITEM_RECIPE,""+recipe_ID );
                stepsDetailFragment fragment = new stepsDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.steps_detail_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(getApplicationContext(), stepsDetailActivity.class);
                intent.putExtra(stepsDetailFragment.ARG_ITEM_ID,""+ mValues.get(index).getId());
                intent.putExtra(stepsDetailFragment.ARG_ITEM_RECIPE,""+recipe_ID );
                startActivity(intent);
            }
        }
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }


        }
    }


}
