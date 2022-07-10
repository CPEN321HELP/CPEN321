package com.example.help_m5.ui.report;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.help_m5.CustomAdapter;
import com.example.help_m5.FacilityActivity;
import com.example.help_m5.ReportActivity;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.example.help_m5.R;
import com.example.help_m5.databinding.FragmentReportBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class ReportFragment extends Fragment {

    private static final String vm_ip = "http://20.213.243.141:8000/";

    private static final int posts = 0;
    private static final int study = 1;
    private static final int entertainments = 2;
    private static final int restaurants = 3;
    private static final int report_user = 4;
    private static final int report_comment = 5;
    private static final int report_facility = 6;

    private static final int normal_local_load = 0;
    private  static final int normal_server_load = 1;
    private static final int reached_end = 2;
    private static final int server_error = 3;
    private static final int local_error = 4;
    private  static final int only_one_page = 5;

    private static final String TAG = "ReportFragment";

    private float transY = 100f;
    private OvershootInterpolator interpolator = new OvershootInterpolator();

    private boolean isMenuOpen = false;

    private DatabaseConnection DBconnection;
    private FragmentReportBinding binding;
    private FloatingActionButton refresh;
    private Spinner spin;



    private static String[] countryNames={"Comment","Facility"};
    private static int flags[] = {R.drawable.ic_baseline_comment_24, R.drawable.ic_baseline_all_inclusive_24};
    private int facility_type = -1;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DBconnection = new DatabaseConnection();
        DBconnection.cleanCaches(getContext());

        initFavMenu();
        setConsOnCl();
        initButtons();
        return root;
    }

    private void setConsOnCl(){
        binding.reportY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayoutOnClickListener(1);
            }
        });

        binding.reportY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayoutOnClickListener(2);
            }
        });
    }

    private void ConstraintLayoutOnClickListener(int which){
        String facility_id = "";
        Log.d(TAG, "before facility_type is "+facility_type);

        switch (which){
            case 1:
                facility_type = getTypeInt(binding.facilityTypeContY1.getText().toString());
                facility_id = binding.reportedIdY1.getText().toString();
                break;
            case 2:
                facility_type = getTypeInt(binding.facilityTypeContY2.getText().toString());
                facility_id = binding.reportedIdY2.getText().toString();
                break;
        }

        Log.d(TAG, "after facility_type is "+facility_type);
        Log.d(TAG, "after facility_id is "+facility_id);

        int result = DBconnection.getSpecificFacility(facility_type, facility_id, getContext());
        Handler handler = new Handler();

        String finalFacility_id = facility_id;
        handler.postDelayed(new Runnable() {
            public void run() {
                if(result == server_error){
                    Toast.makeText(getContext(), "Error happened when connecting to server, please try again later", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "opening " + which, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), FacilityActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("facility_type", facility_type);
                    bundle.putString("facility_id", finalFacility_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }, 300);
    }

    private void initFavMenu(){
        refresh = binding.fabCloseOrRefresh;
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReports(getContext());
            }
        });
    }

    private void getReports(Context context){
        String url = vm_ip+"report/admin";

        final RequestQueue queue = Volley.newRequestQueue(context);
        HashMap<String, String> params = new HashMap<String, String>();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "ccc" + response.toString());
                try {
                    int length = response.getInt("length");
                    if(length == 0){

                    } else if(length == 1){
                        JSONArray a1 = response.getJSONArray("report_content");
                        update1(a1.getJSONObject(0));
                    } else {
                        JSONArray a1 = response.getJSONArray("report_content");
                        update1(a1.getJSONObject(0));
                        update2(a1.getJSONObject(1));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse" + "Error: " + error.getMessage());
                Toast.makeText(context, "Error sending report: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsObjRequest);
    }

    private void initButtons(){
        //approve y1
        binding.reportApproveY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve(true,1,getContext());
            }
        });
        //approve y2
        binding.reportApproveY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve(true,2,getContext());
            }
        });
        //not approve y1
        binding.reportNotY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve(false,1,getContext());
            }
        });
        //not approve y2
        binding.reportNotY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve(false,2,getContext());
            }
        });
    }

    private TextView report_title_cont_Y1, facility_type_cont_Y1, facility_id_org_cont_Y1, reporter_id_cont_Y1, report_type_cont_Y1, reported_id_cont_Y1, reported_reason_cont_Y1, report_id_Y1;
    private TextView report_title_cont_y2, facility_type_cont_y2, facility_id_org_cont_y2, reporter_id_cont_y2, report_type_cont_y2, reported_id_cont_y2, reported_reason_cont_y2, report_id_y2;

    private void approve(boolean isApprove, int which, Context context){
        String url = vm_ip + "admin/reportApproval";
        final RequestQueue queue = Volley.newRequestQueue(context);
        HashMap<String, String> params = new HashMap<String, String>();
        if(isApprove){
            params.put("approve", "1");
        }else {
            params.put("approve", "0");
        }
        if(which == 1){
//            params.put("report_type", "5");
//            params.put("report_id", "62cb1c4fea53d3e824fcc10c");
//            params.put("facility_type", "entertainments");
//            params.put("facility_id", "6");
//            params.put("reported_user", "l2542293790@gmail.com");

            params.put("report_type", binding.reportTypeContY1.getText().toString());
            params.put("report_id", binding.reportIdY1.getText().toString());
            params.put("facility_type", binding.facilityTypeContY1.getText().toString());
            params.put("facility_id", binding.facilityIdOrgContY1.getText().toString());
            params.put("reported_user", binding.reportedIdContY1.getText().toString());
        }else {
            params.put("report_type", binding.reportTypeContY2.getText().toString());
            params.put("report_id", binding.reportIdY2.getText().toString());
            params.put("facility_type", binding.facilityTypeContY2.getText().toString());
            params.put("facility_id", binding.facilityIdOrgContY2.getText().toString());
            params.put("reported_user", binding.reportedIdContY2.getText().toString());
        }
        Log.d(TAG, params.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse" + "Error: " + error.getMessage());
                Toast.makeText(context, "Error sending report: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsObjRequest);
    }

    private void update1(JSONObject data){
        //1
        report_title_cont_Y1 = binding.reportTitleContY1;
        try {
            String reportType = data.getString("title");
            report_title_cont_Y1.setText((reportType));
        } catch (JSONException e) {
            e.printStackTrace();
            report_title_cont_Y1.setText("none");
        }
        //2
        facility_type_cont_Y1 = binding.facilityTypeContY1;
        try {
            String reportedFacilityType = data.getString("facility_type");
            facility_type_cont_Y1.setText(getTypeInString(reportedFacilityType));
        } catch (JSONException e) {
            e.printStackTrace();
            facility_type_cont_Y1.setText("none");
        }
        //3
        facility_id_org_cont_Y1 = binding.facilityIdOrgContY1;
        try {
            String reportedFacilityID = data.getString("facility_id");
            facility_id_org_cont_Y1.setText((reportedFacilityID));
        } catch (JSONException e) {
            e.printStackTrace();
            facility_id_org_cont_Y1.setText("none");
        }
        //4
        reporter_id_cont_Y1 = binding.reporterIdContY1;
        try {
            String reporterID = data.getString("reporter");
            reporter_id_cont_Y1.setText((reporterID));
        } catch (JSONException e) {
            e.printStackTrace();
            reporter_id_cont_Y1.setText("none");
        }
        //5
        report_type_cont_Y1 = binding.reportTypeContY1;
        try {
            String report_type = data.getString("report_type");
            report_type_cont_Y1.setText((report_type));
        } catch (JSONException e) {
            e.printStackTrace();
            report_type_cont_Y1.setText("none");
        }
        //6
        reported_id_cont_Y1 = binding.reportedIdContY1;
        try {
            String reported_id = data.getString("reported_user");
            reported_id_cont_Y1.setText((reported_id));
        } catch (JSONException e) {
            e.printStackTrace();
            reported_id_cont_Y1.setText("none");
        }
        //7
        reported_reason_cont_Y1 = binding.reportedReasonContY1;
        try {
            String reportReason = data.getString("reason");
            reported_reason_cont_Y1.setText((reportReason));
        } catch (JSONException e) {
            reported_reason_cont_Y1.setText("none");
            e.printStackTrace();
        }
        //id
        report_id_Y1 = binding.reportIdY1;
        try {
            String report_id = data.getString("_id");
            report_id_Y1.setText((report_id));
        } catch (JSONException e) {
            e.printStackTrace();
            report_id_Y1.setText("none");
        }
        binding.reportY1.setVisibility(View.VISIBLE);
    }

    private void update2(JSONObject data){
        //1
        report_title_cont_y2 = binding.reportTitleContY2;
        try {
            String reportType = data.getString("title");
            report_title_cont_y2.setText((reportType));
        } catch (JSONException e) {
            e.printStackTrace();
            report_title_cont_y2.setText("none");
        }
        //2
        facility_type_cont_y2 = binding.facilityTypeContY2;
        try {
            String reportedFacilityType = data.getString("facility_type");
            facility_type_cont_y2.setText(getTypeInString(reportedFacilityType));
        } catch (JSONException e) {
            e.printStackTrace();
            facility_type_cont_y2.setText("none");
        }
        //3
        facility_id_org_cont_y2 = binding.facilityIdOrgContY2;
        try {
            String reportedFacilityID = data.getString("facility_id");
            facility_id_org_cont_y2.setText((reportedFacilityID));
        } catch (JSONException e) {
            e.printStackTrace();
            facility_id_org_cont_y2.setText("none");
        }
        //4
        reporter_id_cont_y2 = binding.reporterIdContY2;
        try {
            String reporterID = data.getString("reporter");
            reporter_id_cont_y2.setText((reporterID));
        } catch (JSONException e) {
            e.printStackTrace();
            reporter_id_cont_y2.setText("none");
        }
        //5
        report_type_cont_y2 = binding.reportTypeContY2;
        try {
            String report_type = data.getString("report_type");
            report_type_cont_y2.setText((report_type));
        } catch (JSONException e) {
            e.printStackTrace();
            report_type_cont_y2.setText("none");
        }
        //6
        reported_id_cont_y2 = binding.reportedIdContY2;
        try {
            String reported_id = data.getString("reported_user");
            reported_id_cont_y2.setText((reported_id));
        } catch (JSONException e) {
            e.printStackTrace();
            reported_id_cont_y2.setText("none");
        }
        //7
        reported_reason_cont_y2 = binding.reportedReasonContY2;
        try {
            String reportReason = data.getString("reason");
            reported_reason_cont_y2.setText((reportReason));
        } catch (JSONException e) {
            reported_reason_cont_y2.setText("none");
            e.printStackTrace();
        }
        //id
        report_id_y2 = binding.reportIdY2;
        try {
            String report_id = data.getString("_id");
            report_id_y2.setText((report_id));
        } catch (JSONException e) {
            e.printStackTrace();
            report_id_y2.setText("none");
        }
        binding.reportY2.setVisibility(View.VISIBLE);
    }

    private String getTypeInString(String type){
        switch (type){
            case "0":
                return "posts";
            case "1":
                return "studys";
            case "2":
                return "entertainments";
            case "3":
                return "restaurants";
            case "5":
                return "report_comment";
            case "6":
                return "report_facility";
        }
        return "none";
    }

    private int getTypeInt(String type){
        switch (type){
            case "posts":
                return posts;
            case "study":
                return study;
            case "entertainments":
                return entertainments;
            case "restaurants":
                return restaurants;
            case "report_comment":
                return report_comment;
            case "report_facility":
                return report_facility;
        }
        return -1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}