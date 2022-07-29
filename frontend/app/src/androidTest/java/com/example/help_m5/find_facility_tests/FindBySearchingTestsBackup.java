package com.example.help_m5.find_facility_tests;

import android.util.Log;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.FacilityActivity;
import com.example.help_m5.R;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.example.help_m5.ui.home.HomeFragment;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class FindBySearchingTestsBackup {
        /*
        Please see m8 document for set up
     */

    final int posts = 0;
    final int study = 1;
    final int entertainments = 2;
    final int restaurants = 3;
    final int search = 4;

    FragmentScenario<HomeFragment> mfragment;
    ActivityScenarioRule<FacilityActivity> mActivity;
    DatabaseConnection db;

    @Before
    public void setUp() throws Exception {
        db = new DatabaseConnection();
        mfragment = FragmentScenario.launchInContainer(HomeFragment.class, null, R.style.MyMaterialTheme, Lifecycle.State.STARTED);
    }

    @Test
    public void testRefreshButtonIconChange(){
        Espresso.onView(ViewMatchers.withId(R.id.fab_main)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("refresh"))));
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.typeText("a"));
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("close"))));
    }

    @Test
    public void testNoResult(){
        Espresso.onView(ViewMatchers.withId(R.id.fab_main)).perform(ViewActions.click());

        Assert.assertTrue(testNoResultHelper(posts,3));
        Assert.assertTrue(testNoResultHelper(restaurants,2));
        Assert.assertTrue(testNoResultHelper(study,1));
        Assert.assertTrue(testNoResultHelper(posts,0));
    }

    public boolean testNoResultHelper(int facility_type, int indexSpinner){
        Espresso.onView(ViewMatchers.withId(R.id.spinnerFacility)).perform(ViewActions.click());
        Espresso.onData(Matchers.anything()).atPosition(indexSpinner).perform(ViewActions.click());
        try {
            createJsonForTesting(0, facility_type, true);
            Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.typeText("a"));
            Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.facility2)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.facility3)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
            Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.facility4)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
            Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.facility5)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Test
    public void testReadAndWriteToJson(){
        createJsonForTesting(11, posts, true);
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.typeText("a"));
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.closeSoftKeyboard());
        try {
            Thread.sleep(5000);
        }catch (Exception e){

        }
        Assert.assertTrue(testFacilitySearch1Show(posts, "11",true));
        Assert.assertTrue(testFacilitySearch5Show(posts, "7",true));
    }

    private boolean createJsonForTesting(int length, int facility_type,boolean isSearch){
        try{
            JSONArray ja = new JSONArray();
            JSONObject jo = new JSONObject();
            jo.put("length" , length);
            jo.put("current_page" , 1);

            for(int i = length; i>0; i--){
                ja.put(new JSONArray(new Object[]{i, 0, "title " + getTypeInString(facility_type) + (isSearch ? " search " : " ")+i, "content " + getTypeInString(facility_type) + (isSearch ? " search " : " ")+i, "date " + getTypeInString(facility_type) + (isSearch ? " search " : " ")+i}));
            }
            jo.put("result", ja);

            String fileName = "";
            if (isSearch) {
                fileName = getTypeInString(facility_type) +"Search.json";
            } else {
                fileName = getTypeInString(facility_type) + ".json";
            }
            db.writeToJsonForTesting("/data/data/com.example.help_m5/files/", jo, fileName);
            Log.d("TESTING", jo.toString());
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Test
    public void entertainmentsTests(){
        Assert.assertTrue(createJsonForTesting(11, entertainments, true));
        Assert.assertTrue(testSpinnerAndSearch(3));
        Espresso.onView(ViewMatchers.withId(R.id.fab_main)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(entertainments, "11",true));
        Assert.assertTrue(testFacilitySearch5Show(entertainments, "7",true));
        Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(entertainments, "6",true));
        Assert.assertTrue(testFacilitySearch5Show(entertainments, "2",true));
        Assert.assertTrue(createJsonForTesting(11, entertainments, false));
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(entertainments, "11",false));
        Assert.assertTrue(testFacilitySearch5Show(entertainments, "7",false));
    }

    @Test
    public void studyTests(){
        Assert.assertTrue(createJsonForTesting(11, study, true));
        Assert.assertTrue(testSpinnerAndSearch(2));
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(study, "11",true));
        Assert.assertTrue(testFacilitySearch5Show(study, "7",true));
        Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(study, "6",true));
        Assert.assertTrue(testFacilitySearch5Show(study, "2",true));
        Assert.assertTrue(createJsonForTesting(11, study, false));
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(study, "11",false));
        Assert.assertTrue(testFacilitySearch5Show(study, "7",false));
    }

    @Test
    public void restaurantsTests(){
        Assert.assertTrue(createJsonForTesting(11, restaurants, true));
        Assert.assertTrue(testSpinnerAndSearch(1));
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(restaurants, "11",true));
        Assert.assertTrue(testFacilitySearch5Show(restaurants, "7",true));
        Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(restaurants, "6",true));
        Assert.assertTrue(testFacilitySearch5Show(restaurants, "2",true));
        Assert.assertTrue(createJsonForTesting(11, restaurants, false));
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(restaurants, "11",false));
        Assert.assertTrue(testFacilitySearch5Show(restaurants, "7",false));
    }

    @Test
    public void postsTests(){
        Assert.assertTrue(createJsonForTesting(11, posts, true));
        Assert.assertTrue(testSpinnerAndSearch(0));
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(posts, "11",true));
        Assert.assertTrue(testFacilitySearch5Show(posts, "7",true));
        Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(posts, "6",true));
        Assert.assertTrue(testFacilitySearch5Show(posts, "2",true));
        Assert.assertTrue(createJsonForTesting(11, posts, false));
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).perform(ViewActions.click());
        Assert.assertTrue(testFacilitySearch1Show(posts, "11",false));
        Assert.assertTrue(testFacilitySearch5Show(posts, "7",false));
    }

    private boolean testSpinnerAndSearch(int indexSpinner){
        try{
            Espresso.onView(ViewMatchers.withId(R.id.spinnerFacility)).perform(ViewActions.click());
            Espresso.onData(Matchers.anything()).atPosition(indexSpinner).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.typeText("a"));
            Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.closeSoftKeyboard());
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean testFacilitySearch1Show(int facility_type, String facility_id, boolean isSearch){
        String title = "title "+getTypeInString(facility_type)+ (isSearch ? " search " : " ") +facility_id;
        String content = "content "+getTypeInString(facility_type)+(isSearch ? " search " : " ")+facility_id;
        String date = "date "+getTypeInString(facility_type)+ (isSearch ? " search " : " ") +facility_id;
        try{
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(ViewAssertions.matches(ViewMatchers.withText(title)));
            Espresso.onView(ViewMatchers.withId(R.id.dateTextView_facility1)).check(ViewAssertions.matches(ViewMatchers.withText(date)));
            Espresso.onView(ViewMatchers.withId(R.id.contentTextView_facility1)).check(ViewAssertions.matches(ViewMatchers.withText(content)));
            return true;
        }catch (Exception E){
            return false;
        }
    }

    private boolean testFacilitySearch5Show(int facility_type, String facility_id, boolean isSearch){
        String title = "title "+getTypeInString(facility_type)+ (isSearch ? " search " : " ") +facility_id;
        String content = "content "+getTypeInString(facility_type)+(isSearch ? " search " : " ")+facility_id;
        String date = "date "+getTypeInString(facility_type)+ (isSearch ? " search " : " ") +facility_id;
        try{
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility5)).check(ViewAssertions.matches(ViewMatchers.withText(title)));
            Espresso.onView(ViewMatchers.withId(R.id.dateTextView_facility5)).check(ViewAssertions.matches(ViewMatchers.withText(date)));
            Espresso.onView(ViewMatchers.withId(R.id.contentTextView_facility5)).check(ViewAssertions.matches(ViewMatchers.withText(content)));
            return true;
        }catch (Exception E){
            return false;
        }
    }
    private boolean avoidRace(){
        try{
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("hide"))));
            return true;
        }catch (Throwable e){
            return false;
        }
    }

    private String getTypeInString(int type){
        switch (type){
            case posts:
                return "posts";
            case study:
                return "studys";
            case entertainments:
                return "entertainments";
            case restaurants:
                return "restaurants";
            case search:
                return "search";
            default:
                return "none";
        }
    }
}
