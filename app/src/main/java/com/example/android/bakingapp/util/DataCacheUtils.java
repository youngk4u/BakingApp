package com.example.android.bakingapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DataCacheUtils {
    private static final String RECIPE_KEY = "RECIPES";

    public static void cacheRecipeJson(String json, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(RECIPE_KEY, json);
        editor.apply();
    }

    public static String readRecipeJson(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(RECIPE_KEY, null);
    }
}