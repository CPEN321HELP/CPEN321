package com.example.help_m5.menu;

import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.help_m5.spinner.CustomAdapter;
import com.example.help_m5.databinding.FragmentBrowseBinding;
import com.example.help_m5.database.DatabaseConnection;
import com.example.help_m5.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class BrowseFragment extends Fragment {

    final int posts = 0;
    final int study = 1;
    final int entertainments = 2;
    final int restaurants = 3;

    static final String TAG = "EntertainmentsFragment";

    float transY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    private static boolean  onSearch = false;   //if this true means user is viewing search result
    private static boolean isMenuOpen = false;
    private static int page = 1;
    private static String search_content;

    private SearchView facilitySearchView;
    private DatabaseConnection DBconnection;
    private @NonNull
    FragmentBrowseBinding binding;
    private FloatingActionButton close_or_refresh;
    private FloatingActionButton page_up;
    private FloatingActionButton page_down;
    private static boolean isLoadingFacility;
    Spinner spin;

    private final String[] countryNames={"Posts","Eat","Study","Play"};

    private int facility_type = posts;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBrowseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        isLoadingFacility = false;
        DBconnection = new DatabaseConnection();
        DBconnection.cleanAllCaches(getContext());  //disable this line for testing

        // Saving state of our app
        // using SharedPreferences
        SharedPreferences sharedPreferences
                = getActivity().getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);
        // When user reopens the app
        // after applying dark/light mode
        int[] flags;
        if (isDarkModeOn) {
            flags = new int[]{R.drawable.ic_baseline_post_24_white, R.drawable.ic_baseline_resturants_white, R.drawable.ic_baseline_menu_book_24_white, R.drawable.ic_baseline_videogame_asset_24_white};
        } else {
            flags = new int[]{R.drawable.ic_baseline_post__24, R.drawable.ic_menu_restaurants, R.drawable.ic_menu_study, R.drawable.ic_baseline_videogame_asset_24};
        }

        //set up spinner
        spin = binding.spinnerFacility;
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facility_type = getTypeInt(countryNames[position]);
                if (facility_type == posts){
                    setRateBarVisibility(View.GONE);
                    setDateVisibility(View.VISIBLE);
                    setViewBackground(R.drawable.posts_background);
                    setViewIcon(R.drawable.posts_image_browse);
                } else {
                    setRateBarVisibility(View.VISIBLE);
                    setDateVisibility(View.GONE);
                    if (facility_type == restaurants) {
                        setViewBackground(R.drawable.resturants_background);
                        setViewIcon(R.drawable.restaurants_image_browse);
                    } else if (facility_type == entertainments) {
                        setViewBackground(R.drawable.entertainments_background);
                        setViewIcon(R.drawable.entertainments_image_browse);
                        setTextColor("#DDFFFF");
                    } else if (facility_type == study) {
                        setViewBackground(R.drawable.studys_background);
                        setViewIcon(R.drawable.studys_image_browse);
                    }
                }
                setFacilitiesVisibility(View.INVISIBLE);
                Log.d(TAG, "facility_type in onItemSelected "+facility_type);
//                DBconnection.getFacilities(binding, facility_type, getContext(),false,"", false, false,false, 0);
                if(onSearch){
                    DBconnection.getFacilities(binding, facility_type, 0, getContext(),search_content, new boolean[]{true, false, false, false});
                }else {
                    DBconnection.getFacilities(binding, facility_type, 0, getContext(),"", new boolean[]{false, false, false, false});
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "nothing is selected");
            }
        });
        CustomAdapter customAdapter = new CustomAdapter(getContext(), flags, countryNames, 0);
        spin.setAdapter(customAdapter);

        //load initial page
        //set up search function
        facilitySearchView = binding.searchFacility;
        facilitySearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facilitySearchView.setIconified(false);
            }
        });
        facilitySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()<1){
                    DBconnection.getFacilities(binding, facility_type, 0, getContext(), query, new boolean[]{false, false, false, false});
                    onSearch = false;
                    close_or_refresh.setImageResource(R.drawable.ic_baseline_close_24);
                    close_or_refresh.setTag("refresh");
                    return false;
                }
                Log.d(TAG, "onQueryTextSubmit in onResume: "+onSearch);

                search_content = query;
                DBconnection.cleanSearchCaches(getContext());
                setFacilitiesVisibility(View.INVISIBLE);
                close_or_refresh.setImageResource(R.drawable.ic_baseline_refresh_24);
                close_or_refresh.setTag("close");
                Log.d(TAG, "searching: " + query);
                onSearch = true;
//                DBconnection.getFacilities(binding, facility_type, getContext(),true, query, false, false,false, 0);
                DBconnection.getFacilities(binding, facility_type, 0, getContext(), query, new boolean[]{true, false, false, false});

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()<1){
                    DBconnection.getFacilities(binding, facility_type, 0, getContext(), newText, new boolean[]{false, false, false, false});
                    onSearch = false;
                    close_or_refresh.setImageResource(R.drawable.ic_baseline_refresh_24);
                    close_or_refresh.setTag("refresh");
                    return false;
                }
                Log.d(TAG, "onQueryTextChange in onResume: "+onSearch);

                search_content = newText;
                DBconnection.cleanSearchCaches(getContext());
                setFacilitiesVisibility(View.INVISIBLE);
                close_or_refresh.setImageResource(R.drawable.ic_baseline_close_24);
                close_or_refresh.setTag("close");
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

    private void setDateVisibility(int Visibility){
        binding.dateTextViewFacility1.setVisibility(Visibility);
        binding.dateTextViewFacility2.setVisibility(Visibility);
        binding.dateTextViewFacility3.setVisibility(Visibility);
        binding.dateTextViewFacility4.setVisibility(Visibility);
        binding.dateTextViewFacility5.setVisibility(Visibility);
    }

    private void setViewBackground(int drawable){
        binding.ViewFacility1.setBackground(ContextCompat.getDrawable(getContext(), drawable));
        binding.ViewFacility2.setBackground(ContextCompat.getDrawable(getContext(), drawable));
        binding.ViewFacility3.setBackground(ContextCompat.getDrawable(getContext(), drawable));
        binding.ViewFacility4.setBackground(ContextCompat.getDrawable(getContext(), drawable));
        binding.ViewFacility5.setBackground(ContextCompat.getDrawable(getContext(), drawable));
    }

    private void setViewIcon(int drawable){
        binding.facilityImageview1.setImageResource(drawable);
        binding.facilityImageview2.setImageResource(drawable);
        binding.facilityImageview3.setImageResource(drawable);
        binding.facilityImageview4.setImageResource(drawable);
        binding.facilityImageview5.setImageResource(drawable);
    }

    private void setTextColor(String colorCode){
        binding.contentTextViewFacility1.setTextColor(Color.parseColor(colorCode));
        binding.contentTextViewFacility2.setTextColor(Color.parseColor(colorCode));
        binding.contentTextViewFacility3.setTextColor(Color.parseColor(colorCode));
        binding.contentTextViewFacility4.setTextColor(Color.parseColor(colorCode));
        binding.contentTextViewFacility5.setTextColor(Color.parseColor(colorCode));
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
        if(isLoadingFacility){// if is loading, then do not load again
            Toast.makeText(getContext(), "Loading! Don't click again!", Toast.LENGTH_SHORT).show();
        }else {
            isLoadingFacility = true;
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
        }
    }

    private void initFavMenu(){
        close_or_refresh = binding.fabCloseOrRefresh;
        page_up = binding.fabPrevious;
        page_down = binding.fabNext;
        FloatingActionButton main = binding.fabMain;

        close_or_refresh.setAlpha(0f);
        page_up.setAlpha(0f);
        page_down.setAlpha(0f);

        close_or_refresh.setTranslationY(transY);
        page_up.setTranslationY(transY);
        page_down.setTranslationY(transY);

        page_up.setTag("hide");
        page_down.setTag("hide");

        close_or_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoadingFacility = false;
                Log.d(TAG, "close_or_refresh in onResume: "+onSearch);

                DBconnection.cleanAllCaches(getContext()); //disable this line for testing
                setFacilitiesVisibility(View.INVISIBLE);
                if(onSearch){
                    onSearch = false;
                    close_or_refresh.setImageResource(R.drawable.ic_baseline_refresh_24);
                    close_or_refresh.setTag("refresh");

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
                    close_or_refresh.setTag("close");
                }else {
                    close_or_refresh.setImageResource(R.drawable.ic_baseline_refresh_24);
                    close_or_refresh.setTag("refresh");
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
//        binding.main.animate().setInterpolator(interpolator).rotation(180f).setDuration(300).start();
        close_or_refresh.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        page_up.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        page_up.setTag("show");
        page_down.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        page_down.setTag("show");
    }

    private void closeMenu(){
        isMenuOpen = !isMenuOpen;
//        binding.main.animate().setInterpolator(interpolator).rotation(0).setDuration(300).start();
        close_or_refresh.animate().translationY(transY).alpha(0).setInterpolator(interpolator).setDuration(300).start();
        page_up.animate().translationY(transY).alpha(0).setInterpolator(interpolator).setDuration(300).start();
        page_up.setTag("hide");
        page_down.animate().translationY(transY).alpha(0).setInterpolator(interpolator).setDuration(300).start();
        page_down.setTag("hide");
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
        String username = account.getDisplayName();
        Uri user_logoUri = account.getPhotoUrl();
        String user_logo = "";
        if(user_logoUri != null){
            user_logo = account.getPhotoUrl().toString();
        }else {
            user_logo = "none";

        }
        db.updateUserInfo(navigationView, getContext(),user_email,username,user_logo,getActivity(),true);
    }

    @Override
    public void onResume(){
        isLoadingFacility = false;
        Log.d(TAG, "onSearch in onResume: "+onSearch);
        super.onResume();
        selfUpdate();
        updateCredit(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}