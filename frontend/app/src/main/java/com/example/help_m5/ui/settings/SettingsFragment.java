package com.example.help_m5.ui.settings;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import com.example.help_m5.LoginActivity;
import com.example.help_m5.R;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SettingsFragment extends PreferenceFragmentCompat {

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.white_solid));
        return view;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        GoogleSignInOptions gso = new
                GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        SwitchPreference app_theme = findPreference("app_theme_sw");
        if(app_theme != null){
            app_theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference arg0, Object isVibrateOnObject) {
                    boolean isVibrateOn = (Boolean) isVibrateOnObject;
                    if (isVibrateOn) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    return true;
                }
            });
        }

        Preference button = findPreference("sign_out");
        assert button != null;
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                signOut();
                DatabaseConnection db = new DatabaseConnection();
                db.cleanAllCaches(getContext());
                return true;
            }
        });


//        SwitchPreference sign_out = findPreference("sign_out");
//        if(sign_out != null){
//            sign_out.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference arg0, Object isVibrateOnObject) {
//                    boolean isVibrateOn = (Boolean) isVibrateOnObject;
//                    if (isVibrateOn) {
//                        DatabaseConnection db = new DatabaseConnection();
//                        db.removeFile("/data/data/com.example.help_m5/files/userInfo.json");
//                        Toast.makeText(getContext(), "please exist", Toast.LENGTH_LONG).show();
//                    }
//                    return true;
//                }
//            });
//        }

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
    }

}
