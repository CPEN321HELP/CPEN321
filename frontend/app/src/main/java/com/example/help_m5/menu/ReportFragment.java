package com.example.help_m5.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.help_m5.database.DatabaseConnection;
import com.example.help_m5.R;
import com.example.help_m5.databinding.FragmentReportBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class ReportFragment extends Fragment {

    private String vm_ip;

    private static final int posts = 0;
    private static final int study = 1;
    private static final int entertainments = 2;
    private static final int restaurants = 3;
    private static final int report_comment = 5;
    private static final int report_facility = 6;
    private static final String TAG = "ReportFragment";

    private DatabaseConnection DBconnection;
    private FragmentReportBinding binding;

    private int facility_type = -1;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm_ip = getResources().getString(R.string.azure_ip);
        Log.d(TAG, vm_ip);
        DBconnection = new DatabaseConnection();
        DBconnection.cleanAllCaches(getContext());
        binding.c1.setVisibility(View.INVISIBLE);
        binding.c2.setVisibility(View.INVISIBLE);
        binding.reportedReasonContY1.setMovementMethod(new ScrollingMovementMethod());
        binding.reportedReasonContY2.setMovementMethod(new ScrollingMovementMethod());
        initFavMenu();
        setConsOnCl();
        initButtons();
        return root;
    }

    private void setConsOnCl(){
        binding.l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventOnClickListener(1);
            }
        });

        binding.l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventOnClickListener(2);
            }
        });
    }

    private void eventOnClickListener(int which){
        String facility_id = "";
        Log.d(TAG, "before facility_type is "+facility_type);

        if(which == 1){
            facility_type = getTypeInt(binding.facilityTypeContY1.getText().toString());
            facility_id = binding.facilityIdOrgContY1.getText().toString();
        }else {
            facility_type = getTypeInt(binding.facilityTypeContY2.getText().toString());
            facility_id = binding.facilityIdOrgContY2.getText().toString();
        }
        Log.d(TAG," facility_type is: "+facility_type +" facility_id is: "+ facility_id );
        DBconnection.getSpecificFacility(facility_type, facility_id, getContext(), getActivity());
    }

    private void initFavMenu(){
        FloatingActionButton refresh = binding.fabRefresh;
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

        if(DBconnection.isCached(context, "testReportFragment.json")){
            Log.d("TESTINGG", "here");
            try {
                JSONObject response = new JSONObject(DBconnection.readFromJson(context,"testReportFragment.json"));
                JSONArray a1 = response.getJSONArray("report_content");
                update1(a1.getJSONObject(0));
                update2(a1.getJSONObject(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        Log.d("TESTINGG", "here2");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                DBconnection.writeToJson(context, response, "reports.json");

                Log.d(TAG, "ccc" + response.toString());
                try {
                    int length = response.getInt("length");
                    binding.c1.setVisibility(View.INVISIBLE);
                    binding.c2.setVisibility(View.INVISIBLE);

                    if(length == 0){
                        Toast.makeText(getContext(),"There is not report need to be process",Toast.LENGTH_SHORT).show();
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
                Log.d(TAG, "onErrorResponse in getReports Error: " + error.getMessage());
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

    private void approve(boolean isApprove, int which, Context context){
        Toast.makeText(context, "Sending result to server!" , Toast.LENGTH_SHORT).show();
        final RequestQueue queue = Volley.newRequestQueue(context);
        HashMap<String, String> params = new HashMap<String, String>();
        if(isApprove){
            params.put("approve", "1");
        }else {
            params.put("approve", "0");
        }
        GoogleSignInAccount userAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if( userAccount == null){
            params.put("adminEmail","test@gmail.com");
        }else {
            String userEmail = userAccount.getEmail();
            params.put("adminEmail",userEmail);
        }

        if(which == 1){
            boolean deduct = !binding.deductedContY1.getText().toString().equals("False");
            params.put("report_type", get(binding.reportTypeContY1.getText().toString()));
            params.put("report_id",  binding.reportIdY1.getText().toString());
            params.put("facility_type",  binding.facilityTypeContY1.getText().toString());
            params.put("facility_id",  binding.facilityIdOrgContY1.getText().toString());
            params.put("reported_user",binding.reportedIdContY1.getText().toString());
            //for adding credit
            params.put("AdditionType", "report");
            params.put("upUserId", binding.reporterIdContY1.getText().toString());
            params.put("downUserId", deduct? binding.reportedIdContY1.getText().toString() : "none@gmail.com");
            String upMessage = "Your report of "+ binding.facilityTypeContY1.getText().toString() +", with facility id: "+ binding.facilityIdOrgContY1.getText().toString() +", admin ";
            String downMessage = "Your are being report in "+ binding.reportTypeContY1.getText().toString() +", with facility id: "+ binding.reportIdY1.getText().toString() +", admin ";
            if(isApprove){
                upMessage += "approves your report, you gain one credit";
                downMessage +="approves this report, you lost one credit";
            }else {
                upMessage += "rejects your report.";
            }

            params.put("upMessage", upMessage);
            params.put("downMessage", downMessage);
        }else {
            boolean deduct = !binding.deductedContY2.getText().toString().equals("False");

            params.put("report_type", get((binding.reportTypeContY2.getText().toString())));
            params.put("report_id",  binding.reportIdY2.getText().toString());
            params.put("facility_type",  binding.facilityTypeContY2.getText().toString());
            params.put("facility_id",  binding.facilityIdOrgContY2.getText().toString());
            params.put("reported_user",binding.reportedIdContY2.getText().toString());
            //for adding credit
            params.put("AdditionType", "report");
            params.put("upUserId", binding.reporterIdContY2.getText().toString());
            params.put("downUserId", deduct? binding.reportedIdContY2.getText().toString() : "none@gmail.com");
            String upMessage = "Your report of "+ binding.facilityTypeContY2.getText().toString() +", with facility id: "+ binding.facilityIdOrgContY2.getText().toString() +", admin ";
            String downMessage = "Your are being report in "+ binding.reportTypeContY2.getText().toString() +", with facility id: "+ binding.reportIdY2.getText().toString() +", admin ";
            if(isApprove){
                upMessage += "approves your report, you gain one credit";
                downMessage +="approves this report, you lost one credit";
            }else {
                upMessage += "rejects your report.";
            }
            params.put("upMessage", upMessage);
            params.put("downMessage", downMessage);
        }

        //can not used this format otherwise codacy will say this method is too complex
//        params.put("report_type", which == 1? binding.reportTypeContY1.getText().toString() : binding.reportTypeContY2.getText().toString());
//        params.put("report_id", which == 1? binding.reportIdY1.getText().toString(): binding.reportIdY2.getText().toString());
//        params.put("facility_type", which == 1? binding.facilityTypeContY1.getText().toString():binding.facilityTypeContY2.getText().toString());
//        params.put("facility_id", which == 1? binding.facilityIdOrgContY1.getText().toString():binding.facilityIdOrgContY2.getText().toString());
//        params.put("reported_user", which == 1? binding.reportedIdContY1.getText().toString():binding.reportedIdContY2.getText().toString());
//        //for adding credit
//        params.put("AdditionType", "report");
//        params.put("upUserId", which == 1? binding.reporterIdContY1.getText().toString():binding.reporterIdContY2.getText().toString());
//        params.put("downUserId",  which == 1? binding.reportedIdContY1.getText().toString():binding.reportedIdContY2.getText().toString());
//        String upMessage = "Your report of "+ ( which == 1? binding.facilityTypeContY1.getText().toString():binding.facilityTypeContY2.getText().toString()) +", with facility id: "+ (which == 1? binding.facilityIdOrgContY1.getText().toString():binding.facilityIdOrgContY2.getText().toString()) +", admin ";
//        String downMessage = "Your are being report in "+ (which == 1? binding.reportTypeContY1.getText().toString() : binding.reportTypeContY2.getText().toString()) +", with facility id: "+ (which == 1? binding.reportIdY1.getText().toString(): binding.reportIdY2.getText().toString()) +", admin ";
//        if(isApprove){
//            upMessage += "approves your report, you gain one credit";
//            downMessage +="approves this report, you lost one credit";
//        }else {
//            upMessage += "rejects your report.";
//        }
//        params.put("upMessage", upMessage);
//        params.put("downMessage", downMessage);
        Log.d(TAG, "aass " +params.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, vm_ip + "admin/reportApproval", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "sss "+response);
                Toast.makeText(context, "Server has received your decision!" , Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "approve onErrorResponse" + "Error: " + error.getMessage());
                Toast.makeText(context, "approve Error sending report: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
        binding.fabRefresh.performClick();
    }

    @SuppressLint("SetTextI18n")
    private void update1(JSONObject data){
        //1
        TextView report_title_cont_y1 = binding.reportTitleContY1;
        TextView facility_type_cont_y1 = binding.facilityTypeContY1;
        TextView facility_id_org_cont_y1 = binding.facilityIdOrgContY1;
        TextView reporter_id_cont_y1 = binding.reporterIdContY1;
        TextView report_type_cont_y1 = binding.reportTypeContY1;
        TextView reported_id_cont_y1 = binding.reportedIdContY1;
        TextView reported_reason_cont_y1 = binding.reportedReasonContY1;
        TextView report_id_y1 = binding.reportIdY1;
        TextView deducted_y1 = binding.deductedContY1;

        try {
            String reportType = data.getString("title");
            report_title_cont_y1.setText((reportType));

            String reportedFacilityType = data.getString("facility_type");
            facility_type_cont_y1.setText(getTypeInString(reportedFacilityType));

            String reportedFacilityID = data.getString("facility_id");
            facility_id_org_cont_y1.setText((reportedFacilityID));

            String reporterID = data.getString("reporter");
            reporter_id_cont_y1.setText((reporterID));

            String report_type = data.getString("report_type");
            report_type_cont_y1.setText(getTypeInString(report_type));

            String reported_id = data.getString("reported_user");
            reported_id_cont_y1.setText((reported_id));

            String reportReason = data.getString("reason");
            reported_reason_cont_y1.setText((reportReason));

            String report_id = data.getString("_id");
            report_id_y1.setText((report_id));

            String deducted = data.getString("reportUserStatus");
            deducted_y1.setText(deducted.equals("0") ? "False":"True");

            binding.c1.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"Error! can not load report 1. Missing info",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void update2(JSONObject data){
        //1
        TextView report_title_cont_y2 = binding.reportTitleContY2;
        TextView facility_type_cont_y2 = binding.facilityTypeContY2;
        TextView facility_id_org_cont_y2 = binding.facilityIdOrgContY2;
        TextView reporter_id_cont_y2 = binding.reporterIdContY2;
        TextView report_type_cont_y2 = binding.reportTypeContY2;
        TextView reported_id_cont_y2 = binding.reportedIdContY2;
        TextView reported_reason_cont_y2 = binding.reportedReasonContY2;
        TextView report_id_y2 = binding.reportIdY2;
        TextView deducted_y2 = binding.deductedContY2;

        try {
            String reportType = data.getString("title");
            report_title_cont_y2.setText((reportType));

            String reportedFacilityType = data.getString("facility_type");
            facility_type_cont_y2.setText(getTypeInString(reportedFacilityType));

            String reportedFacilityID = data.getString("facility_id");
            facility_id_org_cont_y2.setText((reportedFacilityID));

            String reporterID = data.getString("reporter");
            reporter_id_cont_y2.setText((reporterID));

            String report_type = data.getString("report_type");
            report_type_cont_y2.setText(getTypeInString(report_type));

            String reported_id = data.getString("reported_user");
            reported_id_cont_y2.setText((reported_id));

            String reportReason = data.getString("reason");
            reported_reason_cont_y2.setText((reportReason));

            String report_id = data.getString("_id");
            report_id_y2.setText((report_id));

            String deducted = data.getString("reportUserStatus");
            deducted_y2.setText(deducted.equals("0") ? "False":"True");
            binding.c2.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"Error! can not load report 2. Missing info",Toast.LENGTH_SHORT).show();
        }
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
                return "reported comment";
            case "6":
                return "reported facility";
            default:
                return "none";
        }
    }

    private String get(String a){
        if(a.equals("reported comment")){
            return "5";
        }else{
            return "6";
        }
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
            default:
                return -1;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}