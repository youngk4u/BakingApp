package com.example.android.bakingapp.ui.detail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.util.DataParsingUtils;
import com.example.android.bakingapp.widget.WidgetProvider;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String RECIPE_EXTRA = "SELECTED RECIPE";
    public static final String WIDGET_INGREDIENTS = "WIDGET INGREDIENTS";
    public static final String INGREDIENT_STRING = "INGREDIENT STRING";
    public static final String RECIPE_STRING = "RECIPE STRING";
    public Recipe mRecipe;
    private boolean masterDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        mRecipe = intent.getParcelableExtra(RECIPE_EXTRA);

        SharedPreferences sharedPreferences = getSharedPreferences(WIDGET_INGREDIENTS, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (findViewById(R.id.recipe_step_container) != null) {
            masterDetail = true;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String recipeName = mRecipe.getName();
        getSupportActionBar().setTitle(recipeName);

        editor.putString(RECIPE_STRING, recipeName);

        ImageView imageView = findViewById(R.id.recipe_detail_image);

        switch (recipeName) {
            case "Nutella Pie":
                imageView.setImageResource(R.drawable.nutella);
                break;
            case "Brownies":
                imageView.setImageResource(R.drawable.brownie);
                break;
            case "Yellow Cake":
                imageView.setImageResource(R.drawable.yellow);
                break;
            case "Cheesecake":
                imageView.setImageResource(R.drawable.cheese);
                break;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recipe_step_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        recyclerView.requestFocus();
        recyclerView.setAdapter(new DetailAdapter(this, mRecipe.getSteps(), masterDetail));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        String ingredients = listOfIngredients();
        editor.putString(INGREDIENT_STRING, ingredients);
        editor.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));
        for (int appWidgetId : appWidgetIds) {
            WidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetId);
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.layout.recipe_widget);
    }

    private String listOfIngredients() {
        List<Ingredient> ingredients = mRecipe.getIngredients();
        String ingredientsText = getIngredientsDisplayText(ingredients, this);
        TextView ingredientsListView = findViewById(R.id.tv_ingredients_list);
        ingredientsListView.setText(ingredientsText);

        return ingredientsText;
    }

    public static String getIngredientsDisplayText(List<Ingredient> ingredients,
                                                   Context context) {
        if (ingredients == null) return null;

        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (Ingredient ingredient : ingredients) {
            if (first) first = false;
            else stringBuilder.append("\n");
            if (context != null) {
                stringBuilder.append("\u2022 ");
            }
            stringBuilder.append(ingredient.getQuantity());
            stringBuilder.append(" ");
            String measure = ingredient.getMeasure();
            if (measure != null && !measure.equals(DataParsingUtils.NO_MEASURE)) {
                stringBuilder.append(ingredient.getMeasure());
                stringBuilder.append(" ");
            }
            stringBuilder.append(ingredient.getIngredient());
        }
        return stringBuilder.toString();
    }
}