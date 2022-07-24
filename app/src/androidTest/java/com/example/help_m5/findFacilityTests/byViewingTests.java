package com.example.help_m5.findFacilityTests;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.FacilityActivity;
import com.example.help_m5.R;
import com.example.help_m5.databinding.FragmentHomeBinding;
import com.example.help_m5.ui.add_facility.AddFacilityFragment;
import com.example.help_m5.ui.home.HomeFragment;

import org.junit.Before;
import org.junit.Test;

public class byViewingTests {

    /*
        In order to test the facility shows up in correct place with expected content,
        please use the predefined json files in the folder name "JsonForTesting"
        please put all files in "JsonForTesting" folder to path "/data/data/com.example.help_m5/files"
        on the simulator.
        Then disable the clean cache function in DatabaseConnection by comment out following lines:
            line 66, DBconnection.cleanAllCaches(getContext());
            Line 265, DBconnection.cleanAllCaches(getContext());
     */

    final int posts = 0;
    final int study = 1;
    final int entertainments = 2;
    final int restaurants = 3;
    final int search = 4;

    FragmentScenario<HomeFragment> mfragment;
    ActivityScenarioRule<FacilityActivity> mActivity;
    @Before
    public void setUp() throws Exception {
        mfragment = FragmentScenario.launchInContainer(HomeFragment.class, null, R.style.MyMaterialTheme, Lifecycle.State.STARTED);

    }

    @Test
    public void testPageUpAndDown(){
        //if first facility and last facility shows up, then  the 3 facility in middle should show up as expected. show facility with id from 11 to 7
        assertTrue(testFacility1Show(posts, "11"));
        assertTrue(testFacility5Show(posts, "7"));
        onView(ViewMatchers.withId(R.id.fab_main)).perform(click());
        try {//wait for animation
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //test content not change be cause we are on first page
        onView(ViewMatchers.withId(R.id.fab_previous)).perform(click());
        assertTrue(testFacility1Show(posts, "11"));
        assertTrue(testFacility5Show(posts, "7"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //test content next page, show facility with id from 6 to 2
        onView(ViewMatchers.withId(R.id.fab_next)).perform(click());
        assertTrue(testFacility1Show(posts, "6"));
        assertTrue(testFacility5Show(posts, "2"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.fab_next)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(testFacility1Show(posts, "1"));
        onView(ViewMatchers.withId(R.id.fab_previous)).perform(click());
        onView(ViewMatchers.withId(R.id.fab_previous)).perform(click());
    }

    @Test
    public void testSpinner(){
        onView(withId(R.id.spinnerFacility)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        assertTrue(testFacility1Show(restaurants, "11"));
        assertTrue(testFacility5Show(restaurants, "7"));

        onView(withId(R.id.spinnerFacility)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        assertTrue(testFacility1Show(study, "11"));
        assertTrue(testFacility5Show(study, "7"));

        onView(withId(R.id.spinnerFacility)).perform(click());
        onData(anything()).atPosition(3).perform(click());
        assertTrue(testFacility1Show(entertainments, "11"));
        assertTrue(testFacility5Show(entertainments, "7"));

        onView(withId(R.id.spinnerFacility)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        assertTrue(testFacility1Show(posts, "11"));
        assertTrue(testFacility5Show(posts, "7"));
    }

    @Test
    public void testRefresh(){
        onView(withId(R.id.spinnerFacility)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(ViewMatchers.withId(R.id.fab_main)).perform(click());
        onView(ViewMatchers.withId(R.id.fab_next)).perform(click());
        onView(ViewMatchers.withId(R.id.fab_next)).perform(click());
        onView(ViewMatchers.withId(R.id.fab_next)).perform(click());
        assertTrue(testFacility1Show(restaurants, "1"));
        assertFalse(testFacility5Show(restaurants, "7"));
        onView(ViewMatchers.withId(R.id.fab_close_or_refresh)).perform(click());
        try {//wait for animation
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(testFacility1Show(restaurants, "11"));
        assertTrue(testFacility5Show(restaurants, "7"));


    }

    private boolean testFacility1Show(int facility_type, String facility_id){
        String title = "title "+getTypeInString(facility_type)+" "+facility_id;
        String content = "content "+getTypeInString(facility_type)+" "+facility_id;
        String date = "date "+getTypeInString(facility_type)+" "+facility_id;
        try{
            onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(matches(withText(title)));
            onView(ViewMatchers.withId(R.id.dateTextView_facility1)).check(matches(withText(date)));
            onView(ViewMatchers.withId(R.id.contentTextView_facility1)).check(matches(withText(content)));
            return true;
        }catch (Exception E){
            return false;
        }
    }

    private boolean testFacility5Show(int facility_type, String facility_id){
        String title = "title "+getTypeInString(facility_type)+" "+facility_id;
        String content = "content "+getTypeInString(facility_type)+" "+facility_id;
        String date = "date "+getTypeInString(facility_type)+" "+facility_id;
        try{
            onView(ViewMatchers.withId(R.id.titleTextView_facility5)).check(matches(withText(title)));
            onView(ViewMatchers.withId(R.id.dateTextView_facility5)).check(matches(withText(date)));
            onView(ViewMatchers.withId(R.id.contentTextView_facility5)).check(matches(withText(content)));
            return true;
        }catch (Exception E){
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
