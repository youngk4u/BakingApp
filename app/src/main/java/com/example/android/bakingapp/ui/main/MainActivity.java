package com.example.android.bakingapp.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.ui.detail.DetailActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainAdapter.OnClickHandler {

    public static final String RECIPE_EXTRA = "SELECTED RECIPE";
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private ProgressBar mProgressBar;

    // Column calculator when rotated
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recipeRV = findViewById(R.id.recyclerview_recipe_tab);
        mProgressBar = findViewById(R.id.pb_loading_indicator);

        // Tablet size and phone size configuration
        RecyclerView.LayoutManager layoutManager;
        if (recipeRV != null) {
            mRecyclerView = recipeRV;
            layoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        } else {
            mRecyclerView = findViewById(R.id.recyclerview_recipe_phone);
            layoutManager = new LinearLayoutManager(this);
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MainAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        showLoading();
        setUpViewModel();
    }

    private void setUpViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getRecipesData().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes == null) {
                    showLoading();
                } else {
                    mAdapter.setRecipes(recipes);
                    showView();
                }
            }
        });
    }

    private void showView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(RECIPE_EXTRA, recipe);
        startActivity(intent);
    }
}
