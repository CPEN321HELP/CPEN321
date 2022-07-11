package com.example.help_m5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private int RC_SIGN_IN = 1;
    final static String TAG = "LoginActivity";
    private final String vm_ip = "http://20.213.243.141:8000/";

    private final int posts = 0;
    private final int study = 1;
    private final int entertainments = 2;
    private final int restaurants = 3;
    private final int report_user = 4;
    private final int report_comment = 5;
    private final int report_facility = 6;

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseConnection db;

    private int userType;
    private String userInfo = "userInfo.json";
    private static final String ONESIGNAL_APP_ID = "f38cdc86-9fb7-40a5-8176-68b4115411da";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseConnection();

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.setEmail("none@gmail.com");
        OneSignal.setNotificationOpenedHandler(
                new OneSignal.OSNotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenedResult result) {
                        OSNotificationAction.ActionType type = result.getAction().getType(); // "ActionTaken" | "Opened"
                        String message = result.getNotification().getBody();
                        Log.d(TAG,message);
                        Pattern id = Pattern.compile("\\d+"); //match facility id
                        Pattern fc_type = Pattern.compile("\\bstudys|entertainments|restaurants|posts\\b");//match facility type
                        Matcher id_m = id.matcher(message);
                        Matcher fc_type_m = fc_type.matcher(message);

                        if(id_m.find() && fc_type_m.find()) {
                            String facility_id = id_m.group(0);
                            String facility_type_s = fc_type_m.group(0);
                            int facility_type_int = -1;
                            Log.d(TAG, "In regex facility_id is : " + facility_id+ " facility_type is: " + facility_type_s);
                            switch (facility_type_s){
                                case "posts":
                                    facility_type_int = posts;break;
                                case "studys":
                                    facility_type_int =  study;break;
                                case "entertainments":
                                    facility_type_int =  entertainments;break;
                                case "restaurants":
                                    facility_type_int =  restaurants;break;
                            }
                            Log.d(TAG, "In regex facility_id is : " + facility_id+ " facility_type_int is: " + facility_type_int);
                            if(facility_type_int == -1){
                                Toast.makeText(getApplicationContext(), "Error when opening posts, please report", Toast.LENGTH_LONG).show();
                                return;
                            }
                            String url = "http://20.213.243.141:8000/specific";
                            final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            HashMap<String, String> params = new HashMap<String, String>();
                            queue.start();
                            params.put("facility_id", facility_id);
                            params.put("facility_type", String.valueOf(facility_type_int));
                            int finalFacility_type_int = facility_type_int;
                            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent intent = new Intent(getApplicationContext(), FacilityActivity.class);
                                    Log.d(TAG, "response is: " + response.toString());
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("facility_type", finalFacility_type_int);
                                    bundle.putString("facility_id", facility_id);
                                    bundle.putString("facility_json", response.toString());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG,"ERROR notification: " + error);
                                }
                            });
                            queue.add(jsObjRequest);
                        }else {
                            Toast.makeText(getApplicationContext(), "Error when opening posts, please report", Toast.LENGTH_LONG).show();
                        }

                    }
                });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if(db.isCached(getApplicationContext(), userInfo)){
            //user has already login in
            Log.d(TAG, "cached");

            Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(MainIntent);
        }
        Log.d(TAG, "not cached");

        // Google Sign In Button
        signInButton = findViewById(R.id.sign_in_button);
        setButtonText(signInButton, "Sign in with Google");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); // If the sign in works, return RC_SIGN_IN
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                handleSignInResult(task);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) throws JSONException {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
            Log.d(TAG, "Trying to load device phone model information");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //try {
        //    updateUI(account);
        //} catch (JSONException e) {
        //    e.printStackTrace();
        //}
    }

    private void updateUI(GoogleSignInAccount account) throws JSONException {
        if (account == null) {
            Log.d(TAG, "There is no user signed in");
        } else {
            String email = account.getEmail();
            if(email!= null){
                Toast.makeText(getApplicationContext(), "email is "+email, Toast.LENGTH_SHORT).show();
                OneSignal.setExternalUserId(email);
            }else {
                OneSignal.setExternalUserId("none@gmail.com");
                Toast.makeText(getApplicationContext(), "email is none@gmail.com", Toast.LENGTH_SHORT).show();

            }

            // Send token to back-end
            RequestQueue queue = Volley.newRequestQueue(this);
            HashMap<String, String> params = new HashMap<String, String>();
            queue.start();
            params.put("_id", account.getEmail());
            params.put("username", account.getDisplayName());
            if (account.getPhotoUrl() != null) {
                params.put("user_logo", account.getPhotoUrl().toString());
            } else {
                params.put("user_logo", "none");
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, vm_ip+"google_sign_up", new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            JSONObject info = new JSONObject();
                            try {
                                info.put("user_name", account.getDisplayName());
                                info.put("user_email", account.getEmail());
                                info.put("user_type", 0);
                                info.put("number_of_credit", response.get("number_of_credit"));
                                if (account.getPhotoUrl() != null) {
                                    info.put("user_icon", account.getPhotoUrl().toString());
                                } else {
                                    info.put("user_icon", "none");
                                }
                                db.writeToJson(getApplicationContext(), info, userInfo);
                                Log.d(TAG,"info is: "+info.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse " + "Error: " + error.getMessage());
                        }
                    });
            queue.add(request);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Move to another activity
                    Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user_name", account.getDisplayName());
                    bundle.putString("user_email", account.getEmail());
                    bundle.putInt("user_type", 0);
                    if (account.getPhotoUrl() != null) {
                        bundle.putString("user_icon", account.getPhotoUrl().toString());
                    } else {
                        bundle.putString("user_icon", "none");
                    }
                    MainIntent.putExtras(bundle);
                    startActivity(MainIntent);
                    Log.d(TAG, "start next activity");
                }
            }, 500);
        }
    }

    protected void setButtonText(SignInButton signInButton, String text) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View view = signInButton.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setText(text);
                return;
            }
        }
    }

    protected void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "You have Sign out.", Toast.LENGTH_LONG).show();
                    }
                });
        mGoogleSignInClient.revokeAccess();
        finishAndRemoveTask();
        System.exit(0);
    }

}