package com.example.help_m5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FacilityActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "FacilityActivity";

    private String facilityId;
    private String title;
    private float rate;
    private int numReviews;
    private String adderID;
    private int type;
    private double latitude;
    private double longitude;
    private boolean isPost;
//    private Button rateButton;
//    private Button reportFacilityButton;
//    private MapView mapView;
//    private GoogleMap mMap;
//    private ArrayList<CharSequence> reviewers;
//    private int id = 1;
//    private final int UPVOTE_BASE_ID = 10000000;
//    private final int DOWNVOTE_BASE_ID = 20000000;
//    private final int UPVOTE_TEXTVIEW_BASE_ID = 30000000;
//    private final int DOWNVOTE_TEXTVIEW_BASE_ID = 40000000;
//    private final int REPORT_BUTTON_BASE_ID = 50000000;
//    private final int POST = 0;
    private MapView mapView;
    private ArrayList<CharSequence> reviewers;
    private int id = 1;
    private final int POST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);
        String description = "";
        reviewers = new ArrayList<>();
        // Get data from database
        Bundle bundle = getIntent().getExtras();
        facilityId = bundle.getString("facility_id");
        type = bundle.getInt("facilityType");
        isPost = (POST == type);
        String facilityInfo = bundle.getString("facility_json");
        Log.d(TAG,"type is "+type+", is Post: "+isPost);
        try {
            JSONObject facility = new JSONObject(facilityInfo);
            try {
                title = (String) facility.getJSONObject("facility").getString("facilityTitle");

            }catch (JSONException e){
                title = "FacilityActivity does not have field: title";
                Log.d(TAG, "FacilityActivity does not have field: facilityTitle");
            }

            try {
                description = (String) facility.getJSONObject("facility").getString("facilityDescription");

            }catch (JSONException e){
                description = "FacilityActivity does not have field: facilityDescription";
                Log.d(TAG, "FacilityActivity does not have field: facilityDescription");
            }

            try {
                adderID = facility.getString("adderID");
            }catch (JSONException e){
                adderID = "none";
                Log.d(TAG, "FacilityActivity does not have field: adderID");
            }

            // Facility Image
            String image;
            try {
                image = (String) facility.getJSONObject("facility").getString("facilityImageLink");
                if (Uri.parse(image) == null) {
                    findViewById(R.id.imageView2).setVisibility(View.GONE);
                } else {
                    Uri uriImage = Uri.parse(image);
                    Picasso.get().load(uriImage).into((ImageView)findViewById(R.id.imageView2), new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "image loaded successfully");
                        }

                        @Override
                        public void onError(Exception e) {
                            ImageView imageView = (ImageView)findViewById(R.id.imageView2);
                            imageView.setVisibility(View.GONE);
                        }
                    });
                }
            }catch (JSONException e){
                image = "none";
                Log.d(TAG, "FacilityActivity does not have field: image");
            }

            reCreatePart1(facility);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Facility Title
        TextView facilityTitle = findViewById(R.id.facilityTitle);
        facilityTitle.setText(title);

        // Facility Description
        TextView facilityDescription = findViewById(R.id.facilityDescription);
        facilityDescription.setText(description);

        reCreatePart2();
        /*
        for (int i = 1; i < id; i++) {
            CheckBox checkUpvote = (CheckBox) findViewById(UPVOTE_BASE_ID + i);
            boolean checkedUp = PreferenceManager.getDefaultSharedPreferences(FacilityActivity.this)
                    .getBoolean("upVote"+String.valueOf(UPVOTE_BASE_ID + i), false);
            checkUpvote.setOnCheckedChangeListener(null);
            checkUpvote.setChecked(checkedUp);

            CheckBox checkDownvote = (CheckBox) findViewById(DOWNVOTE_BASE_ID + i);
            boolean checkedDown = PreferenceManager.getDefaultSharedPreferences(FacilityActivity.this)
                    .getBoolean("downVote"+String.valueOf(DOWNVOTE_BASE_ID + i), false);
            checkDownvote.setOnCheckedChangeListener(null);
            checkDownvote.setChecked(checkedDown);
        }
        */
        // Google Maps Location
        if ((Double) latitude != null && (Double) longitude != null) {
            mapView = findViewById(R.id.mapView);
            mapView.getMapAsync(FacilityActivity.this);
            mapView.onCreate(savedInstanceState);
        }
    }

    private void reCreatePart1(JSONObject facility){
        try {
            rate = (float) facility.getJSONObject("facility").getDouble("facilityOverallRate");
        }catch (JSONException e){
            rate = 0;
            Log.d(TAG, "FacilityActivity does not have field: rate");
        }

        try {
            numReviews = (int) facility.getJSONObject("facility").getInt("numberOfRates");
        }catch (JSONException e){
            numReviews = 0;
            Log.d(TAG, "FacilityActivity does not have field: rate");
        }

        if (type != POST) {
            try {
                latitude = facility.getJSONObject("facility").getDouble("latitude");
                longitude = facility.getJSONObject("facility").getDouble("longitude");
            }catch (JSONException e){
                latitude = 49.273570;
                longitude = -123.241990;
                Log.d(TAG, "FacilityActivity does not have field: latitude or longitude");
            }
        }

        try {
            HashMap<String, String> map = new HashMap<String, String>();

            JSONArray jsonarray = facility.getJSONArray("reviews");
            numReviews = (int) jsonarray.length();
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if(jsonobject.toString().equals("{}")){
                    continue;
                }
                if(jsonobject.toString().equals("[]")){
                    continue;
                }
                try{
                    String replierID = (String) jsonobject.getString("replierID");
                    if(reviewers.contains(replierID)){
                        continue;
                    }
                    String userName = (String) jsonobject.getString("userName");
                    double userRate = (double) jsonobject.getDouble("rateScore");
                    int downVote = (int) jsonobject.getInt("downVotes");
                    int upvote =  (int) jsonobject.getInt("upVotes");
                    String comment = (String) jsonobject.getString("replyContent");
                    String time = (String) jsonobject.getString("timeOfReply");
                    createUserReview((float) userRate, userName, replierID, comment, time, upvote, downVote, isPost);
                    map.put(replierID,"1");
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }catch (JSONException e){
            Log.d(TAG, "FacilityActivity does not have field: reviews");
        }
    }

    private void reCreatePart2(){
        // Facility Rate
        TextView facilityRate = findViewById(R.id.facilityRatingText);
        facilityRate.setText("★" + String.valueOf(rate));

        // Rating bar
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setRating(rate);

        // Facility Number of Reviews/Rates
        TextView facilityNumReviews = findViewById(R.id.facilityNumberOfRates);
        facilityNumReviews.setText(String.valueOf(numReviews) + " Reviews");



        // Address
        if (type != POST) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                if (geocoder.getFromLocation(latitude, longitude, 1) == null) {
                    mapView.setVisibility(View.GONE);
                    TextView addressView = (TextView) findViewById(R.id.facilityAddress);
                    addressView.setText("Address not avaliable yet");
                } else {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    TextView addressView = (TextView) findViewById(R.id.facilityAddress);
                    addressView.setText(address);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Rate Button
        Button rateButton = findViewById(R.id.rate_button);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rateIntent = new Intent(FacilityActivity.this, RateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("facility_id", facilityId);
                bundle.putInt("facility_type", type);
                bundle.putCharSequenceArrayList("reviewers", reviewers);
                rateIntent.putExtras(bundle);
                startActivity(rateIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Button reportFacilityButton = findViewById(R.id.repor_facility_button);
        reportFacilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reportIntent = new Intent(FacilityActivity.this, ReportActivity.class);
                Bundle bundle = new Bundle();
                Button button = (Button) v;
                bundle.putString("title", title);
                bundle.putString("user_email", (String) button.getTag());
                bundle.putInt("facility_id", Integer.parseInt(facilityId));
                bundle.putInt("facility_type", type);
                bundle.putString("reportedUserId", adderID);
                Log.d(TAG, "adderID "+adderID);
                bundle.putString("report_type", "6"); //5 means report comment

                reportIntent.putExtras(bundle);
                startActivity(reportIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        if (type == POST) {
            LinearLayout.LayoutParams marginParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            marginParams.setMargins(0, 5, 0, 0);
            facilityRate.setVisibility(View.GONE);
            rateButton.setText("Comment");
            ratingBar.setVisibility(View.GONE);
            facilityNumReviews.setTextSize(dpToPx(7f));
            facilityNumReviews.setLayoutParams(marginParams);
            facilityNumReviews.setGravity(Gravity.CENTER);
            LinearLayout mapLayout = (LinearLayout) findViewById(R.id.facilityMap);
            mapLayout.setVisibility(View.GONE);
            TextView comments = (TextView) findViewById(R.id.facilityReviewsTitle);
            comments.setText("Comments");
        }
    }

    public void createUserReview(float userRate, String userName, String replierID, String userDescription, String userDate, int upVoteCounter, int downVoteCounter, boolean isPost) {
        // Linear Layouts
        reviewers.add(replierID);
        LinearLayout review = new LinearLayout(this);
        review.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        review.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dpToPx(15f), dpToPx(5f), dpToPx(15f), dpToPx(10f));
        review.setElevation(30);
        review.setBackgroundColor(Color.parseColor("#FFFFFF"));
        review.setLayoutParams(layoutParams);

        LinearLayout usernameAndDate = new LinearLayout(this);
        usernameAndDate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        usernameAndDate.setOrientation(LinearLayout.HORIZONTAL);
        usernameAndDate.setBackgroundColor(Color.parseColor("#FFFFFF"));

        LinearLayout descriptionAndReport = new LinearLayout(this);
        descriptionAndReport.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        descriptionAndReport.setOrientation(LinearLayout.HORIZONTAL);
        descriptionAndReport.setBackgroundColor(Color.parseColor("#FFFFFF"));

        LinearLayout votingSystem = new LinearLayout(this);
        votingSystem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        votingSystem.setOrientation(LinearLayout.HORIZONTAL);
        votingSystem.setBackgroundColor(Color.parseColor("#FFFFFF"));

        // Specific Element Views
        TextView userNameView = new TextView(this);
        userNameView.setText(userName);
        userNameView.setTextSize(15f);
        userNameView.setTextColor(Color.parseColor("#000000"));
        LinearLayout.LayoutParams layoutMargin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMargin.setMargins(dpToPx(5f), dpToPx(5f), dpToPx(5f), dpToPx(0f));
        userNameView.setLayoutParams(layoutMargin);

        TextView userDateView = new TextView(this);
        userDateView.setText(userDate);
        userDateView.setTextSize(15f);
        userDateView.setTextColor(Color.parseColor("#626062"));
        LinearLayout.LayoutParams layoutParamsDate = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsDate.setMargins(dpToPx(210f), dpToPx(3f), dpToPx(5f), dpToPx(0f));
        layoutParamsDate.gravity = Gravity.CENTER_VERTICAL;
        userDateView.setLayoutParams(layoutParamsDate);

        TextView userDescriptionView = new TextView(this);
        userDescriptionView.setText(userDescription);
        userDescriptionView.setTextSize(15f);
        userDescriptionView.setTextColor(Color.parseColor("#000000"));
        userDescriptionView.setWidth(dpToPx(300f));
        LinearLayout.LayoutParams layoutParamsDescription = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsDescription.setMargins(dpToPx(5f), dpToPx(7f), dpToPx(5f), dpToPx(3f));
        layoutParamsDescription.gravity = Gravity.CENTER;
        userDescriptionView.setLayoutParams(layoutParamsDescription);

        Button reportCommentButton = new Button(this, null, androidx.appcompat.R.attr.borderlessButtonStyle);
        int REPORT_BUTTON_BASE_ID = 50000000;
        reportCommentButton.setId(REPORT_BUTTON_BASE_ID + id);
        reportCommentButton.setTag(replierID);
        reportCommentButton.setText("Report");
        reportCommentButton.setTextSize(dpToPx(5f));
        reportCommentButton.setTextColor(Color.parseColor("#626062"));
        reportCommentButton.setAllCaps(false);
        LinearLayout.LayoutParams layoutParamsReport = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsReport.setMargins(dpToPx(170f), dpToPx(0f), dpToPx(0f), dpToPx(0f));
        reportCommentButton.setLayoutParams(layoutParamsReport);
        reportCommentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent reportIntent = new Intent(FacilityActivity.this, ReportActivity.class);
                Bundle bundle = new Bundle();
                Button button = (Button) v;
                bundle.putString("title", title);
                bundle.putString("user_email", (String) button.getTag());
                bundle.putInt("facility_id", Integer.parseInt(facilityId));
                bundle.putInt("facility_type", type);
                bundle.putString("report_type", "5"); //5 means report comment
                bundle.putString("reportedUserId", replierID); //5 means report comment

                reportIntent.putExtras(bundle);
                startActivity(reportIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        TextView upVoteCount = new TextView(this);
        upVoteCount.setText(String.valueOf(upVoteCounter));
        int UPVOTE_TEXTVIEW_BASE_ID = 30000000;
        upVoteCount.setId(UPVOTE_TEXTVIEW_BASE_ID + id);
        LinearLayout.LayoutParams layoutParamsVoteCount = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsVoteCount.setMargins(dpToPx(2f), dpToPx(0f), dpToPx(0f), dpToPx(0f));
        upVoteCount.setLayoutParams(layoutParamsVoteCount);

        CheckBox upVote = new CheckBox(this);
        upVote.setTag(replierID);
        upVote.setButtonDrawable(R.drawable.upvote);
        int UPVOTE_BASE_ID = 10000000;
        upVote.setId(UPVOTE_BASE_ID + id);
        boolean checkedUp = PreferenceManager.getDefaultSharedPreferences(FacilityActivity.this)
                .getBoolean("upVote"+String.valueOf(UPVOTE_BASE_ID + id), false);
        if (checkedUp) {
            upVote.setChecked(checkedUp);
        }
        LinearLayout.LayoutParams layoutParamsUpvote = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsUpvote.setMargins(dpToPx(5f), dpToPx(0f), dpToPx(0f), dpToPx(0f));
        upVote.setLayoutParams(layoutParamsUpvote);
        upVote.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LinearLayout linearLayout = (LinearLayout) buttonView.getParent();
            TextView textView = (TextView) linearLayout.getChildAt(1);
            if (isChecked) {
                AdjustVote(String.valueOf(type), facilityId, (String) buttonView.getTag(), "up", "pend");
                textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) + 1));
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("upVote"+String.valueOf(buttonView.getId()), true).commit();
                CheckBox oppositeBox = (CheckBox)findViewById((int)buttonView.getId() + 10000000);
                if (oppositeBox.isChecked()) {
                    oppositeBox.setChecked(false);
                }
            } else {
                AdjustVote(String.valueOf(type), facilityId, (String) buttonView.getTag(), "up", "cancel");
                textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) - 1));
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("upVote"+String.valueOf(buttonView.getId()), false).commit();
            }
        });

        TextView downVoteCount = new TextView(this);
        downVoteCount.setText(String.valueOf(downVoteCounter));
        int DOWNVOTE_TEXTVIEW_BASE_ID = 40000000;
        downVoteCount.setId(DOWNVOTE_TEXTVIEW_BASE_ID + id);
        downVoteCount.setLayoutParams(layoutParamsVoteCount);

        CheckBox downVote = new CheckBox(this);
        downVote.setTag(replierID);
        downVote.setButtonDrawable(R.drawable.downvote);
        int DOWNVOTE_BASE_ID = 20000000;
        downVote.setId(DOWNVOTE_BASE_ID + id);
        boolean checkedDown = PreferenceManager.getDefaultSharedPreferences(FacilityActivity.this)
                .getBoolean("downVote"+String.valueOf(DOWNVOTE_BASE_ID + id), false);
        if (checkedDown) {
            downVote.setChecked(checkedDown);
        }
        LinearLayout.LayoutParams layoutParamsDownvote = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsDownvote.setMargins(dpToPx(10f), dpToPx(0f), dpToPx(0f), dpToPx(0f));
        downVote.setLayoutParams(layoutParamsDownvote);
        downVote.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LinearLayout linearLayout = (LinearLayout) buttonView.getParent();
            TextView textView = (TextView) linearLayout.getChildAt(3);
            if (isChecked) {
                AdjustVote(String.valueOf(type), facilityId, (String) buttonView.getTag(), "down", "pend");
                textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) - 1));
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("downVote"+String.valueOf(buttonView.getId()), true).commit();
                CheckBox oppositeBox = (CheckBox)findViewById((int)buttonView.getId() - 10000000);
                if (oppositeBox.isChecked()) {
                    oppositeBox.setChecked(false);
                }
            } else {
                AdjustVote(String.valueOf(type), facilityId, (String) buttonView.getTag(), "down", "cancel");
                textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) + 1));
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("downVote"+String.valueOf(buttonView.getId()), false).commit();
            }
        });

        // Define Parent-Child relationships
        usernameAndDate.addView(userNameView);
        usernameAndDate.addView(userDateView);
        descriptionAndReport.addView(userDescriptionView);

        votingSystem.addView(upVote);
        votingSystem.addView(upVoteCount);
        votingSystem.addView(downVote);
        votingSystem.addView(downVoteCount);
        votingSystem.addView(reportCommentButton);
        review.addView(usernameAndDate);
        if (!isPost) {
            RatingBar userRateView = new RatingBar(new ContextThemeWrapper(this, R.style.RatingBar), null, android.R.attr.ratingBarStyleSmall);
            userRateView.setRating(userRate);
            userRateView.setNumStars(5);
            LinearLayout.LayoutParams layoutParamsRate = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsRate.setMargins(dpToPx(4f), dpToPx(0f), dpToPx(5f), dpToPx(0f));
            layoutParamsRate.gravity = Gravity.CENTER_VERTICAL;
            userRateView.setLayoutParams(layoutParamsRate);
            review.addView(userRateView);
        }
        review.addView(descriptionAndReport);
        review.addView(votingSystem);

        LinearLayout linearLayout = findViewById(R.id.facilityReviews);
        linearLayout.addView(review);
        id++;
    }

    private void AdjustVote(String facilityType, String facilityId, String userId, String vote, String isCancelled) {
        String url = "http://20.213.243.141:8000/Votes";
        RequestQueue queue = Volley.newRequestQueue(FacilityActivity.this);
        HashMap<String, String> params = new HashMap<String, String>();
        queue.start();
        params.put("facilityType", facilityType);
        params.put("facility_id", facilityId);
        params.put("user_id", userId);
        params.put("vote", vote);
        params.put("isCancelled", isCancelled);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"response is: "+response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse" + "Error: " + error.getMessage());
                    }
                });
        queue.add(request);
    }

    private int dpToPx(float dp) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return (int) px;
    }

    private void selfUpdate(String facility_id, int facility_type){
        String url = getString(R.string.azure_ip) + "specific";

        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.start();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("facility_id", facility_id);
        params.put("facilityType", String.valueOf(facility_type));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                reCreatePart1(response);
                reCreatePart2();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse getSpecificFacility " + "Error: " + error.getMessage());
            }
        });
        queue.add(jsObjRequest);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng marker = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions()
                .position(marker)
                .title("Marker"));
        float zoomLevel = 17.0f; //This goes up to 21
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, zoomLevel));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        selfUpdate(facilityId, type);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}