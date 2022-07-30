package com.example.help_m5.find_facility_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
import com.example.help_m5.ui.home.HomeFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FindByViewingTests {
    /*
      In order to test the facility shows up in correct place with expected content,
      please use the predefined json files in the folder name "JsonForTesting"
      please put all files in "JsonForTesting" folder to path "/data/data/com.example.help_m5/files"
      on the simulator.
      Then disable the clean cache function in DatabaseConnection by comment out following line:
          DBconnection.cleanAllCaches(getContext());
          in private void initFavMenu(),
   */
    private static String TAG = "FindByViewingTests";
    final int posts = 0;
    final int study = 1;
    final int entertainments = 2;
    final int restaurants = 3;
    final int search = 4;

    DatabaseConnection db;
    FragmentScenario<HomeFragment> mfragment;
    @Before
    public void setUp() throws Exception {
        db = new DatabaseConnection();
        mfragment = FragmentScenario.launchInContainer(HomeFragment.class, null, R.style.MyMaterialTheme, Lifecycle.State.STARTED);
    }

    @Test
    public void changeFacilityType(){
        Assert.assertTrue(spinnerChangeIndex(3));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility2)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility3)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility4)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility5)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Assert.assertTrue(spinnerChangeIndex(2));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility2)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility3)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility4)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility5)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Assert.assertTrue(spinnerChangeIndex(1));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility2)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility3)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility4)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility5)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Assert.assertTrue(spinnerChangeIndex(0));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility2)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility3)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility4)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.facility5)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
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
    public void navigationTest(){
        Espresso.onView(ViewMatchers.withId(R.id.fab_next)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("hide"))));
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("hide"))));
        Espresso.onView(ViewMatchers.withId(R.id.fab_main)).perform(ViewActions.click());
        if(avoidRace()){
            Espresso.onView(ViewMatchers.withId(R.id.fab_main)).perform(ViewActions.click());
        }
        Espresso.onView(ViewMatchers.withId(R.id.fab_next)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("show"))));
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("show"))));
        Espresso.onView(ViewMatchers.withId(R.id.fab_main)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fab_next)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("hide"))));
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("hide"))));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    @Test
    public void pageUpDownRefreshTest(){
        spinnerChangeIndex(0);
        Espresso.onView(ViewMatchers.withId(R.id.fab_main)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).perform(ViewActions.click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.fab_previous)).perform(ViewActions.click());
        onView(withText("You are on the first page")).inRoot(new ToastMatcher()).check(matches(withText("You are on the first page")));
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
            onView(withText("You are on the last page")).inRoot(new ToastMatcher()).check(matches(withText("You are on the last page")));
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
    public void openSpecific() {
        Espresso.onView(ViewMatchers.withId(R.id.fab_main)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.facility2)).perform(ViewActions.click());
        String severResponse = readFromJson(posts);
        Assert.assertNotEquals(severResponse, "");
        Log.d(TAG, "severResponse is: "+severResponse);
        try {
            JSONObject summary = new JSONObject(severResponse);
            JSONArray facilities = summary.getJSONArray("result");
            JSONArray facility1 = facilities.getJSONArray(1);
            String facility1Title = facility1.getString(2);
            Espresso.onView(ViewMatchers.withId(R.id.facilityTitle)).check(ViewAssertions.matches(ViewMatchers.withText(facility1Title)));
        }catch (JSONException e){
            Assert.fail();
        }
    }

    private String readFromJson(int facility_type){
        String fileName = getTypeInString(facility_type) + ".json";
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
