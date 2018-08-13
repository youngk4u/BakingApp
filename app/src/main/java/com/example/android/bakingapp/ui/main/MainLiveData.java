package com.example.android.bakingapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import com.example.android.bakingapp.model.Recipe;

import java.util.List;


public class MainLiveData extends LiveData<List<Recipe>> implements MainAsyncTask.Callback {

    private Context mContext;

    MainLiveData(Context context) {
        mContext = context;
        new MainAsyncTask(this).execute(null, null);
    }

    @Override
    public void onFetchCompleted(List<Recipe> recipes) {
        setValue(recipes);
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
