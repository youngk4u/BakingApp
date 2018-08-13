package com.example.android.bakingapp.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.RecipeStep;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailStepActivity extends AppCompatActivity {

    public static final String STEP_INDEX = "STEP_INDEX";
    public static final String STEPS_LIST = "STEP_LIST";
    public static final String RECIPE_NAME = "RECIPE_NAME";
    private static final String INDEX_POSITION = "INDEX_POSITION";

    private ArrayList<RecipeStep> mSteps;
    private int mIndex = -1;

    @BindView(R.id.detail_toolbar) Toolbar toolbar;
    @BindView(R.id.btn_previous_step) Button previousButton;
    @BindView(R.id.btn_next_step) Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe_step);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String recipeName = getIntent().getStringExtra(RECIPE_NAME);
        setTitle(recipeName);

        mSteps = getIntent().getParcelableArrayListExtra(STEPS_LIST);
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt(INDEX_POSITION, -1);
        }
        if (mIndex == -1) {
            mIndex = getIntent().getIntExtra(STEP_INDEX, -1);
        }
        if (mIndex == 0) previousButton.setEnabled(false);
        if (mIndex == mSteps.size() - 1) nextButton.setEnabled(false);

        if (savedInstanceState == null) {
            setLayout();
            Bundle arguments = new Bundle();
            if (mIndex != -1) {
                arguments.putParcelable(DetailStepFragment.ITEM_ID, mSteps.get(mIndex));
            }
            DetailStepFragment fragment = new DetailStepFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_container, fragment)
                    .commit();
        } else {
            setLayout();
        }
    }

    private void setLayout() {
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        LinearLayout buttonBar = findViewById(R.id.button_bar);

        if (buttonBar != null) {
            ConstraintLayout layout = findViewById(R.id.layout_land_constraint);
            ConstraintSet mSet = new ConstraintSet();

            if (TextUtils.isEmpty(mSteps.get(mIndex).getVideoUrl())
                    && TextUtils.isEmpty(mSteps.get(mIndex).getThumbnailUrl())) {
                appBarLayout.setVisibility(View.VISIBLE);
                buttonBar.setVisibility(View.VISIBLE);
                mSet.clone(layout);
                mSet.connect(R.id.recipe_step_container, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                mSet.connect(R.id.recipe_step_container, ConstraintSet.TOP, R.id.app_bar, ConstraintSet.BOTTOM);
                mSet.connect(R.id.recipe_step_container, ConstraintSet.BOTTOM, R.id.button_bar, ConstraintSet.TOP);
            } else {
                appBarLayout.setVisibility(View.GONE);
                buttonBar.setVisibility(View.GONE);
                mSet.clone(layout);
                mSet.connect(R.id.recipe_step_container, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                mSet.connect(R.id.recipe_step_container, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                mSet.connect(R.id.recipe_step_container, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            }
            mSet.applyTo(layout);
        }
    }

    public void previousClicked(View view) {
        mIndex -= 1;
        if (mIndex == 0) previousButton.setEnabled(false);
        if (mIndex < mSteps.size() - 1) nextButton.setEnabled(true);
        setLayout();
        updateRecipeStepFragment();
    }

    public void nextClicked(View view) {
        mIndex += 1;
        if (mIndex == mSteps.size() - 1) nextButton.setEnabled(false);
        if (mIndex > 0) previousButton.setEnabled(true);
        setLayout();
        updateRecipeStepFragment();
    }

    private void updateRecipeStepFragment() {
        RecipeStep step = mSteps.get(mIndex);
        Bundle arguments = new Bundle();
        arguments.putParcelable(DetailStepFragment.ITEM_ID, step);
        DetailStepFragment fragment = new DetailStepFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_container, fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INDEX_POSITION, mIndex);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, DetailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
