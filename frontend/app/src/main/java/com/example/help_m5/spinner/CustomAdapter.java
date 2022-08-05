package com.example.help_m5.spinner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.help_m5.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int spinnerType;
    int[] flags;
    String[] countryNames;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, int[] flags, String[] countryNames, int spinnerType) {
        this.context = applicationContext;
        this.flags = flags;
        this.countryNames = countryNames;
        this.spinnerType = spinnerType;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int i, View viewOriginal, ViewGroup viewGroup) {
        View view = inflter.inflate(R.layout.custom_spinner_items, null);
        ImageView icon = view.findViewById(R.id.spinnerImageView);
        TextView names = view.findViewById(R.id.spinnerTextView);
        icon.setImageResource(flags[i]);
        names.setText(countryNames[i]);
        if (spinnerType == 1) {
            names.setTextColor(Color.parseColor("#AAFFFFFF"));
        }
        return view;
    }
}