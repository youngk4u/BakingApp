package com.example.android.bakingapp.ui.main;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.util.DataCacheUtils;
import com.example.android.bakingapp.util.NetworkUtils;
import com.example.android.bakingapp.util.DataParsingUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainAsyncTask extends AsyncTask<Void, Void, List<Recipe>> {

    private Callback mCallback;

    MainAsyncTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected List<Recipe> doInBackground(Void... voids) {
        URL recipesUrl = NetworkUtils.getRecipesUrl();
        String response = null;
        try {
            response = NetworkUtils.getResponseFromHttpUrl(recipesUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response == null) {
            response = DataCacheUtils.readRecipeJson(mCallback.getContext());
        } else {
            DataCacheUtils.cacheRecipeJson(response, mCallback.getContext());
        }
        return DataParsingUtils.parseRecipes(response);
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        mCallback.onFetchCompleted(recipes);
    }

    public interface Callback {
        void onFetchCompleted(List<Recipe> recipes);
        Context getContext();
    }
}

