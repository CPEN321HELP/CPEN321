package com.example.help_m5.ui.faclity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.help_m5.R;
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

    private GoogleSignInAccount userAccount;

    private LinearLayout rateLayout;
    private String userEmail;
    private String facilityId;
    private int facilityType;
    private List<CharSequence> reviewers;
    private boolean isRating;
    private Button cancelButton;
    private Button submitButton;
    private EditText editText;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        isRating = false;
        vm_ip = getString(R.string.azure_ip);
        Bundle bundle = getIntent().getExtras();
        facilityId = bundle.getString("facility_id");
        facilityType = bundle.getInt("facility_type");
        reviewers = bundle.getCharSequenceArrayList("reviewers");
        Log.d(TAG, facilityId);
        Log.d(TAG, ""+facilityType);
        Log.d(TAG, ""+reviewers.toString());

        rateLayout = (LinearLayout) findViewById(R.id.rateFacilityView);
        editText = findViewById(R.id.editTextTextMultiLine);
        userAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(userAccount == null){
            userEmail = "test@gmail.com";
        }else {
            userEmail = userAccount.getEmail();
        }
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        cancelButton = findViewById(R.id.cancel_button_review);
        submitButton = findViewById(R.id.submit_button_review);

        setBackgroundColor(facilityType);

        if (facilityType == POST) {
            ratingBar.setVisibility(View.INVISIBLE);
            TextView textTitle = (TextView) findViewById(R.id.RateFacilityTitle);
            textTitle.setText("Comment on a Post");

            TextView textView = (TextView) findViewById(R.id.RateFacilityDescription);
            textView.setText("Please leave your comments\nbelow");
        }

        submitButton.setTextColor(Color.parseColor("#dbba00"));
        submitButton.setEnabled(true);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(RateActivity.this);
                queue.start();

                if ((ratingBar.getRating() == 0 && editText.getText().toString().isEmpty() && facilityType != POST)
                    || (editText.getText().toString().isEmpty() && facilityType == POST)) {
                    Toast.makeText(getApplicationContext(), "Please do not submit an empty form", Toast.LENGTH_SHORT).show();
                } else if (ratingBar.getRating() == 0 && facilityType != POST) {
                    Toast.makeText(getApplicationContext(), "Please rate the facility from 0.5 to 5", Toast.LENGTH_SHORT).show();
                    return;
                } else if (editText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please add a comment", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if(isRating){
                        Toast.makeText(getApplicationContext(), "Please do not click again", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    isRating = true;
                    HashMap<String, String> paramsComment = new HashMap<String, String>();
                    paramsComment.put("facilityType", String.valueOf(facilityType));
                    paramsComment.put("facility_id", facilityId);
                    paramsComment.put("user_id", userEmail);
                    paramsComment.put("replyContent", editText.getText().toString());
                    if( userAccount == null){
                        paramsComment.put("username", "test test");

                    }else {
                        paramsComment.put("username", userAccount.getDisplayName());

                    }

                    paramsComment.put("rateScore", String.valueOf(ratingBar.getRating()));

                    //for add credit
                    paramsComment.put("AdditionType", "comment");
                    paramsComment.put("upUserId", userEmail);
                    paramsComment.put("downUserId", "");

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

                    JsonObjectRequest requestComment = new JsonObjectRequest(Request.Method.POST, vm_ip + "comment/add", data,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    isRating = false;
                                    Log.d(TAG, "requestComment" + response.toString());
                                    try {
                                        String result = response.getString("result");
                                        if (result.equals("already_exist")) {
                                            if (facilityType == POST) {
                                                Toast.makeText(getApplicationContext(), "You have commented in the past.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "You have reviewed in the past.", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
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
                                    isRating = false;
                                    Log.d(TAG, "2 onErrorResponse" + "Error: " + error.getMessage());
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

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 2000);

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setBackgroundColor(int facilityType) {
        switch (facilityType) {
            case 0:  // posts
                rateLayout.setBackgroundColor(Color.parseColor("#7781AE"));
                submitButton.setBackgroundColor(Color.parseColor("#7781AE"));
                cancelButton.setBackgroundColor(Color.parseColor("#7781AE"));
                editText.setBackgroundColor(Color.parseColor("#535A7A"));
                break;
            case 1:  // study
                rateLayout.setBackgroundColor(Color.parseColor("#010280"));
                submitButton.setBackgroundColor(Color.parseColor("#010280"));
                cancelButton.setBackgroundColor(Color.parseColor("#010280"));
                editText.setBackgroundColor(Color.parseColor("#010166"));
                break;
            case 2:  // entertainment
                rateLayout.setBackgroundColor(Color.parseColor("#00BB98"));
                submitButton.setBackgroundColor(Color.parseColor("#00BB98"));
                cancelButton.setBackgroundColor(Color.parseColor("#00BB98"));
                editText.setBackgroundColor(Color.parseColor("#00876E"));
                break;
            case 3:  // restaurant
                rateLayout.setBackgroundColor(Color.parseColor("#D2887A"));
                submitButton.setBackgroundColor(Color.parseColor("#D2887A"));
                cancelButton.setBackgroundColor(Color.parseColor("#D2887A"));
                editText.setBackgroundColor(Color.parseColor("#9E675C"));
                break;
            default:
                Log.d(TAG, "Error setting background color");
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        isRating = false;
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}