package com.example.help_m5.ui.home;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.help_m5.CustomAdapter;
import com.example.help_m5.databinding.FragmentHomeBinding;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.example.help_m5.FacilityActivity;
import com.example.help_m5.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    static final int posts = 0;
    static final int study = 1;
    static final int entertainments = 2;
    static final int restaurants = 3;
    static final int report_user = 4;
    static final int report_comment = 5;
    static final int report_facility = 6;

    static final int normal_local_load = 0;
    static final int normal_server_load = 1;
    static final int reached_end = 2;
    static final int server_error = 3;
    static final int local_error = 4;
    static final int only_one_page = 5;

    static final String TAG = "EntertainmentsFragment";

    float transY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    boolean onSearch = false;   //if this true means user is viewing search result
    boolean isMenuOpen = false;

    private SearchView facilitySearchView;
    private DatabaseConnection DBconnection;
    private FragmentHomeBinding binding;
    private FloatingActionButton close_or_refresh, page_up, page_down, main;
    ConstraintLayout shows1, shows2, shows3, shows4, shows5;

    Spinner spin;

    private int search_page_number = 1;
    private int newest_page_number = 1;
    private boolean reached_end_newest = false;
    private boolean reached_end_search = false, one_page = false;

    private static String[] countryNames={"Posts","Eat","Study","Play"};
    private static int flags[] = {R.drawable.ic_menu_posts, R.drawable.ic_menu_restaurants, R.drawable.ic_menu_study, R.drawable.ic_menu_entertainment};

    private int facility_type = posts;
    private String facility_id = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DBconnection = new DatabaseConnection();
        DBconnection.cleanCaches(getContext());

        shows1 = binding.facility1;
        shows2 = binding.facility2;
        shows3 = binding.facility3;
        shows4 = binding.facility4;
        shows5 = binding.facility5;

        //set up spinner
        spin = binding.spinnerFacility;
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search_page_number = 1;
                newest_page_number = 1;
                one_page = false;
                facility_type = getTypeInt(countryNames[position]);
                if (facility_type == posts){
                    binding.ratingBarFacility1.setVisibility(View.INVISIBLE);
                    binding.ratingBarFacility2.setVisibility(View.INVISIBLE);
                    binding.ratingBarFacility3.setVisibility(View.INVISIBLE);
                    binding.ratingBarFacility4.setVisibility(View.INVISIBLE);
                    binding.ratingBarFacility5.setVisibility(View.INVISIBLE);
                }else {
                    binding.ratingBarFacility1.setVisibility(View.VISIBLE);
                    binding.ratingBarFacility2.setVisibility(View.VISIBLE);
                    binding.ratingBarFacility3.setVisibility(View.VISIBLE);
                    binding.ratingBarFacility4.setVisibility(View.VISIBLE);
                    binding.ratingBarFacility5.setVisibility(View.VISIBLE);
                }
                setFacilitiesVisibility(View.INVISIBLE);
                Log.d(TAG, "facility_type in onItemSelected"+facility_type);
                int result = -1;
                result =  DBconnection.getFacilities(binding, facility_type, 1, getContext(), false, false, "");
                Log.d(TAG, "initial result is : " + result);
                if (result == server_error){
                    Toast.makeText(getContext(), "Error happened when connecting to server, please exist", Toast.LENGTH_SHORT).show();
                }else if (result == only_one_page || result == reached_end){
                    one_page = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        CustomAdapter customAdapter = new CustomAdapter(getContext(),flags,countryNames);
        spin.setAdapter(customAdapter);

        //load initial page

        //set up search function
        facilitySearchView = binding.searchFacility;
        facilitySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setFacilitiesVisibility(View.INVISIBLE);
                close_or_refresh.setImageResource(R.drawable.ic_baseline_close_24);
                Log.d(TAG, "searching: " + query);
                onSearch = true;
                one_page = false;
                int result = DBconnection.getFacilities(binding, facility_type, 1, getContext(), true, false, query);
                if (result == normal_local_load) {
                    Log.d(TAG, "Load data from local device");
                } else if (result == normal_server_load) {
                    Log.d(TAG, "Load data from server");
                } else if (result == local_error) {
                    Log.d(TAG, "ERROR Load data from local device");
                    Toast.makeText(getContext(), "Error happened when loading data, please exist", Toast.LENGTH_SHORT).show();
                } else if (result == server_error) {
                    Log.d(TAG, "ERROR Load data from server");
                    Toast.makeText(getContext(), "Error happened when connecting to server, please exist", Toast.LENGTH_SHORT).show();
                } else if (result == reached_end) {
                    Log.d(TAG, "load finished");
                } else if (result == only_one_page){
                    one_page = true;
                }
                search_page_number = 1;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        setConsOnCl();

        //set up p fabs
        initFavMenu();
        return root;
    }

    private void setFacilitiesVisibility(int Visibility){
        shows1.setVisibility(Visibility);
        shows2.setVisibility(Visibility);
        shows3.setVisibility(Visibility);
        shows4.setVisibility(Visibility);
        shows5.setVisibility(Visibility);

    }

    private void setRateBarVisibility(int Visibility){
        binding.ratingBarFacility1.setVisibility(Visibility);
        binding.ratingBarFacility2.setVisibility(Visibility);
        binding.ratingBarFacility3.setVisibility(Visibility);
        binding.ratingBarFacility4.setVisibility(Visibility);
        binding.ratingBarFacility5.setVisibility(Visibility);

    }

    private void setConsOnCl(){
        shows1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ConstraintLayoutOnClickListener(1);}
        });

        shows2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayoutOnClickListener(2);
            }
        });

        shows3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayoutOnClickListener(3);
            }
        });

        shows4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayoutOnClickListener(4);
            }
        });

        shows5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayoutOnClickListener(5);
            }
        });
    }

    private void ConstraintLayoutOnClickListener(int which){
        switch (which){
            case 1:
                facility_id = binding.facilityIDTextViewFacility1.getText().toString();
                break;
            case 2:
                facility_id = binding.facilityIDTextViewFacility2.getText().toString();
                break;
            case 3:
                facility_id = binding.facilityIDTextViewFacility3.getText().toString();
                break;
            case 4:
                facility_id = binding.facilityIDTextViewFacility4.getText().toString();
                break;
            case 5:
                facility_id = binding.facilityIDTextViewFacility5.getText().toString();
                break;
        }

        String url = "http://20.213.243.141:8000/specific";
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        queue.start();
        params.put("facility_id", facility_id);
        params.put("facility_type", String.valueOf(facility_type));
        Log.d(TAG, "eeee: "+params.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent intent = new Intent(getActivity(), FacilityActivity.class);
                Log.d(TAG, "response is: " + response.toString());
                Bundle bundle = new Bundle();
                bundle.putInt("facility_type", facility_type);
                bundle.putString("facility_id", facility_id);
                bundle.putString("facility_json", response.toString());
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+bundle.getInt("facility_type"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "ERROR when connecting to database getSpecificFacility: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
    }

    private void initFavMenu(){
        close_or_refresh = binding.fabCloseOrRefresh;
        page_up = binding.fabPrevious;
        page_down = binding.fabNext;
        main = binding.fabMain;

        close_or_refresh.setAlpha(0f);
        page_up.setAlpha(0f);
        page_down.setAlpha(0f);

        close_or_refresh.setTranslationY(transY);
        page_up.setTranslationY(transY);
        page_down.setTranslationY(transY);

        close_or_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBconnection.cleanCaches(getContext());
                search_page_number = 1;
                one_page = false;
                setFacilitiesVisibility(View.INVISIBLE);
                if(onSearch){
                    onSearch = false;
                    close_or_refresh.setImageResource(R.drawable.ic_baseline_refresh_24);
                    facilitySearchView.setQuery("", false);
                    facilitySearchView.clearFocus();
                }
                int result = DBconnection.getFacilities(binding, facility_type, 1, getContext(), false, false, "");
                if(result == only_one_page || result == reached_end){
                    one_page = true;
                }
            }
        });

        page_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearch) {
                    if (search_page_number == 1 || one_page) {
                        reached_end_search = false;
                        Toast.makeText(getContext(), "You are already on first page", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setFacilitiesVisibility(View.INVISIBLE);
                    search_page_number -= 1;
                    int result = DBconnection.getFacilities(binding, facility_type, search_page_number, getContext(), true, false, "");
                    if (result == local_error) {
                        search_page_number = 1;
                        Toast.makeText(getContext(), "Error happened when loading data, please exist", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "down page Error happened when loading data, please exist");
                    } else if (result == reached_end) {
                        search_page_number = 1;
                        Log.d(TAG, "down page load all");
                    } else if(result == only_one_page ){
                        one_page = true;
                    }
                    Log.d(TAG, "1 result is :" + result);
                    Log.d(TAG, "1 search_page_number is :" + search_page_number);
                } else {
                    if (newest_page_number == 1 || one_page) {
                        reached_end_newest = false;
                        Toast.makeText(getContext(), "You are already on first page", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setFacilitiesVisibility(View.INVISIBLE);
                    newest_page_number -= 1;
                    int result = DBconnection.getFacilities(binding, facility_type, newest_page_number, getContext(), false, false, "");
                    if (result == local_error) {
                        newest_page_number = 1;
                        Toast.makeText(getContext(), "Error happened when loading data, please exist", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "down page Error happened when loading data, please exist");
                    } else if (result == reached_end) {
                        newest_page_number = 1;
                        Log.d(TAG, "down page load all");
                    } else if(result == only_one_page ){
                        one_page = true;
                    }
                    Log.d(TAG, "1 result is :" + result);
                    Log.d(TAG, "1 newest_page_number is :" + newest_page_number);
                }
            }
        });

        page_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearch) {
                    if (one_page) {
                        search_page_number = 1;
                        DBconnection.getFacilities(binding, facility_type, 1, getContext(), true, false, "");
                        Toast.makeText(getContext(), "No more facility to show", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (reached_end_search) {
                        reached_end_search = false;
                        search_page_number = 1;
                    } else {
                        search_page_number++;
                    }
                    setFacilitiesVisibility(View.INVISIBLE);
                    int result = DBconnection.getFacilities(binding, facility_type, search_page_number, getContext(), true, false, "");
                    if (result == local_error) {
                        reached_end_search = true;
                        Toast.makeText(getContext(), "Error happened when loading data, please exist", Toast.LENGTH_SHORT).show();
                    } else if (result == reached_end) {
                        reached_end_search = true;
                    } else if(result == only_one_page ){
                        one_page = true;
                    }
                    Log.d(TAG, "2 result is :" + result);
                    Log.d(TAG, "2 search_page_number is :" + search_page_number);
                    Log.d(TAG, "2 reached_end_search is :" + reached_end_search);
                } else {
                    if (one_page) {
                        newest_page_number = 1;
                        DBconnection.getFacilities(binding, facility_type, 1, getContext(), false, false, "");
                        Toast.makeText(getContext(), "No more facility to show", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (reached_end_newest) {
                        reached_end_newest = false;
                        newest_page_number = 1;
                    } else {
                        newest_page_number++;
                    }
                    setFacilitiesVisibility(View.INVISIBLE);
                    int result = DBconnection.getFacilities(binding, facility_type, newest_page_number, getContext(), false, false, "");
                    if (result == local_error) {
                        reached_end_newest = true;
                        Toast.makeText(getContext(), "Error happened when loading data, please exist", Toast.LENGTH_SHORT).show();
                    } else if (result == reached_end) {
                        reached_end_newest = true;
                    }else if(result == only_one_page ){
                        one_page = true;
                    }
                    Log.d(TAG, "2 result is :" + result);
                    Log.d(TAG, "2 newest_page_number is :" + newest_page_number);
                    Log.d(TAG, "2 reached_end_search is :" + reached_end_newest);
                }
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSearch){
                    close_or_refresh.setImageResource(R.drawable.ic_baseline_close_24);
                }else {
                    close_or_refresh.setImageResource(R.drawable.ic_baseline_refresh_24);
                }

                if(isMenuOpen){
                    closeMenu();
                }else {
                    openMenu();
                }
            }
        });
    }

    private void openMenu(){
        isMenuOpen = !isMenuOpen;
        main.animate().setInterpolator(interpolator).rotation(180f).setDuration(300).start();
        close_or_refresh.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        page_up.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        page_down.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

    }

    private void closeMenu(){
        isMenuOpen = !isMenuOpen;
        main.animate().setInterpolator(interpolator).rotation(0).setDuration(300).start();
        close_or_refresh.animate().translationY(transY).alpha(0).setInterpolator(interpolator).setDuration(300).start();
        page_up.animate().translationY(transY).alpha(0).setInterpolator(interpolator).setDuration(300).start();
        page_down.animate().translationY(transY).alpha(0).setInterpolator(interpolator).setDuration(300).start();
    }

    private int getTypeInt(String selected){
        switch (selected){
            case "Play":
                return entertainments;
            case "Eat":
                return restaurants;
            case "Study":
                return study;
            case "Posts":
                return posts;
        }
        return -1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}