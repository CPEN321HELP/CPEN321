package com.example.help_m5.find_facility_tests;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;

import android.util.Log;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.help_m5.R;
import com.example.help_m5.ToastMatcher;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.example.help_m5.ui.browse.BrowseFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class FindBySearchingTests {

    /*
      In order to test the facility shows up in correct place with expected content,
      please use the predefined json files in the folder name "JsonForTesting"
      please put all files in "JsonForTesting" folder to path "/data/data/com.example.help_m5/files"
      on the simulator.
      Then disable the clean cache function in DatabaseConnection by comment out following line:
          DBconnection.cleanAllCaches(getContext());
          in private void initFavMenu(),
   */
    private static String TAG = "FindBySearchingTests";
    final int posts = 0;
    final int study = 1;
    final int entertainments = 2;
    final int restaurants = 3;
    final int search = 4;

    DatabaseConnection db;
    FragmentScenario<BrowseFragment> mfragment;
    @Before
    public void setUp(){
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable");
        db = new DatabaseConnection();
        mfragment = FragmentScenario.launchInContainer(BrowseFragment.class, null, R.style.MyMaterialTheme, Lifecycle.State.STARTED);
    }

    @Test
    public void changeFacilityType(){
        Assert.assertTrue(spinnerChangeIndex(0));
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.typeText("the"));
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.closeSoftKeyboard());
        Assert.assertTrue(spinnerChangeIndex(3));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(ViewAssertions.matches(ViewMatchers.withText("the play")));
        Assert.assertTrue(spinnerChangeIndex(2));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(ViewAssertions.matches(ViewMatchers.withText("the studys")));
        Assert.assertTrue(spinnerChangeIndex(1));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(ViewAssertions.matches(ViewMatchers.withText("the resturant")));
        Assert.assertTrue(spinnerChangeIndex(0));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(ViewAssertions.matches(ViewMatchers.withText("the post")));

    }

    private boolean spinnerChangeIndex(int indexSpinner){
        try{
            Espresso.onView(ViewMatchers.withId(R.id.spinnerFacility)).perform(ViewActions.click());
            Espresso.onData(Matchers.anything()).atPosition(indexSpinner).perform(ViewActions.click());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Test
    public void pageUpDownRefreshTest(){
        Assert.assertTrue(spinnerChangeIndex(0));
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.typeText("the"));
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.fab_main)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).perform(ViewActions.click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("You are on the first page")).inRoot(new ToastMatcher()).check(ViewAssertions.matches(ViewMatchers.withText("You are on the first page")));
        String severResponse = readFromJson(posts);
        Assert.assertNotEquals(severResponse, "");
        Log.d(TAG, "severResponse is: "+severResponse);
        try {
            JSONObject summary = new JSONObject(severResponse);
            JSONArray facilities = summary.getJSONArray("result");
            JSONArray facility1 = facilities.getJSONArray(0);
            String facility1Title = facility1.getString(2);
            JSONArray facility5 = facilities.getJSONArray(4);
            String facility5Title = facility5.getString(2);
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(ViewAssertions.matches(ViewMatchers.withText(facility1Title)));
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility5)).check(ViewAssertions.matches(ViewMatchers.withText(facility5Title)));

            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.withText(facility1Title))));
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility5)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.withText(facility5Title))));

            Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(ViewAssertions.matches(ViewMatchers.withText(facility1Title)));
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility5)).check(ViewAssertions.matches(ViewMatchers.withText(facility5Title)));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.fab_next)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.withText(facility1Title))));
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility5)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.withText(facility5Title))));
            Espresso.onView(ViewMatchers.withText("You are on the last page")).inRoot(new ToastMatcher()).check(ViewAssertions.matches(ViewMatchers.withText("You are on the last page")));
            Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).perform(ViewActions.click());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(ViewAssertions.matches(ViewMatchers.withText(facility1Title)));
            Espresso.onView(ViewMatchers.withId(R.id.titleTextView_facility5)).check(ViewAssertions.matches(ViewMatchers.withText(facility5Title)));
        }catch (JSONException e){
            Assert.fail();
        }
    }

    @Test
    public void closeTest(){
        Assert.assertTrue(spinnerChangeIndex(0));
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.typeText("the"));
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("close"))));
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("refresh"))));

    }

    private String readFromJson(int facility_type){
        String fileName = getTypeInString(facility_type) +"Search.json";
        String path = "/data/data/com.example.help_m5/files/";
        try {
            File file = new File(path, fileName);
            FileReader fileReader = null;
            fileReader = new FileReader(file);
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
            return "";
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
