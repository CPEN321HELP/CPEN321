package com.example.help_m5.ui.database;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.help_m5.databinding.FragmentHomeBinding;
import com.example.help_m5.databinding.FragmentReportBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadToScreen {

    /**
     * @param binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param page_number   : what range to load
     * @param data          : Json format data to be process and show on screen
     * @return 0 execute as expected, 1 errer when reading json
     * @Pupose : to load the content from server our cached file to screen for user to view
     */
    public int loadToScreen(Object binding, int facility_type, int page_number, JSONObject data, boolean isReport) {
        try {
            int length = data.getInt("length");
            int start = (page_number - 1) * 5;
            int end = Math.min((page_number * 5), length);
            int counter = 0;
            JSONArray array = data.getJSONArray("result");

            for (int index = start; index < end; index++) {
                loadToFragment(binding, facility_type, array.getJSONArray(index), counter, isReport);
                counter++;
            }
            if(length<=5){
                return 2;
            }else if (end == length) {
                return 1;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    /**
     * @param binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param facility_info : a json array that holds information about facilities
     * @param index         : a int index range from 0 to 5
     * @Pupose load information from JSONArray to texView
     */
    private void loadToFragment(Object binding, int facility_type, JSONArray facility_info, int index, boolean isReport) {
        switch (index) {
            case 0:
                load_facility1(binding, facility_type, facility_info, isReport);
                break;
            case 1:
                load_facility2(binding, facility_type, facility_info, isReport);
                break;
            case 2:
                load_facility3(binding, facility_type, facility_info, isReport);
                break;
            case 3:
                load_facility4(binding, facility_type, facility_info, isReport);
                break;
            case 4:
                load_facility5(binding, facility_type, facility_info, isReport);
                break;
        }
    }

    /**
     * @param Binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    private void load_facility1(Object Binding, int facility_type, JSONArray result, boolean isReport) {
        TextView titleTextView_facility1 = null, dateTextView_facility1 = null, contentTextView_facility1 = null, facilityID_textView1_facility1 = null;
        RatingBar ratingBar_facility1 = null;
        ConstraintLayout constraintLayout_facility1 = null;

        if(isReport){
//            titleTextView_facility1 = ((FragmentReportBinding) Binding).titleTextViewFacility1;
//            dateTextView_facility1 = ((FragmentReportBinding) Binding).dateTextViewFacility1;
//            contentTextView_facility1 = ((FragmentReportBinding) Binding).contentTextViewFacility1;
//            ratingBar_facility1 = ((FragmentReportBinding) Binding).ratingBarFacility1;
//            facilityID_textView1_facility1 = ((FragmentReportBinding) Binding).facilityIDTextViewFacility1;
//            constraintLayout_facility1 = ((FragmentReportBinding) Binding).facility1;
        }else {
            titleTextView_facility1 = ((FragmentHomeBinding) Binding).titleTextViewFacility1;
            dateTextView_facility1 = ((FragmentHomeBinding) Binding).dateTextViewFacility1;
            contentTextView_facility1 = ((FragmentHomeBinding) Binding).contentTextViewFacility1;
            ratingBar_facility1 = ((FragmentHomeBinding) Binding).ratingBarFacility1;
            facilityID_textView1_facility1 = ((FragmentHomeBinding) Binding).facilityIDTextViewFacility1;
            constraintLayout_facility1 = ((FragmentHomeBinding) Binding).facility1;
        }


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
     * @param facility_type : int representing the type of facility calling this function
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    private void load_facility2(Object Binding, int facility_type, JSONArray result, boolean isReport) {
        TextView titleTextView_facility2 = null, dateTextView_facility2 = null, contentTextView_facility2 = null, facilityID_textView1_facility2 = null;
        RatingBar ratingBar_facility2 = null;
        ConstraintLayout constraintLayout_facility2 = null;

        if(isReport){
//            titleTextView_facility2 = ((FragmentReportBinding) Binding).titleTextViewFacility2;
//            dateTextView_facility2 = ((FragmentReportBinding) Binding).dateTextViewFacility2;
//            contentTextView_facility2 = ((FragmentReportBinding) Binding).contentTextViewFacility2;
//            ratingBar_facility2 = ((FragmentReportBinding) Binding).ratingBarFacility2;
//            facilityID_textView1_facility2 = ((FragmentReportBinding) Binding).facilityIDTextViewFacility2;
//            constraintLayout_facility2 = ((FragmentReportBinding) Binding).facility2;
        }else {
            titleTextView_facility2 = ((FragmentHomeBinding) Binding).titleTextViewFacility2;
            dateTextView_facility2 = ((FragmentHomeBinding) Binding).dateTextViewFacility2;
            contentTextView_facility2 = ((FragmentHomeBinding) Binding).contentTextViewFacility2;
            ratingBar_facility2 = ((FragmentHomeBinding) Binding).ratingBarFacility2;
            facilityID_textView1_facility2 = ((FragmentHomeBinding) Binding).facilityIDTextViewFacility2;
            constraintLayout_facility2 = ((FragmentHomeBinding) Binding).facility2;
        }
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
     * @param facility_type : int representing the type of facility calling this function
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    private void load_facility3(Object Binding, int facility_type, JSONArray result, boolean isReport) {
        TextView titleTextView_facility3 = null, dateTextView_facility3 = null, contentTextView_facility3 = null, facilityID_textView1_facility3 = null;
        RatingBar ratingBar_facility3 = null;
        ConstraintLayout constraintLayout_facility3 = null;

        if(isReport){
//            titleTextView_facility3 = ((FragmentReportBinding) Binding).titleTextViewFacility3;
//            dateTextView_facility3 = ((FragmentReportBinding) Binding).dateTextViewFacility3;
//            contentTextView_facility3 = ((FragmentReportBinding) Binding).contentTextViewFacility3;
//            ratingBar_facility3 = ((FragmentReportBinding) Binding).ratingBarFacility3;
//            facilityID_textView1_facility3 = ((FragmentReportBinding) Binding).facilityIDTextViewFacility3;
//            constraintLayout_facility3 = ((FragmentReportBinding) Binding).facility3;
        }else {
            titleTextView_facility3 = ((FragmentHomeBinding) Binding).titleTextViewFacility3;
            dateTextView_facility3 = ((FragmentHomeBinding) Binding).dateTextViewFacility3;
            contentTextView_facility3 = ((FragmentHomeBinding) Binding).contentTextViewFacility3;
            ratingBar_facility3 = ((FragmentHomeBinding) Binding).ratingBarFacility3;
            facilityID_textView1_facility3 = ((FragmentHomeBinding) Binding).facilityIDTextViewFacility3;
            constraintLayout_facility3 = ((FragmentHomeBinding) Binding).facility3;
        }

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
     * @param facility_type : int representing the type of facility calling this function
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    private void load_facility4(Object Binding, int facility_type, JSONArray result, boolean isReport) {
        TextView titleTextView_facility4 = null, dateTextView_facility4 = null, contentTextView_facility4 = null, facilityID_textView1_facility4 = null;
        RatingBar ratingBar_facility4 = null;
        ConstraintLayout constraintLayout_facility4 = null;

        if(isReport){
//            titleTextView_facility4 = ((FragmentReportBinding) Binding).titleTextViewFacility4;
//            dateTextView_facility4 = ((FragmentReportBinding) Binding).dateTextViewFacility4;
//            contentTextView_facility4 = ((FragmentReportBinding) Binding).contentTextViewFacility4;
//            ratingBar_facility4 = ((FragmentReportBinding) Binding).ratingBarFacility4;
//            facilityID_textView1_facility4 = ((FragmentReportBinding) Binding).facilityIDTextViewFacility4;
//            constraintLayout_facility4 = ((FragmentReportBinding) Binding).facility4;
        }else {
            titleTextView_facility4 = ((FragmentHomeBinding) Binding).titleTextViewFacility4;
            dateTextView_facility4 = ((FragmentHomeBinding) Binding).dateTextViewFacility4;
            contentTextView_facility4 = ((FragmentHomeBinding) Binding).contentTextViewFacility4;
            ratingBar_facility4 = ((FragmentHomeBinding) Binding).ratingBarFacility4;
            facilityID_textView1_facility4 = ((FragmentHomeBinding) Binding).facilityIDTextViewFacility4;
            constraintLayout_facility4 = ((FragmentHomeBinding) Binding).facility4;
        }

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
     * @param facility_type : int representing the type of facility calling this function
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    private void load_facility5(Object Binding, int facility_type, JSONArray result, boolean isReport) {
        TextView titleTextView_facility5 = null, dateTextView_facility5 = null, contentTextView_facility5 = null, facilityID_textView1_facility5 = null;
        RatingBar ratingBar_facility5 = null;
        ConstraintLayout constraintLayout_facility5 = null;

        if(isReport){
//            titleTextView_facility5 = ((FragmentReportBinding) Binding).titleTextViewFacility5;
//            dateTextView_facility5 = ((FragmentReportBinding) Binding).dateTextViewFacility5;
//            contentTextView_facility5 = ((FragmentReportBinding) Binding).contentTextViewFacility5;
//            ratingBar_facility5 = ((FragmentReportBinding) Binding).ratingBarFacility5;
//            facilityID_textView1_facility5 = ((FragmentReportBinding) Binding).facilityIDTextViewFacility5;
//            constraintLayout_facility5 = ((FragmentReportBinding) Binding).facility5;
        }else {
            titleTextView_facility5 = ((FragmentHomeBinding) Binding).titleTextViewFacility5;
            dateTextView_facility5 = ((FragmentHomeBinding) Binding).dateTextViewFacility5;
            contentTextView_facility5 = ((FragmentHomeBinding) Binding).contentTextViewFacility5;
            ratingBar_facility5 = ((FragmentHomeBinding) Binding).ratingBarFacility5;
            facilityID_textView1_facility5 = ((FragmentHomeBinding) Binding).facilityIDTextViewFacility5;
            constraintLayout_facility5 = ((FragmentHomeBinding) Binding).facility5;
        }

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