package com.example.help_m5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.help_m5.onboard.OnBoardAdapter;

public class OnBoardActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    OnBoardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        Bundle bundle = getIntent().getExtras();
        String userName = bundle.getString("user_name");
        String userEmail = bundle.getString("user_email");
        String userIcon = bundle.getString("user_icon");
        int userType = bundle.getInt("user_type");
        int numCredits = bundle.getInt("number_of_credits");

        viewPager = findViewById(R.id.viewpager);
        adapter = new OnBoardAdapter(this, userName, userEmail, userIcon, userType, numCredits);
        viewPager.setAdapter(adapter);
        if (isOpened()) {
            Intent intent = new Intent(OnBoardActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            SharedPreferences.Editor editor=getSharedPreferences("slide", MODE_PRIVATE).edit();
            editor.putBoolean("slide",true);
            editor.apply();
        }
    }

    private boolean isOpened() {
        SharedPreferences sharedPreferences=getSharedPreferences("slide", MODE_PRIVATE);
        boolean result = sharedPreferences.getBoolean("slide",false);
        return result;
    }
}