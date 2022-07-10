package com.example.help_m5.ui.database;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.help_m5.databinding.FragmentReportBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;

//DatabaseConnection
public class DatabaseConnection {

    private static final String vm_ip = "http://20.213.243.141:8000/";
    final String TAG = "databaseConnection";

    //following are types of facility
    static final int posts = 0;
    static final int study = 1;
    static final int entertainments = 2;
    static final int restaurants = 3;
    static final int report_user = 4;
    static final int report_comment = 5;
    static final int report_facility = 6;
    //above are types of facility

    //below are types of error that could happen
    static final int normal_local_load = 0;
    static final int normal_server_load = 1;
    static final int reached_end = 2;
    static final int server_error = 3;
    static final int local_error = 4;
    static final int only_one_page = 5;
    //above are types of error that could happen

    //remove?
    public void sendToken(Context applicationContext, String token){
        String url = vm_ip + "sendToDevice";
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);
        HashMap<String, String> params = new HashMap<String, String>();
        queue.start();
        params.put("token", token);

        Log.d(TAG, params.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "response is: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERROR when connecting to database getSpecificFacility");
            }
        });
        queue.add(jsObjRequest);
    }

    private int status_add_facility = normal_server_load;

    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param title, description, type, imageLink, longitude, latitude : information need to be stored on database
     * @param user_id : the user who added this facility
     * @Pupose : to add a new facility in to database
     */
    public int addFacility(Context applicationContext,String title, String description, String type, String imageLink, String longitude, String latitude, String user_id){
        String url = vm_ip + "addFacility";
        Log.d(TAG, url);
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);
        HashMap<String, String> params = new HashMap<String, String>();
        queue.start();

        params.put("title", title);
        params.put("description", description);
        params.put("long", longitude);
        params.put("lat", latitude);
        params.put("type", type);
        params.put("facilityImageLink", imageLink);

        Log.d(TAG, params.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "response is: " + response.toString());
                status_add_facility = normal_server_load;
                addCredit(applicationContext, user_id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                status_add_facility = server_error;
                Log.d(TAG, "ERROR when connecting to database getSpecificFacility");
            }
        });
        queue.add(jsObjRequest);
        Log.d(TAG, "status_add_facility is " + status_add_facility);
        return status_add_facility;
    }

    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param user_id  : string of user id
     * @Pupose : to notify server which user to add credit
     */
    private void addCredit(Context applicationContext, String user_id){
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);
        HashMap<String, String> params = new HashMap<String, String>();
        queue.start();
        String url = vm_ip + "creditHandling/normal";
        params.put("upUserId", user_id);
        Log.d(TAG, params.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "response is: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERROR when connecting to database getSpecificFacility");
            }
        });
        queue.add(jsObjRequest);
        Log.d(TAG, "status_add_facility is " + status_add_facility);
    }

    int status_getSpecificFacility = normal_server_load;
    /**
     * @param facility_type      : int representing the type of facility calling this function
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param facility_id  : string of facility_id
     * @return :
     * normal_server_load, indicate successfully send the data to server
     * server_error, indicate unsuccessfully send the data to server
     * @Pupose : to get a Specific facility by its facility id and type
     */
    /*
    public int getSpecificFacility(int facility_type, String facility_id, Context applicationContext){
        String fileName = "specific_facility.json";
        if(isCached(applicationContext, fileName)){
            removeFile(applicationContext, fileName);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                String url = "http://20.213.243.141:8000/specific";
                final RequestQueue queue = Volley.newRequestQueue(applicationContext);
                HashMap<String, String> params = new HashMap<String, String>();
                queue.start();
                params.put("facility_id", facility_id);
                params.put("facility_type", ""+facility_type);
//        Log.d(TAG, params.toString());
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "response is: " + response.toString());
                        writeToJson(applicationContext, response, fileName);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        status_getSpecificFacility = server_error;
                        Log.d(TAG, "ERROR when connecting to database getSpecificFacility");
                    }
                });
                queue.add(jsObjRequest);
//        Log.d(TAG, "status_getSpecificFacility is " + status_getSpecificFacility);
            }
        }, 500);
        return status_getSpecificFacility;
    }
    */
    public int getSpecificFacility(int facility_type, String facility_id, Context applicationContext){
        String fileName = "specific_facility.json";
        String url = "http://20.213.243.141:8000/specific";
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);
        HashMap<String, String> params = new HashMap<String, String>();
        queue.start();
        params.put("facility_id", facility_id);
        params.put("facility_type", ""+facility_type);
//        Log.d(TAG, params.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "response is: " + response.toString());
                writeToJson(applicationContext, response, fileName);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                status_getSpecificFacility = server_error;
                Log.d(TAG, "ERROR when connecting to database getSpecificFacility");
            }
        });
        queue.add(jsObjRequest);
//        Log.d(TAG, "status_getSpecificFacility is " + status_getSpecificFacility);
        return status_getSpecificFacility;
    }

    int status_getFacilities = normal_server_load;

    /**
     * @param binding            : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type      : int representing the type of facility calling this function
     * @param page_number        : what range to load
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param content_to_search  : string user typed in search box
     * @return : 0, indicate successfully load the data from cached file to screen
     * 4, indicate unsuccessfully load the data from cached file to screen
     * 1, indicate successfully load the data from server to screen
     * 3, indicate unsuccessfully load the data from server to screen
     * 2, reached end of show
     * @Pupose : to load the content from server our cached file to screen for user to view
     */
    public int getFacilities(Object binding, int facility_type, int page_number, Context applicationContext, boolean is_search, boolean is_report, String content_to_search) {
        String fileName = "";
        if (is_search) {
            fileName = "search_" + getStringType(facility_type) + ".json";
        } else if (is_report){
            fileName = "report" + getStringType(facility_type) + ".json";
        } else {
            fileName = getStringType(facility_type) + ".json";
        }
        return searchFacilities(binding, facility_type, page_number, applicationContext, is_search, is_report, content_to_search, fileName);
    }

    /**
     * @param binding            : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type      : int representing the type of facility calling this function
     * @param page_number        : what range to load
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param content_to_search  : string user typed in search box
     * @return : 0, indicate successfully load the data from cached file to screen
     * 4, indicate unsuccessfully load the data from cached file to screen
     * 1, indicate successfully load the data from server to screen
     * 3, indicate unsuccessfully load the data from server to screen
     * 2, reached end of show
     * @Pupose : to load the content from server our cached file to screen for user to view
     */
    private int searchFacilities(Object binding, int facility_type, int page_number, Context applicationContext, boolean is_search, boolean is_report, String content_to_search, String fileName) {
//        Log.d(TAG, "facility_type :"+facility_type);
        LoadToScreen loader = new LoadToScreen();
        if (isCached(applicationContext, fileName)) {//page up and page down should go here
            try {
                JSONObject data = new JSONObject(readFromJson(applicationContext, fileName));
                int result = loader.loadToScreen(binding, facility_type, page_number, data, is_report);
                Log.d(TAG, "result in cached is " + result);

                if (result == 1) {
                    return reached_end;
                } else if (result == 2){
                    return only_one_page;
                }
                return normal_local_load;
            } catch (JSONException e) {
                e.printStackTrace();
                return local_error;
            }
        } else { //search should go here
            final RequestQueue queue = Volley.newRequestQueue(applicationContext);
            HashMap<String, String> params = new HashMap<String, String>();
            queue.start();
            params.put("page_number", "" + page_number);
            params.put("type", ""+ facility_type);
            String url = vm_ip;

            if(is_report){
                url += "user/Report/" + getStringType(facility_type);
            }else {
                url += "facility";
                //url += getStringType(facility_type);
                if (is_search) {
                    url += "/search";
                    params.put("search", "" + content_to_search);
                }else{
                    url += "/newest";
                }
            }
            Log.d(TAG,url);
            Log.d(TAG, params.toString());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "response is: " + response.toString());
                    int result = writeToJson(applicationContext, response, fileName);
                    if (result == 2) {
                        status_getFacilities = local_error;// IOException
                        return;
                    }
                    try {
                        JSONObject data = new JSONObject(readFromJson(applicationContext, fileName));
                        int result2 = loader.loadToScreen(binding, facility_type, page_number, data, is_report);
                        if (result2 == 1) {
                            status_getFacilities = server_error; //reached end of facility json array
                        }
                    } catch (JSONException e) {
                        status_getFacilities = local_error; // error reading json file
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    status_getFacilities = server_error;
                    Log.d(TAG, "ERROR when connecting to database searchFacilities");
                    Log.d(TAG, "onErrorResponse" + "Error: " + error.getMessage());
                    Toast.makeText(applicationContext, "Error sending report: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsObjRequest);
            Log.d(TAG, "status_getFacilities is " + status_getFacilities);
            return status_getFacilities;
        }
    }

    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param fileName           :
     * @return true, if file is cached; false otherwise
     * @Pupose : to check if file is cached in internal storage, this method is used only by searchFacilities()
     */
    public boolean isCached(Context applicationContext, String fileName) {
        File f = new File(applicationContext.getFilesDir().toString()+"/"+fileName);
        return f.exists() && !f.isDirectory();
    }


    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param response           : response from server
     * @return 0 if cached successfully; 1, if File Already Exists; 2 if IOException.
     * @Pupose write json response from server to a file
     */
    public int writeToJson(Context applicationContext, JSONObject response, String fileName) {
        try {
            File file = new File(applicationContext.getFilesDir(), fileName);
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(response.toString().getBytes());
            writer.close();
            Log.d(TAG, "write to file" + fileName + " path is: " + file.getCanonicalPath());
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        }
        return 0;
    }

    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param fileName           : file name to be read
     * @return String of corrsponding file; "1" if FileNotFoundException; "2" if IOException
     * @Pupose read json response from file
     */
    public String readFromJson(Context applicationContext, String fileName) {
        try {
            File file = new File(applicationContext.getFilesDir(), fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            // This responce will have Json Format String
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void removeFile(Context applicationContext, String fileName){
        if(!isCached(applicationContext, fileName)){
            File f = new File(applicationContext.getFilesDir().toString()+"/"+fileName);
            f.delete();
        }
    }
    public void removeFile(String filePath){
        File f = new File(filePath);
        f.delete();

    }
    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @Pupose clean all files stored in /data/data/com.example.help_m5/files/
     */
    public void cleanCaches(Context applicationContext){
        if( applicationContext == null){
//            Log.d(TAG, "applicationContext Null");
            return;
        }
        File targetDir = applicationContext.getFilesDir();
        File[] files = targetDir.listFiles();
        if( files == null){
//            Log.d(TAG, "dir empty, nothing to clean");
            return;
        }
        for(File f : files){
            String filename = f.getName();
            Log.d(TAG, "filename is: "+filename);
            if(filename.equals("userInfo.json") || f.isDirectory()){
                continue;
            }
            Log.d(TAG, "delete filename is: " + filename);

            f.delete();
        }
    }
    /**
     * @param facility_type : int representing the type of facility calling this function
     * @return String of facility type
     * @Pupose take int facility_type and return string of facility_type
     */
    private String getStringType(int facility_type) {
        String facilityToFetch = "";
        switch (facility_type) {
            case posts:
                facilityToFetch = "posts";
                break;
            case study:
                facilityToFetch = "studys";
                break;
            case entertainments:
                facilityToFetch = "entertainments";
                break;
            case restaurants:
                facilityToFetch = "restaurants";
                break;
            case report_user: //need ?
                facilityToFetch = "user";
                break;
            case report_facility:
                facilityToFetch = "facility";
                break;
            case report_comment:
                facilityToFetch = "comment";

                break;
        }
        return facilityToFetch;
    }
}
