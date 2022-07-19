package com.example.help_m5.ui.entertainments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EntertainmentsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public EntertainmentsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Entertainments fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}