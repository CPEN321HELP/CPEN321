package com.example.help_m5;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.help_m5.databinding.ActivityMainBinding;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final int NORMAL_USER = 0;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DatabaseConnection db;
    private String TAG = "MainActivity";
    private String userInfo = "userInfo.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

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
        if(db.isCached(getApplicationContext(),userInfo)){
            info = db.readFromJson(getApplicationContext(), userInfo);
            Log.d(TAG,"info in main is  "+info);
        }
        if(info != null){
            try {
                JSONObject user_data = new JSONObject(info);
                String userName = user_data.getString("user_name");
                String userEmail = user_data.getString("user_email");
                String userLogo = user_data.getString("user_icon");
                TextView userNameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
                userNameView.setText(userName);
                TextView userEmailView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmail);
                userEmailView.setText(userEmail);
                if (!userLogo.equals("none")) {
                    Uri userIcon = Uri.parse(userLogo);
                    Picasso.get().load(userIcon).into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.userIcon));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "finish set up");
        }else {
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

    private void displaySharedPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
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