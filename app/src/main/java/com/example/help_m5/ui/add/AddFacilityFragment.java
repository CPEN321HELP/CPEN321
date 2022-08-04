package com.example.help_m5.ui.add;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.help_m5.CustomAdapter;
import com.example.help_m5.R;
import com.example.help_m5.databinding.FragmentAddFacilityBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddFacilityFragment extends Fragment {
    private String vm_ip;
    private static final String TAG = "AddFacilityFragment";

    private FragmentAddFacilityBinding binding;
    private Button submit;
    private Button clean;
    private final String[] countryNames={"<-Please Select Below->", "Posts","Eat","Study","Play"};
    private final int[] flags = {R.drawable.ic_baseline_all_inclusive_24_white, R.drawable.ic_baseline_post_24_white, R.drawable.ic_baseline_resturants_white, R.drawable.ic_baseline_menu_book_24_white, R.drawable.ic_baseline_videogame_asset_24_white};
    private String facility_type;
    private EditText newFacilityTitle;
    private EditText newFacilityDescription;
    private EditText newFacilityImageLink;
    private EditText newFacilityLocation;
    private boolean titleOK = false;
    private boolean descriptionOK = false;
    private boolean imageLinkOK = false;
    private boolean locationOK = false;
    private boolean isPost = false;
    private String longitude;
    private String latitude;
    private String comment;
    private String user_email ;
    private boolean isAdding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isAdding = false;
        vm_ip = getResources().getString(R.string.azure_ip);
        binding = FragmentAddFacilityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account == null){
            user_email = "test@gmail.com";
        }else {
            user_email = account.getEmail();
        }


        newFacilityTitle = binding.newFacilityTitle;
        //newFacilityTitle.setHint("please enter a title");
        newFacilityTitle.setHint("please enter at least 5 characters");
        newFacilityTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //must have
                comment = s.toString();
                Log.d("not useful", comment);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                Log.d(TAG, "\""+s.toString().trim()+"\"");
                int length = input.replaceAll("[^a-zA-Z0-9]","").length();
                if(length > 5){
                    titleOK = true;
                    //binding.imageNewFacilityTitle.setImageResource(android.R.drawable.presence_online);
                    binding.imageNewFacilityTitle.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    binding.imageNewFacilityTitle.setTag("good");
                }else{
                    titleOK = false;
                    //binding.imageFacilityDescription.setImageResource(android.R.drawable.presence_busy);
                    binding.imageNewFacilityTitle.setImageResource(R.drawable.ic_baseline_warning_24);
                    binding.imageNewFacilityTitle.setTag("bad");
                }
                enableSubmit();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //must have
                comment = s.toString();
                Log.d("not useful", comment);
            }

        });

        newFacilityDescription = binding.newFacilityDescription;
        //newFacilityDescription.setHint("Please enter a description");
        newFacilityDescription.setHint("Please enter at least 25 characters");
        newFacilityDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                comment = s.toString();
                Log.d("not useful", comment);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
//                Log.d(TAG, "\""+s.toString().trim()+"\"");
                int length = input.replaceAll("[^a-zA-Z0-9]","").length();
                if(length > 25){
                    descriptionOK = true;
                    //binding.imageNewFacilityTitle.setImageResource(android.R.drawable.presence_online);
                    binding.imageFacilityDescription.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    binding.imageFacilityDescription.setTag("good");
                }else{
                    descriptionOK = false;
                    //binding.imageFacilityDescription.setImageResource(android.R.drawable.presence_busy);
                    binding.imageFacilityDescription.setImageResource(R.drawable.ic_baseline_warning_24);
                    binding.imageFacilityDescription.setTag("bad");
                }
                enableSubmit();
            }
            @Override
            public void afterTextChanged(Editable s) {
                comment = s.toString();
                Log.d("not useful, here because to remove codacy issues", comment);
            }
        });

        newFacilityImageLink = binding.newFacilityImageLink;
        //newFacilityImageLink.setHint("Please enter an valid url");
        newFacilityImageLink.setHint("i.e. https://imgtu.come/i/jwCDjH");
        binding.imageFacilityImageLink.setTag("bad");
        newFacilityImageLink.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                comment = s.toString();
                Log.d(TAG, comment);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();

                String regex1 = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                Pattern pattern1 = Pattern.compile(regex1);
                Matcher matcher1 = pattern1.matcher(input);

                String regex2 = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                Pattern pattern2 = Pattern.compile(regex2);
                Matcher matcher2 = pattern2.matcher(input);

                if(matcher1.matches() || matcher2.matches()){
                    imageLinkOK = true;
                    //binding.imageNewFacilityTitle.setImageResource(android.R.drawable.presence_online);
                    binding.imageFacilityImageLink.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    binding.imageFacilityImageLink.setTag("good");

                }else{
                    imageLinkOK = false;
                    //binding.imageFacilityDescription.setImageResource(android.R.drawable.presence_busy);
                    binding.imageFacilityImageLink.setImageResource(R.drawable.ic_baseline_warning_24);
                    binding.imageFacilityImageLink.setTag("bad");
                }
//                Log.d(TAG, "\""+s.toString().trim()+"\"");
                enableSubmit();
            }
            @Override
            public void afterTextChanged(Editable s) {
                comment = s.toString();
                Log.d(TAG, comment);
            }
        });

        setLocationListener();
        setSpinner();
        setButtons();
        return root;
    }

    private void setLocationListener(){
        newFacilityLocation = binding.newFacilityLocation;
        newFacilityLocation.setHint("i.e. 650 W 41st Ave, Vancouver, BC, V5Z2M9");
        //newFacilityLocation.setHint("Please enter an valid address");
        newFacilityLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String input = newFacilityLocation.getText().toString().trim();
                    Log.d(TAG, input);

                    LatLng result = getLocationFromAddress(getContext(),input );
                    if (result == null){
                        Log.d(TAG, "error");
                        locationOK = false;
                        //binding.imageFacilityDescription.setImageResource(android.R.drawable.presence_busy);
                        binding.imageFacilityLocation.setImageResource(R.drawable.ic_baseline_warning_24);
                        binding.imageFacilityLocation.setTag("bad");

                    }else {
                        locationOK = true;
                        //binding.imageNewFacilityTitle.setImageResource(android.R.drawable.presence_online);
                        binding.imageFacilityLocation.setImageResource(R.drawable.ic_baseline_check_circle_24);
                        binding.imageFacilityLocation.setTag("good");
                        latitude = Double.toString(result.latitude);
                        longitude = Double.toString(result.longitude);
                        Log.d(TAG, result.toString());
                    }
                }
                enableSubmit();
            }
        });
    }

    private void setSpinner(){
        binding.newFacilityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facility_type = getString(countryNames[position]);
                Log.d(TAG, "facility_type in onItemSelected is: " +facility_type);
                LinearLayout linearLayout = binding.addFacilityView;
                if(position != 0){
                    if(position == 1){
                        isPost = true;
                        binding.locationLayout.setVisibility(View.INVISIBLE);
                        linearLayout.setBackgroundColor(Color.parseColor("#7781AE"));
                        newFacilityDescription.setBackgroundColor(Color.parseColor("#535A7A"));
                        newFacilityTitle.setBackgroundColor(Color.parseColor("#535A7A"));
                        newFacilityLocation.setBackgroundColor(Color.parseColor("#535A7A"));
                        newFacilityImageLink.setBackgroundColor(Color.parseColor("#535A7A"));
                        binding.newFacilityType.setPopupBackgroundResource(R.drawable.spinner_background_posts);
                    }else {
                        binding.locationLayout.setVisibility(View.VISIBLE);
                        isPost = false;
                        switch (position) {
                            case 2:  // restaurants
                                linearLayout.setBackgroundColor(Color.parseColor("#D2887A"));
                                newFacilityDescription.setBackgroundColor(Color.parseColor("#9E675C"));
                                newFacilityTitle.setBackgroundColor(Color.parseColor("#9E675C"));
                                newFacilityLocation.setBackgroundColor(Color.parseColor("#9E675C"));
                                newFacilityImageLink.setBackgroundColor(Color.parseColor("#9E675C"));
                                binding.newFacilityType.setPopupBackgroundResource(R.drawable.spinner_background_restaurants);
                                break;
                            case 3:  // studies
                                linearLayout.setBackgroundColor(Color.parseColor("#010280"));
                                newFacilityDescription.setBackgroundColor(Color.parseColor("#010166"));
                                newFacilityTitle.setBackgroundColor(Color.parseColor("#010166"));
                                newFacilityLocation.setBackgroundColor(Color.parseColor("#010166"));
                                newFacilityImageLink.setBackgroundColor(Color.parseColor("#010166"));
                                binding.newFacilityType.setPopupBackgroundResource(R.drawable.spinner_background_studys);
                                break;
                            case 4: // entertainments
                                linearLayout.setBackgroundColor(Color.parseColor("#00BB98"));
                                newFacilityDescription.setBackgroundColor(Color.parseColor("#00876E"));
                                newFacilityTitle.setBackgroundColor(Color.parseColor("#00876E"));
                                newFacilityLocation.setBackgroundColor(Color.parseColor("#00876E"));
                                newFacilityImageLink.setBackgroundColor(Color.parseColor("#00876E"));
                                binding.newFacilityType.setPopupBackgroundResource(R.drawable.spinner_background_entertainments);
                                break;
                            default:
                                Log.d(TAG, "error setting spinner background color");
                                break;
                        }
                    }
                    binding.imageFacilityType.setTag("good");
                    //binding.imageNewFacilityTitle.setImageResource(android.R.drawable.presence_online);
                    binding.imageFacilityType.setImageResource(R.drawable.ic_baseline_check_circle_24);
                }else{
                    binding.imageFacilityType.setTag("bad");
                    //binding.imageFacilityDescription.setImageResource(android.R.drawable.presence_busy);
                    binding.imageFacilityType.setImageResource(R.drawable.ic_baseline_warning_24);
                }
                enableSubmit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.imageFacilityType.setTag("bad");
                //binding.imageFacilityDescription.setImageResource(android.R.drawable.presence_busy);
                binding.imageFacilityType.setImageResource(R.drawable.ic_baseline_warning_24);
            }
        });
        CustomAdapter customAdapter = new CustomAdapter(getContext(), flags, countryNames, 1);
        binding.newFacilityType.setAdapter(customAdapter);
    }

    private void setButtons(){
        submit = binding.submitAll;
        submit.setEnabled(false);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPost){
                    Toast.makeText(getContext(), "Sending your response to server!", Toast.LENGTH_SHORT).show();
                    addFacility(getContext(), newFacilityTitle.getText().toString().trim(), newFacilityDescription.getText().toString().trim(), facility_type,newFacilityImageLink.getText().toString().trim(), "", "", user_email, clean);
//                    Toast.makeText(getContext(), "Please fill-out all fields!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getContext(), "Sending your response to server!", Toast.LENGTH_SHORT).show();
                    addFacility(getContext(), newFacilityTitle.getText().toString().trim(), newFacilityDescription.getText().toString().trim(), facility_type,newFacilityImageLink.getText().toString().trim(), longitude, latitude, user_email, clean);
                }
                enableSubmit();
                /*
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                        DatabaseConnection db = new DatabaseConnection();
                        db.updateUserInfo(navigationView, getContext(), user_email, getActivity(),true);
                    }
                }, 1000);
                */

            }
        });

        clean = binding.cleanAll;
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFacilityTitle.setText("");
                newFacilityDescription.setText("");
                newFacilityImageLink.setText("");
                newFacilityLocation.setText("");
                //binding.imageFacilityDescription.setImageResource(android.R.drawable.presence_busy);
                binding.imageFacilityDescription.setImageResource(R.drawable.ic_baseline_warning_24);
                binding.imageFacilityLocation.setTag("bad");
                enableSubmit();
            }
        });
    }

    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param strAddress  : string of address user typed
     * @return : LatLng, contains Latitude and Longitude; or null if user typed is not a valid address.
     * @Pupose : get long and lat from the address user typed.
     */
    private LatLng getLocationFromAddress(Context applicationContext, String strAddress) {
        Geocoder coder = new Geocoder(applicationContext);
        LatLng p1 = null;
        try {
            List<Address> address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    private String getString(String selected){
        switch (selected){
            case "Play":
                return "2";
            case "Eat":
                return "3";
            case "Study":
                return "1";
            case "Posts":
                return "0";
            default:
                return "";
        }
    }
    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param title, description, type, imageLink, longitude, latitude : information need to be stored on database
     * @param user_id : the user who added this facility
     * @Pupose : to add a new facility in to database
     */
    public void addFacility(Context applicationContext,String title, String description, String type, String imageLink, String longitude, String latitude, String user_id, Button clean){
        if(isAdding){
            Toast.makeText(getContext(), "Please do not click again", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = vm_ip + "addFacility";
//        Log.d(TAG, url);
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);
        HashMap<String, String> newFacilityParam = new HashMap<String, String>();
        queue.start();

        newFacilityParam.put("title", title);
        newFacilityParam.put("description", description);
        newFacilityParam.put("long", longitude);
        newFacilityParam.put("lat", latitude);
        newFacilityParam.put("type", type);
        newFacilityParam.put("facilityImageLink", imageLink);
        newFacilityParam.put("adderID", user_id);
        //used to add credit for users
        newFacilityParam.put("AdditionType", "addFacility");
        newFacilityParam.put("upUserId", user_id);
        Log.d(TAG, "newFacilityParam" + newFacilityParam.toString());
        isAdding = true;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(newFacilityParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                isAdding = false;
                Log.d(TAG, "response in addFacility is: " + response.toString());
                Toast.makeText(getContext(), "Server received your submission", Toast.LENGTH_SHORT).show();
                clean.performClick();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isAdding = false;
                Log.d(TAG, "onErrorResponse addFacility " + "Error: " + error.getMessage());
                Toast.makeText(getContext(), "Error happened when connecting to server, please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
    }

    private void enableSubmit(){
        if(isPost){
            if(titleOK && descriptionOK && imageLinkOK){
                submit.setEnabled(true);
            }
        }else{
            if(titleOK && descriptionOK && imageLinkOK && locationOK && (longitude != null) && (latitude != null)){
                submit.setEnabled(true);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        isAdding = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        getFragmentManager().beginTransaction().remove(AddFacilityFragment.this).commitAllowingStateLoss();
        binding = null;
    }



}