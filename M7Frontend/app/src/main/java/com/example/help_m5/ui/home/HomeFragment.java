package com.example.help_m5.ui.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.help_m5.CustomAdapter;
import com.example.help_m5.databinding.FragmentHomeBinding;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.example.help_m5.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomeFragment extends Fragment {

    static final int posts = 0;
    static final int study = 1;
    static final int entertainments = 2;
    static final int restaurants = 3;


    static final String TAG = "EntertainmentsFragment";

    float transY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    boolean onSearch = false;   //if this true means user is viewing search result
    boolean isMenuOpen = false;

    private SearchView facilitySearchView;
    private DatabaseConnection DBconnection;
    private FragmentHomeBinding binding;
    private FloatingActionButton close_or_refresh;
    private FloatingActionButton page_up;
    private FloatingActionButton page_down;
    private FloatingActionButton main;

    Spinner spin;

    private final String[] countryNames={"Posts","Eat","Study","Play"};
    private final int[] flags = {R.drawable.ic_menu_posts, R.drawable.ic_menu_restaurants, R.drawable.ic_menu_study, R.drawable.ic_menu_entertainment};

    private int facility_type = posts;
    private int page = 1;
    private String search_content;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DBconnection = new DatabaseConnection();
        DBconnection.cleanAllCaches(getContext());

        //set up spinner
        spin = binding.spinnerFacility;
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facility_type = getTypeInt(countryNames[position]);
                if (facility_type == posts){
                    setRateBarVisibility(View.INVISIBLE);
                }else {
                    setRateBarVisibility(View.VISIBLE);
                }
                setFacilitiesVisibility(View.INVISIBLE);
                Log.d(TAG, "facility_type in onItemSelected "+facility_type);
//                DBconnection.getFacilities(binding, facility_type, getContext(),false,"", false, false,false, 0);
                DBconnection.getFacilities(binding, facility_type, 0, getContext(),"", new boolean[]{false, false, false, false});

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "nothing is selected");

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
                Log.d(TAG, "onQueryTextSubmit in onResume: "+onSearch);

                search_content = query;
                DBconnection.cleanSearchCaches(getContext());
                setFacilitiesVisibility(View.INVISIBLE);
                close_or_refresh.setImageResource(R.drawable.ic_baseline_close_24);
                Log.d(TAG, "searching: " + query);
                onSearch = true;
//                DBconnection.getFacilities(binding, facility_type, getContext(),true, query, false, false,false, 0);
                DBconnection.getFacilities(binding, facility_type, 0, getContext(), query, new boolean[]{true, false, false, false});

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange in onResume: "+onSearch);

                search_content = newText;
                DBconnection.cleanSearchCaches(getContext());
                setFacilitiesVisibility(View.INVISIBLE);
                close_or_refresh.setImageResource(R.drawable.ic_baseline_close_24);
                Log.d(TAG, "searching: " + newText);
                onSearch = true;
//                DBconnection.getFacilities(binding, facility_type, getContext(),true, newText, false, false,false, 0);
                DBconnection.getFacilities(binding, facility_type, 0, getContext(), newText, new boolean[]{true, false, false, false});

                return false;
            }
        });
        setConsOnCl();
        //set up p fabs
        initFavMenu();
        return root;
    }

    private void setFacilitiesVisibility(int Visibility){
        binding.facility1.setVisibility(Visibility);
        binding.facility2.setVisibility(Visibility);
        binding.facility3.setVisibility(Visibility);
        binding.facility4.setVisibility(Visibility);
        binding.facility5.setVisibility(Visibility);
    }

    private void setRateBarVisibility(int Visibility){
        binding.ratingBarFacility1.setVisibility(Visibility);
        binding.ratingBarFacility2.setVisibility(Visibility);
        binding.ratingBarFacility3.setVisibility(Visibility);
        binding.ratingBarFacility4.setVisibility(Visibility);
        binding.ratingBarFacility5.setVisibility(Visibility);

    }

    private void setConsOnCl(){
        binding.facility1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ConstraintLayoutOnClickListener(1);}
        });

        binding.facility2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayoutOnClickListener(2);
            }
        });

        binding.facility3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayoutOnClickListener(3);
            }
        });

        binding.facility4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayoutOnClickListener(4);
            }
        });

        binding.facility5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayoutOnClickListener(5);
            }
        });
    }

    private void ConstraintLayoutOnClickListener(int which){
        String facility_id;
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
            default:
                return;
        }
        DBconnection.getSpecificFacility(facility_type, facility_id, getContext(), getActivity());

//        String url = "specific";
//        final RequestQueue queue = Volley.newRequestQueue(getContext());
//        HashMap<String, String> params = new HashMap<String, String>();
//        queue.start();
//        params.put("facility_id", facility_id);
//        params.put("facility_type", String.valueOf(facility_type));
//        Log.d(TAG, "eeee: "+params.toString());
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Intent intent = new Intent(getActivity(), FacilityActivity.class);
//                Log.d(TAG, "response is: " + response.toString());
//                Bundle bundle = new Bundle();
//                bundle.putInt("facility_type", facility_type);
//                bundle.putString("facility_id", facility_id);
//                bundle.putString("facility_json", response.toString());
//                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+bundle.getInt("facility_type"));
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(), "ERROR when connecting to database getSpecificFacility: " + error, Toast.LENGTH_SHORT).show();
//            }
//        });
//        queue.add(jsObjRequest);
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
                Log.d(TAG, "close_or_refresh in onResume: "+onSearch);

                DBconnection.cleanAllCaches(getContext());
                setFacilitiesVisibility(View.INVISIBLE);
                if(onSearch){
                    onSearch = false;
                    close_or_refresh.setImageResource(R.drawable.ic_baseline_refresh_24);
                    facilitySearchView.setQuery("", false);
                    facilitySearchView.clearFocus();
                }
//                DBconnection.getFacilities(binding, facility_type, getContext(),false, "", false, false, false, 0);
                DBconnection.getFacilities(binding, facility_type, 0, getContext(),"", new boolean[]{false, false, false, false});

            }
        });

        page_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "page_up in onResume: "+onSearch);

                if (onSearch) {
//                    DBconnection.getFacilities(binding, facility_type, getContext(),true, "", false, true,false, 0);
                    DBconnection.getFacilities(binding, facility_type, 0, getContext(),"", new boolean[]{true, false, true, false});
                    page = DBconnection.getCurrentPage(getContext(),true, facility_type);
                    Log.d(TAG, "current page: "+ DBconnection.getCurrentPage(getContext(),true, facility_type));
                } else {
//                    DBconnection.getFacilities(binding, facility_type, getContext(),false, "", false, true,false, 0);
                    DBconnection.getFacilities(binding, facility_type, 0, getContext(),"", new boolean[]{false, false, true, false});
                    page = DBconnection.getCurrentPage(getContext(),false, facility_type);
                    Log.d(TAG, "current page: "+ DBconnection.getCurrentPage(getContext(),false, facility_type));
                }
            }
        });

        page_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "page_down in onResume: "+onSearch);

                if (onSearch) {
//                    DBconnection.getFacilities(binding, facility_type, getContext(),true, "", true, false,false, 0);
                    DBconnection.getFacilities(binding, facility_type, 0, getContext(),"", new boolean[]{true, true, false, false});
                    page = DBconnection.getCurrentPage(getContext(),true, facility_type);
                    Log.d(TAG, "current page: "+ DBconnection.getCurrentPage(getContext(),true, facility_type));

                } else {
//                    DBconnection.getFacilities(binding, facility_type, getContext(),false, "", true, false,false, 0);
                    DBconnection.getFacilities(binding, facility_type, 0, getContext(),"", new boolean[]{false, true, false, false});
                    page = DBconnection.getCurrentPage(getContext(),false, facility_type);
                    Log.d(TAG, "current page: "+ DBconnection.getCurrentPage(getContext(),false, facility_type));

                }
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "main in onResume: "+onSearch);

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
            default:
                return -1;
        }
    }

    private void selfUpdate(){
        if(onSearch){
            Log.d(TAG, "current page selfUpdate: "+page);
//            DBconnection.getFacilities(binding, facility_type, getContext(),true, search_content, false, false, true, page);
            DBconnection.getFacilities(binding, facility_type, page, getContext(), search_content, new boolean[]{true, false, false, true});
        }else {
            Log.d(TAG, "current page selfUpdate: "+page);
//            DBconnection.getFacilities(binding, facility_type, getContext(),false, "", false, false, true, page);
            DBconnection.getFacilities(binding, facility_type, page, getContext(),"", new boolean[]{false, false, false, true});
        }
    }

    private void updateCredit(Context context){
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        DatabaseConnection db = new DatabaseConnection();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if(account == null){
            return;
        }
        String user_email = account.getEmail();
        db.updateUserInfo(navigationView, getContext(),user_email,getActivity(),true);
    }

    @Override
    public void onResume(){
        Log.d(TAG, "onSearch in onResume: "+onSearch);
        super.onResume();
        updateCredit(getContext());
        selfUpdate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}