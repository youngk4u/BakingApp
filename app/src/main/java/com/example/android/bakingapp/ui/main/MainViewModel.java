package com.example.android.bakingapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;


public class MainViewModel extends AndroidViewModel {

    private MainLiveData mRecipesData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRecipesData = new MainLiveData(application);
    }

    public MainLiveData getRecipesData() {
        return mRecipesData;
    }
}
