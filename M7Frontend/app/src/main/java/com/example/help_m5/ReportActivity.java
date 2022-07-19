package com.example.help_m5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private Button submitButton;
    private String userEmail;
    private String reportedUserEmail;
    private String report_type;
    private String title;
    private boolean reportUser;
    private int type;
    private int facilityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        vm_ip = getString(R.string.azure_ip);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Bundle bundle = getIntent().getExtras();

        reportedUserEmail = bundle.getString("reportedUserId");
        type = bundle.getInt("facility_type");
        facilityId = bundle.getInt("facility_id");
        report_type = bundle.getString("report_type");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        userEmail = account.getEmail();
        title = bundle.getString("title");

        EditText editText = findViewById(R.id.editTextReport);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submitButton.setEnabled(true);
                submitButton.setTextColor(Color.parseColor("#dbba00"));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        submitButton = findViewById(R.id.submit_button_report);
//        submitButton.setEnabled(false);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                params.put("reportReason", editText.getText().toString());
                params.put("title", title);

                Log.d(TAG, "aaa" + params.toString());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                                Log.d(TAG,"response is: "+response.toString());
                                Toast.makeText(ReportActivity.this, "Report successfully sent!", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
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
            }
        });
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        if (view.getId() == R.id.checkbox_user) {
            reportUser = checked;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}