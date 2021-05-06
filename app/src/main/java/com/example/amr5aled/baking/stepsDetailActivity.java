package com.example.amr5aled.baking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;


public class stepsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putString(stepsDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(stepsDetailFragment.ARG_ITEM_ID));

            arguments.putString(stepsDetailFragment.ARG_ITEM_RECIPE,
                    getIntent().getStringExtra(stepsDetailFragment.ARG_ITEM_RECIPE));

            stepsDetailFragment fragment = new stepsDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.steps_detail_container,fragment).commit();

        }
    }
}
