package com.example.help_m5.ui.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.help_m5.databinding.FragmentBrowseBinding;
import com.example.help_m5.ui.faclity.FacilityActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;

//DatabaseConnection
public class DatabaseConnection {

    protected String vm_ip = "http://20.213.243.141:8080/";
    final String TAG = "databaseConnection";

    //following are types of facility
    final int posts = 0;
    final int study = 1;
    final int entertainments = 2;
    final int restaurants = 3;
    final int report_user = 4;
    final int report_comment = 5;
    final int report_facility = 6;
    //above are types of facility
    private String userInfo = "userInfo.json";
    
    public void updateUserInfo(NavigationView navigationView, Context context, String user_id, Activity activity,boolean load){

        RequestQueue queue = Volley.newRequestQueue(context);
        HashMap<String, String> params = new HashMap<String, String>();
        queue.start();
        params.put("_id", user_id);
        params.put("username", "");
        params.put("user_logo", "");

        Log.d(TAG, "updateUserInfo params is " + params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, vm_ip+"google_sign_up", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.toString().equals("{}")){
                            Toast.makeText(context, "Unable to get update from server", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        writeToJson(context, response, userInfo);
                        if(load){
                            Log.d(TAG, "updateUserInfo response is "+response);
                            LoadToScreen loader = new LoadToScreen();
                            loader.loadUserInfo(navigationView, response, activity);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse updateUI " + "Error: " + error.getMessage());
                    }
                });
        queue.add(request);
    }

    /**
     * @param facility_type      : int representing the type of facility calling this function
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param facility_id  : string of facility_id
     * normal_server_load, indicate successfully send the data to server
     * server_error, indicate unsuccessfully send the data to server
     * @Pupose : to get a Specific facility by its facility id and type
     */
    public void getSpecificFacility(int facility_type, String facility_id, Context applicationContext, Activity activity){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(applicationContext);
        String url = vm_ip + "specific";
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);
        queue.start();
        HashMap<String, String> params = new HashMap<String, String>();
//        Log.d(TAG, "ssss type in getSpecific is "+facility_type);
        params.put("facility_id", facility_id);
        params.put("facilityType", String.valueOf(facility_type));


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "response getSpecificFacility is: " + response.toString());
                Bundle bundle = new Bundle();
                bundle.putString("userEmail", account.getEmail());
                bundle.putInt("facilityType", facility_type);
                bundle.putString("facility_id", facility_id);
                bundle.putString("facility_json", response.toString());
                Intent intent = new Intent(activity, FacilityActivity.class);
                intent.putExtras(bundle);
                Toast.makeText(applicationContext, "Opening facility", Toast.LENGTH_SHORT).show();
                applicationContext.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse getSpecificFacility " + "Error: " + error.getMessage());
            }
        });
        queue.add(jsObjRequest);
    }

    /**
     * @param binding            : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type      : int representing the type of facility calling this function
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param content_to_search  : string user typed in search box
     * @Pupose : to load the content from server our cached file to screen for user to view
     */
    public void getFacilities (Object binding, int facility_type, int pageNum, Context applicationContext,  String content_to_search, boolean[] options){
        String fileName = "";
        if (options[0]) {
            fileName = getStringType(facility_type) +"Search.json";
        } else {
            fileName = getStringType(facility_type) + ".json";
        }
        searchFacilities(binding, facility_type, pageNum, applicationContext, content_to_search, fileName, options );
    }


    /**
     * @param binding            : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type      : int representing the type of facility calling this function
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param content_to_search  : string user typed in search box
     * @Pupose : to load the content from server our cached file to screen for user to view
     */
    public void searchFacilities(Object binding, int facility_type, int pageNum, Context applicationContext, String content_to_search, String fileName,  boolean[] options) {
        boolean is_search = options[0];
        boolean nextPage = options[1];
        boolean previousPage = options[2];
        boolean reloadPage = options[3];
        Log.d("TESTING", fileName);
        if (isCached(applicationContext, fileName) && !reloadPage) {//page up and page down should go here
            Log.d("TESTING", "Goes here");
            try {
                JSONObject data = new JSONObject(readFromJson(applicationContext, fileName));
                loadToScreen(binding, applicationContext, facility_type, data, nextPage, previousPage, reloadPage, fileName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("TESTING", "Not here");
            final RequestQueue queue = Volley.newRequestQueue(applicationContext);
            HashMap<String, String> params = new HashMap<String, String>();
            queue.start();
            params.put("type", ""+ facility_type);
            String url = vm_ip;
            url += "facility";
            //url += getStringType(facility_type);
            if (is_search) {
                url += "/search";
                params.put("search", "" + content_to_search);
            }else{
                url += "/newest";
            }

            Log.d(TAG,"url in searchFacilities is :"+ url);
            Log.d(TAG, params.toString());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "current page db: " + pageNum);
                    try {
                        if(reloadPage){
                            response.put("current_page", pageNum);
                        }else {
                            response.put("current_page", 1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "response searchFacilities is: " + response.toString());

                    if(writeToJson(applicationContext, response, fileName) != 0){
                        Toast.makeText(applicationContext, "Error happened when loading data, please report to admin", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    Log.d(TAG, "readFromJson" + readFromJson(applicationContext,fileName));
                    loadToScreen(binding, applicationContext, facility_type, response, nextPage, previousPage, reloadPage, fileName);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse searchFacilities " + "Error: " + error.getMessage());
                    Toast.makeText(applicationContext, "ERROR when get data (facilities) from server", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsObjRequest);
        }
    }

    /**
     * @param binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param data          : Json format data to be process and show on screen
     * @Pupose : to load the content from server our cached file to screen for user to view
     */
    public void loadToScreen(Object binding, Context applicationContext, int facility_type, JSONObject data, boolean nextPage, boolean previousPage, boolean reloadPage, String fileName) {
        LoadToScreen loader = new LoadToScreen();
        try {
            int length = data.getInt("length");
            int current_page = data.getInt("current_page");
//            Log.d(TAG, "1 length is: "+length+", current_page is: "+current_page);

            if(previousPage && (current_page == 1)){
                Toast.makeText(applicationContext, "You are on the first page", Toast.LENGTH_SHORT).show();
                return;
            }else if(previousPage){
                current_page -= 1;
            }

            int start = current_page * 5;
//            Log.d(TAG, "1.5 startIndex is: "+start);

            if(nextPage && (start >= length)){
                Toast.makeText(applicationContext, "You are on the last page", Toast.LENGTH_SHORT).show();
                return;
            }else if (nextPage){
                current_page += 1;
            }
//            Log.d(TAG, "2 length is: "+length+", current_page is: "+current_page);
            if(!previousPage && !nextPage && !reloadPage){
                start = 0;
                current_page = 1;
                data.put("current_page", 1);
                writeToJson(applicationContext, data, fileName);
            }else {
                start = (current_page - 1) * 5;
                data.put("current_page", current_page);
                writeToJson(applicationContext, data, fileName);

            }
            int end = Math.min((current_page * 5), length);

//            Log.d(TAG, "3 startIndex is: "+start+", endIndex is: "+end);
//            Log.d(TAG, "4 readFromJson" + readFromJson(applicationContext,fileName));
            int counter = 0;


            JSONArray array = data.getJSONArray("result");

            FragmentBrowseBinding b1 = (FragmentBrowseBinding)binding;
            b1.facility1.setVisibility(View.INVISIBLE);
            FragmentBrowseBinding b2 = (FragmentBrowseBinding)binding;
            b2.facility2.setVisibility(View.INVISIBLE);
            FragmentBrowseBinding b3 = (FragmentBrowseBinding)binding;
            b3.facility3.setVisibility(View.INVISIBLE);
            FragmentBrowseBinding b4 = (FragmentBrowseBinding)binding;
            b4.facility4.setVisibility(View.INVISIBLE);
            FragmentBrowseBinding b5 = (FragmentBrowseBinding)binding;
            b5.facility5.setVisibility(View.INVISIBLE);

            /*
            String colorCode = "";
            System.out.println("IDKKKKKKKKKKKKKKKKKKLKKKKKK"+facility_type);
            if (facility_type == posts) {
                colorCode = "#7781AE";
            } else if (facility_type == restaurants) {
                colorCode = "#D2887A";
            } else if (facility_type == entertainments) {
                colorCode = "#00E5BC";
            } else if (facility_type == study) {
                colorCode = "#2B3858";
            }
            b1.ViewFacility1.setBackgroundColor(Color.parseColor(colorCode));
            b2.ViewFacility2.setBackgroundColor(Color.parseColor(colorCode));
            b3.ViewFacility3.setBackgroundColor(Color.parseColor(colorCode));
            b4.ViewFacility4.setBackgroundColor(Color.parseColor(colorCode));
            b5.ViewFacility5.setBackgroundColor(Color.parseColor(colorCode));

             */

            for (int index = start; index < end; index++) {
                loader.loadToFragment(binding, array.getJSONArray(index), counter);
                counter++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param fileName           :
     * @return true, if file is cached; false otherwise
     * @Pupose : to check if file is cached in internal storage, this method is used only by searchFacilities()
     */
    public boolean isCached(Context applicationContext, String fileName) {
//        Log.d(TAG, applicationContext.getFilesDir().toString()+"/"+fileName);
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
//            Log.d(TAG, "write to file" + fileName + " path is: " + file.getCanonicalPath());
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        }
        return 0;
    }
    //used by tests, not application
    public int writeToJsonForTesting(String path, JSONObject response, String fileName) {
        try {
            Log.d("TESTING", "Here: " +path+fileName);
//            File file = new File(path, fileName);
//            file.delete();
            File file2 = new File(path, fileName);
            FileOutputStream writer = new FileOutputStream(file2);
            writer.write(response.toString().getBytes());
            writer.close();
//            Log.d(TAG, "write to file" + fileName + " path is: " + file.getCanonicalPath());
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

//    public void removeFile(Context applicationContext, String fileName){
//        if(!isCached(applicationContext, fileName)){
//            File f = new File(applicationContext.getFilesDir().toString()+"/"+fileName);
//            f.delete();
//        }
//    }
//
//    public void removeFile(String filePath){
//        File f = new File(filePath);
//        f.delete();
//    }

    public int getCurrentPage(Context applicationContext, boolean isSearch, int facility_type){
        String fileName = "";
        if (isSearch) {
            fileName = "search.json";
        } else {
            fileName = getStringType(facility_type) + ".json";
        }
        if(!isCached(applicationContext, fileName)){
            return 1;
        }else {
            String result = readFromJson(applicationContext, fileName);
            try {
                JSONObject date = new JSONObject(result);
                return date.getInt("current_page");
            } catch (JSONException e) {
                e.printStackTrace();
                return 1;
            }
        }
    }
    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @Pupose clean all files stored in /data/data/com.example.help_m5/files/
     */
    public void cleanAllCaches(Context applicationContext){
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
//            Log.d(TAG, "filename is: "+filename);
            if(filename.equals("userInfo.json") || f.isDirectory()){
                continue;
            }
            Log.d(TAG, "delete filename is: " + filename);

            f.delete();
        }
    }

    public void cleanSearchCaches(Context applicationContext){
        if( applicationContext == null){
//            Log.d(TAG, "applicationContext Null");
            return;
        }
        File targetDir = applicationContext.getFilesDir();
        File[] files = targetDir.listFiles();
        if(files == null){
//            Log.d(TAG, "dir empty, nothing to clean");
            return;
        }
        for(File f : files){
            String filename = f.getName();
            if(!filename.contains("Search.json") || f.isDirectory()){
                continue;
            }
//            Log.d(TAG, "delete filename is: " + filename);
            f.delete();
        }
    }
    /**
     * @param facility_type : int representing the type of facility calling this function
     * @return String of facility type
     * @Pupose take int facility_type and return string of facility_type
     */
    protected String getStringType(int facility_type) {
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
            default:
                facilityToFetch = "";
                break;
        }
        return facilityToFetch;
    }
}
