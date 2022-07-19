package com.example.help_m5.ui.restaruants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RestaurantsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public RestaurantsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is restaurants fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }}