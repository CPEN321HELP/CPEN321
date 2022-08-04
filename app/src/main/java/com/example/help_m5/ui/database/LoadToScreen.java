package com.example.help_m5.ui.database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.help_m5.R;
import com.example.help_m5.databinding.FragmentBrowseBinding;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoadToScreen {
    private final String TAG = "LoadToScreen";
    private int admin = 0;

    @SuppressLint("SetTextI18n")
    public void loadUserInfo(NavigationView navigationView, JSONObject user_data, Activity activity){

        String userName = "error";
        String userEmail = "error";
        String userLogo = "error";
        int numberCredit = -1;
        int userType = -1;

        try {
            userName = user_data.getString("username");
            userEmail = user_data.getString("_id");
            userLogo = user_data.getString("user_logo");
            userType = user_data.getInt("account_type");
            numberCredit = user_data.getInt("number_of_credit");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView userNameView = navigationView.getHeaderView(0).findViewById(R.id.userName);
//        TextView userNameView = activity.findViewById(R.id.userName);

        userNameView.setText(userName);

        TextView userEmailView = navigationView.getHeaderView(0).findViewById(R.id.userEmail);
//        TextView userEmailView = activity.findViewById(R.id.userEmail);
        userEmailView.setText(userEmail);

        if (!userLogo.equals("none")) {
            Uri userIcon = Uri.parse(userLogo);
            Picasso.get().load(userIcon).into((CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.userIcon));
//            Picasso.get().load(userIcon).into((ImageView) activity.findViewById(R.id.userIcon));
        } else {
            CircleImageView userIconView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.userIcon);
            userIconView.setImageResource(R.drawable.user_logo);
        }

        if(userType != -1 && numberCredit != -1){
            if (userType == admin){
                ImageView logo = navigationView.getHeaderView(0).findViewById(R.id.userLevelLogo);
//                ImageView logo = activity.findViewById(R.id.userLevelLogo);
                assert(logo != null);
                logo.setImageResource(R.drawable.ic_baseline_admin_panel_settings_24);

                TextView userCreditView = navigationView.getHeaderView(0).findViewById(R.id.numberOfCredit);
                userNameView.setText(userName+ " (admin)");
//                TextView userCreditView = activity.findViewById(R.id.numberOfCredit);
                userCreditView.setText("Number of Credits: "+numberCredit);

            }else {// not admin
                ImageView logo = navigationView.getHeaderView(0).findViewById(R.id.userLevelLogo);
//                ImageView logo = activity.findViewById(R.id.userLevelLogo);
                int user_level = numberCredit / 50 + 1;
                switch (user_level){
                    case 1:
                        logo.setImageResource(R.drawable.level_1_logo);
                        break;
                    case 2:
                        logo.setImageResource(R.drawable.level_2_logo);
                        break;
                    case 3:
                        logo.setImageResource(R.drawable.level_3_logo);
                        break;
                    case 4:
                        logo.setImageResource(R.drawable.level_4_logo);
                        break;
                    case 5:
                        logo.setImageResource(R.drawable.level_5_logo);
                        break;
                    case 6:
                        logo.setImageResource(R.drawable.level_6_logo);
                        break;
                    case 7:
                        logo.setImageResource(R.drawable.level_7_logo);
                        break;
                    case 8:
                        logo.setImageResource(R.drawable.level_8_logo);
                        break;
                    case 9:
                        logo.setImageResource(R.drawable.level_9_logo);
                        break;
                    default:
                        logo.setImageResource(R.drawable.level_9plus_logo);
                        break;
                }

                TextView userCreditView = navigationView.getHeaderView(0).findViewById(R.id.numberOfCredit);
//                TextView userCreditView = activity.findViewById(R.id.numberOfCredit);
                userCreditView.setText("Number of Credits: "+numberCredit);
            }
        }
        Log.d(TAG, "Finish load all");
    }

    /**
     * @param binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_info : a json array that holds information about facilities
     * @param index         : a int index range from 0 to 5
     * @Pupose load information from JSONArray to texView
     */
    public void loadToFragment(Object binding, JSONArray facility_info, int index) {
        switch (index) {
            case 0:
                load_facility1(binding, facility_info);
                break;
            case 1:
                load_facility2(binding, facility_info);
                break;
            case 2:
                load_facility3(binding, facility_info);
                break;
            case 3:
                load_facility4(binding, facility_info);
                break;
            case 4:
                load_facility5(binding, facility_info);
                break;
            default:
                break;
        }
    }

    /**
     * @param Binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    @SuppressLint("SetTextI18n")
    private void load_facility1(Object Binding, JSONArray result) {
        TextView titleTextView_facility1;
        TextView dateTextView_facility1;
        TextView contentTextView_facility1;
        TextView facilityID_textView1_facility1;
        RatingBar ratingBar_facility1;
        ConstraintLayout constraintLayout_facility1;

        titleTextView_facility1 = ((FragmentBrowseBinding) Binding).titleTextViewFacility1;
        dateTextView_facility1 = ((FragmentBrowseBinding) Binding).dateTextViewFacility1;
        contentTextView_facility1 = ((FragmentBrowseBinding) Binding).contentTextViewFacility1;
        ratingBar_facility1 = ((FragmentBrowseBinding) Binding).ratingBarFacility1;
        facilityID_textView1_facility1 = ((FragmentBrowseBinding) Binding).facilityIDTextViewFacility1;
        constraintLayout_facility1 = ((FragmentBrowseBinding) Binding).facility1;

        try {
            facilityID_textView1_facility1.setText(result.getString(0));
        } catch (JSONException E) {
            facilityID_textView1_facility1.setText("ERROR when loading title 1");
        }

        try {
            ratingBar_facility1.setRating((float) result.getDouble(1));
        } catch (JSONException E) {
            ratingBar_facility1.setRating((float) 0);
        }

        try {
            titleTextView_facility1.setText(result.getString(2));
        } catch (JSONException E) {
            titleTextView_facility1.setText("ERROR when loading title 1");
        }

        try {
            contentTextView_facility1.setText(result.getString(3));
        } catch (JSONException E) {
            contentTextView_facility1.setText("ERROR when loading title 1");
        }

        try {
            dateTextView_facility1.setText(result.getString(4));
        } catch (JSONException E) {
            dateTextView_facility1.setText("ERROR when loading date 1");
        }
        constraintLayout_facility1.setVisibility(View.VISIBLE);
    }

    /**
     * @param Binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    @SuppressLint("SetTextI18n")
    private void load_facility2(Object Binding, JSONArray result ) {
        TextView titleTextView_facility2;
        TextView dateTextView_facility2;
        TextView contentTextView_facility2;
        TextView facilityID_textView1_facility2;
        RatingBar ratingBar_facility2;
        ConstraintLayout constraintLayout_facility2;

        titleTextView_facility2 = ((FragmentBrowseBinding) Binding).titleTextViewFacility2;
        dateTextView_facility2 = ((FragmentBrowseBinding) Binding).dateTextViewFacility2;
        contentTextView_facility2 = ((FragmentBrowseBinding) Binding).contentTextViewFacility2;
        ratingBar_facility2 = ((FragmentBrowseBinding) Binding).ratingBarFacility2;
        facilityID_textView1_facility2 = ((FragmentBrowseBinding) Binding).facilityIDTextViewFacility2;
        constraintLayout_facility2 = ((FragmentBrowseBinding) Binding).facility2;

        try {
            facilityID_textView1_facility2.setText(result.getString(0));
        } catch (JSONException E) {
            facilityID_textView1_facility2.setText("ERROR when loading title 1");
        }

        try {
            ratingBar_facility2.setRating((float) result.getDouble(1));
        } catch (JSONException E) {
            ratingBar_facility2.setRating((float) 0);
        }

        try {
            titleTextView_facility2.setText(result.getString(2));
        } catch (JSONException E) {
            titleTextView_facility2.setText("ERROR when loading title 1");
        }

        try {
            contentTextView_facility2.setText(result.getString(3));
        } catch (JSONException E) {
            contentTextView_facility2.setText("ERROR when loading title 1");
        }

        try {
            dateTextView_facility2.setText(result.getString(4));
        } catch (JSONException E) {
            dateTextView_facility2.setText("ERROR when loading date 1");
        }
        constraintLayout_facility2.setVisibility(View.VISIBLE);
    }

    /**
     * @param Binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    @SuppressLint("SetTextI18n")
    private void load_facility3(Object Binding, JSONArray result) {
        TextView titleTextView_facility3;
        TextView dateTextView_facility3;
        TextView contentTextView_facility3;
        TextView facilityID_textView1_facility3;
        RatingBar ratingBar_facility3;
        ConstraintLayout constraintLayout_facility3;

        titleTextView_facility3 = ((FragmentBrowseBinding) Binding).titleTextViewFacility3;
        dateTextView_facility3 = ((FragmentBrowseBinding) Binding).dateTextViewFacility3;
        contentTextView_facility3 = ((FragmentBrowseBinding) Binding).contentTextViewFacility3;
        ratingBar_facility3 = ((FragmentBrowseBinding) Binding).ratingBarFacility3;
        facilityID_textView1_facility3 = ((FragmentBrowseBinding) Binding).facilityIDTextViewFacility3;
        constraintLayout_facility3 = ((FragmentBrowseBinding) Binding).facility3;


        try {
            facilityID_textView1_facility3.setText(result.getString(0));
        } catch (JSONException E) {
            facilityID_textView1_facility3.setText("ERROR when loading title 1");
        }

        try {
            ratingBar_facility3.setRating((float) result.getDouble(1));
        } catch (JSONException E) {
            ratingBar_facility3.setRating((float) 0);
        }

        try {
            titleTextView_facility3.setText(result.getString(2));
        } catch (JSONException E) {
            titleTextView_facility3.setText("ERROR when loading title 1");
        }

        try {
            contentTextView_facility3.setText(result.getString(3));
        } catch (JSONException E) {
            contentTextView_facility3.setText("ERROR when loading title 1");
        }

        try {
            dateTextView_facility3.setText(result.getString(4));
        } catch (JSONException E) {
            dateTextView_facility3.setText("ERROR when loading date 1");
        }
        constraintLayout_facility3.setVisibility(View.VISIBLE);
    }

    /**
     * @param Binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    @SuppressLint("SetTextI18n")
    private void load_facility4(Object Binding, JSONArray result) {
        TextView titleTextView_facility4;
        TextView dateTextView_facility4;
        TextView contentTextView_facility4;
        TextView facilityID_textView1_facility4;
        RatingBar ratingBar_facility4;
        ConstraintLayout constraintLayout_facility4;

        titleTextView_facility4 = ((FragmentBrowseBinding) Binding).titleTextViewFacility4;
        dateTextView_facility4 = ((FragmentBrowseBinding) Binding).dateTextViewFacility4;
        contentTextView_facility4 = ((FragmentBrowseBinding) Binding).contentTextViewFacility4;
        ratingBar_facility4 = ((FragmentBrowseBinding) Binding).ratingBarFacility4;
        facilityID_textView1_facility4 = ((FragmentBrowseBinding) Binding).facilityIDTextViewFacility4;
        constraintLayout_facility4 = ((FragmentBrowseBinding) Binding).facility4;

        try {
            facilityID_textView1_facility4.setText(result.getString(0));
        } catch (JSONException E) {
            facilityID_textView1_facility4.setText("ERROR when loading title 1");
        }

        try {
            ratingBar_facility4.setRating((float) result.getDouble(1));
        } catch (JSONException E) {
            ratingBar_facility4.setRating((float) 0);
        }

        try {
            titleTextView_facility4.setText(result.getString(2));
        } catch (JSONException E) {
            titleTextView_facility4.setText("ERROR when loading title 1");
        }

        try {
            contentTextView_facility4.setText(result.getString(3));
        } catch (JSONException E) {
            contentTextView_facility4.setText("ERROR when loading title 1");
        }

        try {
            dateTextView_facility4.setText(result.getString(4));
        } catch (JSONException E) {
            dateTextView_facility4.setText("ERROR when loading date 1");
        }
        constraintLayout_facility4.setVisibility(View.VISIBLE);
    }

    /**
     * @param Binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    @SuppressLint("SetTextI18n")
    private void load_facility5(Object Binding, JSONArray result) {
        TextView titleTextView_facility5;
        TextView dateTextView_facility5;
        TextView contentTextView_facility5;
        TextView facilityID_textView1_facility5;
        RatingBar ratingBar_facility5;
        ConstraintLayout constraintLayout_facility5;

        titleTextView_facility5 = ((FragmentBrowseBinding) Binding).titleTextViewFacility5;
        dateTextView_facility5 = ((FragmentBrowseBinding) Binding).dateTextViewFacility5;
        contentTextView_facility5 = ((FragmentBrowseBinding) Binding).contentTextViewFacility5;
        ratingBar_facility5 = ((FragmentBrowseBinding) Binding).ratingBarFacility5;
        facilityID_textView1_facility5 = ((FragmentBrowseBinding) Binding).facilityIDTextViewFacility5;
        constraintLayout_facility5 = ((FragmentBrowseBinding) Binding).facility5;

        try {
            facilityID_textView1_facility5.setText(result.getString(0));
        } catch (JSONException E) {
            facilityID_textView1_facility5.setText("ERROR when loading title 1");
        }

        try {
            ratingBar_facility5.setRating((float) result.getDouble(1));
        } catch (JSONException E) {
            ratingBar_facility5.setRating((float) 0);
        }

        try {
            titleTextView_facility5.setText(result.getString(2));
        } catch (JSONException E) {
            titleTextView_facility5.setText("ERROR when loading title 1");
        }

        try {
            contentTextView_facility5.setText(result.getString(3));
        } catch (JSONException E) {
            contentTextView_facility5.setText("ERROR when loading title 1");
        }

        try {
            dateTextView_facility5.setText(result.getString(4));
        } catch (JSONException E) {
            dateTextView_facility5.setText("ERROR when loading date 1");
        }
        constraintLayout_facility5.setVisibility(View.VISIBLE);
    }
}