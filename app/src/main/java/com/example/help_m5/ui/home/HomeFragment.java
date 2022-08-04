package com.example.help_m5.ui.home;

//import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.help_m5.ChatActivity;
import com.example.help_m5.MainActivity;
import com.example.help_m5.R;
import com.example.help_m5.databinding.FragmentHomeBinding;
import com.example.help_m5.ui.add.AddFacilityFragment;
import com.example.help_m5.ui.browse.BrowseFragment;
import com.example.help_m5.ui.settings.SettingsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
//    private ActionBar actionBar;
    private final String TAG = "HomeFragment";

    public HomeFragment() {
        Log.d(TAG, "creating home Fragment");
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        assert account != null;
        String fullName = account.getDisplayName();
        int i = fullName.indexOf(' ');
        String firstName = fullName.substring(0, i);
        binding.homeUserName.setText(firstName);

        Date date = new Date();
        binding.homeDateTime.setText(date.toString().substring(0, 10));

//        actionBar = getActivity().getActionBar();
        binding.homeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Setting Browse");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.home_fragment_layout, new BrowseFragment(), "NewFragment TAG");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.getMenu().getItem(1).setChecked(true);
                navigationView.getMenu().getItem(0).setEnabled(true);
                getActionBar().setTitle("Browse");
            }
        });

        binding.homeAddFacilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Setting Add");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.home_fragment_layout, new AddFacilityFragment(), "NewFragment TAG");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.getMenu().getItem(3).setChecked(true);
                navigationView.getMenu().getItem(0).setEnabled(true);
                getActionBar().setTitle("Add Facility");
            }
        });

        binding.homeSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Setting Settings");
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.getMenu().getItem(5).setChecked(true);
                navigationView.getMenu().getItem(0).setEnabled(true);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.home_fragment_layout, new SettingsFragment(), "NewFragment TAG");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                getActionBar().setTitle("Settings");
            }
        });

        binding.homeChatbotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Setting ChatBot");
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.getMenu().getItem(0).setChecked(true);
                navigationView.getMenu().getItem(0).setEnabled(false);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private ActionBar getActionBar() {
        return ((MainActivity) requireActivity()).getSupportActionBar();
    }
}