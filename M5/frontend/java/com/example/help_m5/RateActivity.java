package com.example.help_m5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private final String vm_ip = "http://20.213.243.141:8000/";
    private final static int POST = 0;

    private float rate;
    private String comment;
    private GoogleSignInAccount userAccount;
    private String userEmail;
    private Button submitButton;
    private Button cancelButton;
    private RatingBar ratingBar;
    private String facilityId;
    private int facilityType;
    private List<CharSequence> reviewers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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

                HashMap<String, String> paramsRate = new HashMap<String, String>();
                paramsRate.put("_id", userEmail);
                paramsRate.put("rateScore", String.valueOf(rate));
                paramsRate.put("facility_type", String.valueOf(facilityType));
                paramsRate.put("facility_id", facilityId);
                Log.d(TAG, paramsRate.toString());
                JsonObjectRequest requestRate = new JsonObjectRequest(Request.Method.POST, vm_ip+"user/RateFacility", new JSONObject(paramsRate),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG,response.toString());
//                                System.out.println("response is: "+response.toString());
                                Toast.makeText(getApplicationContext(), "Your review was successfully submitted!", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG,"1 onErrorResponse" + "Error: " + error.getMessage());
                                Log.d(TAG, "1 ERROR when connecting to database getSpecificFacility");

                                Toast.makeText(getApplicationContext(), "Error submitting review: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                queue.add(requestRate);

                HashMap<String, String> paramsComment = new HashMap<String, String>();
                paramsComment.put("facilityType", String.valueOf(facilityType));
                paramsComment.put("facility_id", facilityId);
                paramsComment.put("user_id", userEmail);
                paramsComment.put("replyContent", comment);
                paramsComment.put("username", userAccount.getDisplayName());
                paramsComment.put("rateScore", String.valueOf(rate));

                JsonObjectRequest requestComment = new JsonObjectRequest(Request.Method.PUT, vm_ip+"comment/add", new JSONObject(paramsComment),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG,"requestComment"+response.toString());
                                try {
                                    String result = response.getString("result");
                                    if(result.equals("already_exist")){
                                        Toast.makeText(getApplicationContext(), "You have reviewed in the past.", Toast.LENGTH_SHORT).show();
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

                HashMap<String, String> notify = new HashMap<String, String>();
                notify.put("facilityType", String.valueOf(facilityType));
                notify.put("facility_id", facilityId);
                JSONObject data = new JSONObject(notify);
                try {
                    data.put("reviewers", new JSONArray(reviewers));
                    data.put("length", reviewers.size());
                } catch (JSONException e) {
                    Log.d(TAG, "unable to add reviewers");
                    e.printStackTrace();
                }
                Log.d(TAG, "asdasdasd + "+ data);
                JsonObjectRequest requestNotify = new JsonObjectRequest(Request.Method.POST, vm_ip+"sendToDevice3", data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG,response.toString());
//                                System.out.println("response is: "+response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG,"23 onErrorResponse" + "Error: " + error.getMessage());
                                Log.d(TAG, "23 requestNotify");
                            }
                        });
                queue.add(requestNotify);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 1000);

            }
        });

        cancelButton = findViewById(R.id.cancel_button);
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