package com.example.help_m5;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.help_m5.databinding.ActivityMainBinding;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.example.help_m5.ui.database.LoadToScreen;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
                R.id.nav_home, R.id.nav_report, R.id.nav_add_facility)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Log.d(TAG, "you have chose" + item.toString());
//                return true;
//            }
//        });
        /*
        Bundle bundle = getIntent().getExtras();
        String userName = bundle.getString("user_name");
        String userEmail = bundle.getString("user_email");
        int userType = bundle.getInt("user_type");
        TextView userNameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
        userNameView.setText(userName);
        TextView userEmailView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmail);
        userEmailView.setText(userEmail);
        if (!bundle.getString("user_icon").equals("none")) {
           Uri userIcon = Uri.parse(bundle.getString("user_icon"));
            Picasso.get().load(userIcon).into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.userIcon));
        }
        if (userType == NORMAL_USER) {
           Menu nav_Menu = navigationView.getMenu();
           nav_Menu.findItem(R.id.nav_report).setVisible(false);

        */

//        Menu nav_Menu = navigationView.getMenu();
//        nav_Menu.findItem(R.id.nav_report).setVisible(false);
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
            String userName = bundle.getString("user_name");
            String userEmail = bundle.getString("user_email");
            TextView userNameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
            userNameView.setText(userName);
            TextView userEmailView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmail);
            userEmailView.setText(userEmail);
            if (!bundle.getString("user_icon").equals("none")) {
                Uri userIcon = Uri.parse(bundle.getString("user_icon"));
                Picasso.get().load(userIcon).into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.userIcon));
            }
        }
    }

//    private void displaySharedPreferences() {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//    }

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