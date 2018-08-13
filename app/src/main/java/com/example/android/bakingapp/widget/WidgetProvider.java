package com.example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.main.MainActivity;

public class WidgetProvider extends AppWidgetProvider {

    public static final String WIDGET_INGREDIENTS = "WIDGET INGREDIENTS";
    public static final String INGREDIENT_STRING = "INGREDIENT STRING";
    public static final String RECIPE_STRING = "RECIPE STRING";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {

        CharSequence titleText = context.getString(R.string.recipe_title_text);
        CharSequence recipeText = context.getString(R.string.recipe_body_text);

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(WIDGET_INGREDIENTS,0);

        if (sharedPreferences.contains(INGREDIENT_STRING)) {
            titleText = sharedPreferences.getString(RECIPE_STRING, titleText.toString());
            recipeText = sharedPreferences.getString(INGREDIENT_STRING, recipeText.toString());
        }

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.recipe_widget);
        views.setTextViewText(R.id.widget_recipe_title, titleText);
        views.setTextViewText(R.id.widget_recipe_text, recipeText);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_recipe_title, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }

}
