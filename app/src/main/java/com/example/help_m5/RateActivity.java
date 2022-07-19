package com.example.help_m5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class RateActivity extends AppCompatActivity {

    private static final String TAG = "RateActivity";
    private String vm_ip;
    private final static int POST = 0;

    private float rate;
    private String comment;
    private GoogleSignInAccount userAccount;
    private String userEmail;
    private Button submitButton;
//    private RatingBar ratingBar;
    private String facilityId;
    private int facilityType;
    private List<CharSequence> reviewers;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        vm_ip = getString(R.string.azure_ip);
        Bundle bundle = getIntent().getExtras();
        facilityId = bundle.getString("facility_id");
        facilityType = bundle.getInt("facility_type");
        reviewers = bundle.getCharSequenceArrayList("reviewers");
        Log.d(TAG, facilityId);
        Log.d(TAG, ""+facilityType);
        Log.d(TAG, ""+reviewers.toString());

        userAccount = GoogleSignIn.getLastSignedInAccount(this);
        userEmail = userAccount.getEmail();

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                submitButton.setEnabled(true);
                submitButton.setTextColor(Color.parseColor("#dbba00"));
                rate = rating;
            }
        });
        if (facilityType == POST) {
            ratingBar.setVisibility(View.INVISIBLE);
            TextView textTitle = (TextView) findViewById(R.id.RateFacilityTitle);
            textTitle.setText("Comment on a Post");

            TextView textView = (TextView) findViewById(R.id.RateFacilityDescription);
            textView.setText("Please leave your comments\nbelow");
        }

        EditText editText = findViewById(R.id.editTextTextMultiLine);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {comment = s.toString();}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submitButton.setEnabled(true);
                submitButton.setTextColor(Color.parseColor("#dbba00"));
                comment = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                comment = s.toString();
            }
        });

        submitButton = findViewById(R.id.submit_button);
        submitButton.setEnabled(false);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(RateActivity.this);
                queue.start();

                HashMap<String, String> paramsComment = new HashMap<String, String>();
                paramsComment.put("facilityType", String.valueOf(facilityType));
                paramsComment.put("facility_id", facilityId);
                paramsComment.put("user_id", userEmail);
                paramsComment.put("replyContent", comment);
                paramsComment.put("username", userAccount.getDisplayName());
                paramsComment.put("rateScore", String.valueOf(rate));
                //for add credit
                paramsComment.put("AdditionType", "comment");
                paramsComment.put("upUserId", userEmail);
                paramsComment.put("downUserId",  "");
                //for notify other reviewers
                JSONObject data = new JSONObject(paramsComment);
                try {
                    data.put("reviewers", new JSONArray(reviewers));
                    data.put("length", reviewers.size());
                } catch (JSONException e) {
                    Log.d(TAG, "unable to add reviewers");
                    e.printStackTrace();
                }
                Log.d(TAG, "data in requestComment: " + data);

                JsonObjectRequest requestComment = new JsonObjectRequest(Request.Method.POST, vm_ip+"comment/add", data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG,"requestComment"+response.toString());
                                try {
                                    String result = response.getString("result");
                                    if(result.equals("already_exist")){
                                        Toast.makeText(getApplicationContext(), "You have reviewed in the past.", Toast.LENGTH_SHORT).show();

                                    }else{
                                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG,"2 onErrorResponse" + "Error: " + error.getMessage());
                                Log.d(TAG, "2 ERROR when connecting to database getSpecificFacility");
                            }
                        });
                queue.add(requestComment);

//                Handler handler1 = new Handler();
//                handler1.postDelayed(new Runnable() {
//                    public void run() {
//                        Log.d(TAG,"2 second");
//                        NavigationView navigationView =findViewById(R.id.nav_view);
//                        Log.d(TAG, "here in rate s " + (navigationView == null));
//
//                        DatabaseConnection db = new DatabaseConnection();
//                        db.updateUserInfo(navigationView, getApplicationContext(), userEmail, RateActivity.this,true);
//                    }
//                }, 2000);

                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 1000);

            }
        });

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}