package com.example.help_m5;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import org.json.JSONObject;
import java.util.HashMap;

public class ReportActivity extends AppCompatActivity {

    private static final String TAG = "ReportActivity";
    private String vm_ip ;
    private String userEmail;
    private String reportedUserEmail;
    private String report_type;
    private String title;
    private int type;
    private int facilityId;
    private boolean reportUser = false;
    private boolean isReporting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isReporting = false;
        vm_ip = getString(R.string.azure_ip);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Bundle bundle = getIntent().getExtras();

        reportedUserEmail = bundle.getString("reportedUserId");
        type = bundle.getInt("facility_type");
        facilityId = bundle.getInt("facility_id");
        report_type = bundle.getString("report_type");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account == null){
            userEmail = "test@gmail.com";
        }else {
            userEmail = account.getEmail();
        }
        title = bundle.getString("title");

        EditText editText = findViewById(R.id.editTextReport);

        Button submitButton = findViewById(R.id.submit_button_report);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(ReportActivity.this, "Please state your reason of report", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isReporting){
                    Toast.makeText(getApplicationContext(), "Please do not click again", Toast.LENGTH_SHORT).show();
                    return;
                }
                isReporting = true;
                String url = vm_ip + "user/Report/commentAndfacility";
                Log.d(TAG, "bbb" + url);
                RequestQueue queue = Volley.newRequestQueue(ReportActivity.this);
                HashMap<String, String> params = new HashMap<String, String>();
                queue.start();
                params.put("reportedFacilityID", String.valueOf(facilityId));
                params.put("reportedFacilityType", String.valueOf(type));
                params.put("report_type", report_type);
                params.put("reporterID", userEmail);
                params.put("reported_id", reportedUserEmail);
                Log.d(TAG,"reportedUserEmail: "+reportedUserEmail);
                params.put("reportReason", editText.getText().toString());
                params.put("title", title);
                params.put("reportUser", reportUser ? "1" : "0");
                Log.d(TAG, "aaa" + params.toString());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                isReporting = false;
                                Log.d(TAG,"response is: "+response.toString());
                                if (reportUser) {
                                    Toast.makeText(ReportActivity.this, "Report successfully sent with associated user!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ReportActivity.this, "Report successfully sent!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                isReporting = false;
                                Log.d(TAG, "onErrorResponse" + "Error: " + error.getMessage());
                                Toast.makeText(ReportActivity.this, "Error sending report" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                queue.add(request);
                finish();
            }
        });

        Button cancelButton = findViewById(R.id.cancel_button_report);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                isReporting = false;
            }
        });

        CheckBox cb = findViewById(R.id.checkbox_user);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CompoundButton) v).isChecked()){
                    reportUser = true;
                    Log.d(TAG,"reporting user");
                } else {
                    reportUser = false;
                    Log.d(TAG,"not reporting user");
                }
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        isReporting = false;
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}