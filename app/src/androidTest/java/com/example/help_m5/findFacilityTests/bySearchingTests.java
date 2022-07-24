package com.example.help_m5.findFacilityTests;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertTrue;
import static java.util.EnumSet.allOf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.FacilityActivity;
import com.example.help_m5.R;
import com.example.help_m5.ui.home.HomeFragment;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;

public class bySearchingTests {
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
    public void TestSearchAndChangeSpinner(){
        assertTrue(testSpinnerAndSearch(3));

        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_previous)).perform(click());
        onView(withId(R.id.fab_previous)).perform(click());
        assertTrue(testFacilitySearch1Show(entertainments, "11"));
        assertTrue(testFacilitySearch5Show(entertainments, "7"));
        onView(withId(R.id.fab_next)).perform(click());
        assertTrue(testFacilitySearch1Show(entertainments, "6"));
        assertTrue(testFacilitySearch5Show(entertainments, "2"));

        assertTrue(testSpinnerAndSearch(2));
        onView(withId(R.id.fab_previous)).perform(click());
        onView(withId(R.id.fab_previous)).perform(click());
        assertTrue(testFacilitySearch1Show(study, "11"));
        assertTrue(testFacilitySearch5Show(study, "7"));
        onView(withId(R.id.fab_next)).perform(click());
        assertTrue(testFacilitySearch1Show(study, "6"));
        assertTrue(testFacilitySearch5Show(study, "2"));

        assertTrue(testSpinnerAndSearch(1));
        onView(withId(R.id.fab_previous)).perform(click());
        onView(withId(R.id.fab_previous)).perform(click());
        assertTrue(testFacilitySearch1Show(restaurants, "11"));
        assertTrue(testFacilitySearch5Show(restaurants, "7"));
        onView(withId(R.id.fab_next)).perform(click());
        assertTrue(testFacilitySearch1Show(restaurants, "6"));
        assertTrue(testFacilitySearch5Show(restaurants, "2"));

        assertTrue(testSpinnerAndSearch(0));
        onView(withId(R.id.fab_previous)).perform(click());
        onView(withId(R.id.fab_previous)).perform(click());
        assertTrue(testFacilitySearch1Show(posts, "11"));
        assertTrue(testFacilitySearch5Show(posts, "7"));
        onView(withId(R.id.fab_next)).perform(click());
        assertTrue(testFacilitySearch1Show(posts, "6"));
        assertTrue(testFacilitySearch5Show(posts, "2"));
    }

    private boolean testSpinnerAndSearch(int indexSpinner){
        try{
            onView(withId(R.id.spinnerFacility)).perform(click());
            onData(anything()).atPosition(indexSpinner).perform(click());
            onView(ViewMatchers.withId(R.id.searchFacility)).perform(click());
            onView(ViewMatchers.withId(R.id.searchFacility)).perform(typeText("a"));
            onView(ViewMatchers.withId(R.id.searchFacility)).perform(closeSoftKeyboard());
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

    private boolean testFacilitySearch1Show(int facility_type, String facility_id){
        String title = "title "+getTypeInString(facility_type)+" search "+facility_id;
        String content = "content "+getTypeInString(facility_type)+" search "+facility_id;
        String date = "date "+getTypeInString(facility_type)+" search "+facility_id;
        try{
            onView(ViewMatchers.withId(R.id.titleTextView_facility1)).check(matches(withText(title)));
            onView(ViewMatchers.withId(R.id.dateTextView_facility1)).check(matches(withText(date)));
            onView(ViewMatchers.withId(R.id.contentTextView_facility1)).check(matches(withText(content)));
            return true;
        }catch (Exception E){
            return false;
        }
    }

    private boolean testFacilitySearch5Show(int facility_type, String facility_id){
        String title = "title "+getTypeInString(facility_type)+" search "+facility_id;
        String content = "content "+getTypeInString(facility_type)+" search "+facility_id;
        String date = "date "+getTypeInString(facility_type)+" search "+facility_id;
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
