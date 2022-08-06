package com.example.help_m5;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.help_m5.databinding.ActivityMainBinding;
import com.example.help_m5.database.DatabaseConnection;
import com.example.help_m5.database.LoadToScreen;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_browse, R.id.nav_add_facility, R.id.nav_chat, R.id.nav_report, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();

        // Saving state of our app
        // using SharedPreferences
        SharedPreferences sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);
        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.getMenu().getItem(0).setEnabled(true);
        navigationView.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onBackPressed();
                navigationView.getMenu().getItem(0).setEnabled(false);
                return false;
            }
        });

        // Browse
        navigationView.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigationView.getMenu().getItem(0).setEnabled(true);
                return false;
            }
        });

        // Reports
        navigationView.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigationView.getMenu().getItem(0).setEnabled(true);
                return false;
            }
        });

        // Add facility
        navigationView.getMenu().getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigationView.getMenu().getItem(0).setEnabled(true);
                return false;
            }
        });

        // ChatBot
        navigationView.getMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigationView.getMenu().getItem(0).setEnabled(false);
                return false;
            }
        });

        // Settings
        navigationView.getMenu().getItem(5).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigationView.getMenu().getItem(0).setEnabled(true);
                return false;
            }
        });

        DatabaseConnection db = new DatabaseConnection();
        String info = null;
        String userInfo = "userInfo.json";
        String TAG = "MainActivity";
        if(db.isCached(getApplicationContext(), userInfo)){
            info = db.readFromJson(getApplicationContext(), userInfo);
            Log.d(TAG,"info in main is  "+info);
        }
        if(info != null){
            try {
                JSONObject user_data = new JSONObject(info);
                if(!user_data.has("account_type")){
                    Log.d(TAG, "qqq server error");
                    finish();
                }
                int userType = user_data.getInt("account_type");
                Menu processReport = navigationView.getMenu();
                int admin = 0;
                processReport.findItem(R.id.nav_report).setVisible(userType == admin);
                LoadToScreen loader = new LoadToScreen();
                loader.loadUserInfo(navigationView, user_data, MainActivity.this);
                Log.d(TAG, "userInfo finished set up");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Bundle bundle = getIntent().getExtras();
            try {
                String userName = bundle.getString("user_name");
                String userEmail = bundle.getString("user_email");
                TextView userNameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
                userNameView.setText(userName);
                TextView userEmailView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmail);
                userEmailView.setText(userEmail);
                if (!bundle.getString("user_icon").equals("none")) {
                    Uri userIcon = Uri.parse(bundle.getString("user_icon"));
                    Picasso.get().load(userIcon).into((CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.userIcon));
                } else {
                    CircleImageView circleImageView = (CircleImageView) findViewById(R.id.userIcon);
                    circleImageView.setImageResource(R.drawable.user_logo);
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Error when loading data from server",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}