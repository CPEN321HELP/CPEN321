package com.example.help_m5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.example.help_m5.reviews.ReviewAdapter;
import com.example.help_m5.reviews.ReviewItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
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
    private static final int POST = 0;

    private ArrayList<CharSequence> reviewers;
    private String facilityId;
    private String title;
    private int numReviews;
    private int type;
    private double latitude;
    private double longitude;
    private boolean isPost;

    private String adderID;
    private String userID;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private MapView mapView;
    private List<ReviewItem> reviewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        recyclerView = (RecyclerView) findViewById(R.id.facilityRecyclerView);
        recyclerView.setHasFixedSize(true);  // every item in recyclerView has fixed size
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reviewItems = new ArrayList<>();

        adapter = new ReviewAdapter(FacilityActivity.this, FacilityActivity.this, reviewItems);
        recyclerView.setAdapter(adapter);

        // Handle JSON file from backend
        Bundle bundle = getIntent().getExtras();
        String facilityInfo = bundle.getString("facility_json");
        System.out.println(facilityInfo);
        userID = bundle.getString("userEmail");
        facilityId = bundle.getString("facility_id");
        type = bundle.getInt("facilityType");
        reviewers = new ArrayList<>();
        isPost = (POST == type);
        Log.d(TAG,"type is "+type+", is Post: "+isPost);
        try {
            JSONObject facility = new JSONObject(facilityInfo);
            // Load page
            //progressBar.setVisibility(View.VISIBLE);
            loadFacilityBackground(type);
            loadFacilityImage(facility);
            loadFacilityTexts(facility);
            loadFacilityLocation(facility);
            loadReviews(facility);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button rateButton = findViewById(R.id.rate_button);
        if (POST == type) {
            rateButton.setText("Comment");
            LinearLayout.LayoutParams marginParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView textView = (TextView) findViewById(R.id.facilityNumberOfRates);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView.setLayoutParams(marginParams);
        }
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

        Button reportFacilityButton = findViewById(R.id.report_facility_button);
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

        if ((Double) latitude != null && (Double) longitude != null) {
            mapView = findViewById(R.id.mapView);
            mapView.getMapAsync(FacilityActivity.this);
            mapView.onCreate(savedInstanceState);
        }

    }

    private void loadFacilityLocation(JSONObject facility){
        if (type != POST) {
            try {
                latitude = facility.getJSONObject("facility").getDouble("latitude");
                longitude = facility.getJSONObject("facility").getDouble("longitude");
            }catch (JSONException e){
                latitude = 49.273570;
                longitude = -123.241990;
                Log.d(TAG, "FacilityActivity does not have field: latitude or longitude");
            }

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

    }

    private void loadReviews(JSONObject facility) {

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
                    if(reviewers.contains(replierID)){ continue; }
                    String userName = (String) jsonobject.getString("userName");
                    double userRate = (double) jsonobject.getDouble("rateScore");
                    int downVote = (int) jsonobject.getInt("downVotes");
                    int upvote =  (int) jsonobject.getInt("upVotes");
                    String comment = (String) jsonobject.getString("replyContent");
                    String time = (String) jsonobject.getString("timeOfReply");

                    List<String> facilityInformation = new ArrayList<>();
                    facilityInformation.add(title);
                    facilityInformation.add(facilityId);
                    facilityInformation.add(String.valueOf(type));
                    facilityInformation.add(String.valueOf(isPost));

                    List<Integer> voteCounts = new ArrayList<>();
                    voteCounts.add(upvote);
                    voteCounts.add(downVote);

                    ReviewItem reviewItem = new ReviewItem(userName, userID, replierID, time, comment, userRate, voteCounts, facilityInformation);
                    reviewItems.add(reviewItem);
                    adapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    reviewers.add(replierID);
                    map.put(replierID,"1");
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }catch (JSONException e){
            Log.d(TAG, "FacilityActivity does not have field: reviews");
        }
    }

    private void loadFacilityImage(JSONObject facility) {
        String image;
        try {
            image = (String) facility.getJSONObject("facility").getString("facilityImageLink");
            Uri uriImage = Uri.parse(image);
            Picasso.get().load(uriImage).into((ImageView)findViewById(R.id.imageView2), new Callback() {
                @Override
                public void onSuccess() {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView2);
                    imageView.setScaleX(1);
                    imageView.setScaleY(1);
                    imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Log.d(TAG, "image loaded successfully");
                }

                @Override
                public void onError(Exception e) {
                    Log.d(TAG, "no image or loaded unsuccessfully");
                }
            });
        }catch (JSONException e){
            image = "none";
            Log.d(TAG, "FacilityActivity does not have field: image");
        }
    }

    private void loadFacilityTexts(JSONObject facility){
        try {
            title = (String) facility.getJSONObject("facility").getString("facilityTitle");
        } catch (JSONException e){
            title = "FacilityActivity does not have field: title";
            Log.d(TAG, "FacilityActivity does not have field: facilityTitle");
        }

        String description;
        try {
            description = (String) facility.getJSONObject("facility").getString("facilityDescription");
        } catch (JSONException e){
            description = "FacilityActivity does not have field: facilityDescription";
            Log.d(TAG, "FacilityActivity does not have field: facilityDescription");
        }

        try {
            adderID = facility.getString("adderID");
        } catch (JSONException e){
            adderID = "none";
            Log.d(TAG, "FacilityActivity does not have field: adderID");
        }

        float rate;
        try {
            rate = (float) facility.getJSONObject("facility").getDouble("facilityOverallRate");
        } catch (JSONException e){
            rate = 0;
            Log.d(TAG, "FacilityActivity does not have field: rate");
        }

        try {
            numReviews = (int) facility.getJSONObject("facility").getInt("numberOfRates");
        } catch (JSONException e){
            numReviews = 0;
            Log.d(TAG, "FacilityActivity does not have field: rate");
        }

        TextView facilityTitle = findViewById(R.id.facilityTitle);
        facilityTitle.setText(title);

        TextView facilityDescription = findViewById(R.id.facilityDescription);
        facilityDescription.setText(description);

        TextView facilityRate = findViewById(R.id.facilityRatingText);
        facilityRate.setText("★" + String.valueOf(rate));

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(rate);

        TextView facilityNumReviews = findViewById(R.id.facilityNumberOfRates);
        facilityNumReviews.setText(String.valueOf(numReviews) + " Reviews");

        if (type == POST) {
            facilityRate.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            facilityNumReviews.setTextSize(15f);
            facilityNumReviews.setGravity(Gravity.CENTER);
            LinearLayout mapLayout = (LinearLayout) findViewById(R.id.facilityMap);
            mapLayout.setVisibility(View.GONE);
            TextView comments = (TextView) findViewById(R.id.facilityReviewsTitle);
            comments.setText("Comments");
        }
    }

    private void loadFacilityBackground(int type) {
        Button button = (Button) findViewById(R.id.rate_button);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.facilityMap);
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        if (type == 0) {
            button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.posts_background));
            linearLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.posts_background));
            if (imageView.getBackground() == null) {
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.post_image));
                imageView.setScaleX(0.4f);
            }
        } else if (type == 1) {
            button.setBackgroundResource(R.drawable.studys_background);
            linearLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.studys_background));
            if (imageView.getBackground() == null) {
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.studys_image));
                imageView.setScaleX(0.4f);
            }
        } else if (type == 2) {
            button.setBackgroundResource(R.drawable.entertainments_background);
            linearLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.entertainments_background));
            if (imageView.getBackground() == null) {
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.entertainments_image));
                imageView.setScaleX(0.9f);
                imageView.setScaleY(0.9f);
            }
        } else if (type == 3) {
            button.setBackgroundResource(R.drawable.resturants_background);
            linearLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.resturants_background));
            if (imageView.getBackground() == null) {
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.restaurant_image));
            }
        } else {
            button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.posts_background));
            linearLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.posts_background));
            imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.post_image));
        }
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
                loadFacilityTexts(response);
                loadReviews(response);
                loadFacilityBackground(facility_type);
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
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.

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
            boolean success = false;
            if (isDarkModeOn) {
                success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.style_json));
            } else {
                Log.e(TAG, "Map day mode");
            }
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

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