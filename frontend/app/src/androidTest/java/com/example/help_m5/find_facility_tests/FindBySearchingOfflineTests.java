package com.example.help_m5.find_facility_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.help_m5.R;
import com.example.help_m5.ToastMatcher;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.example.help_m5.ui.home.HomeFragment;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class FindBySearchingOfflineTests {
    DatabaseConnection db;
    FragmentScenario<HomeFragment> mfragment;
    @Before
    public void setUp(){
        db = new DatabaseConnection();
        mfragment = FragmentScenario.launchInContainer(HomeFragment.class, null, R.style.MyMaterialTheme, Lifecycle.State.STARTED);
    }

    @Test
    public void emptyResponse(){
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi disable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data disable");
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.typeText("the"));
        Espresso.onView(ViewMatchers.withId(R.id.searchFacility)).perform(ViewActions.closeSoftKeyboard());
        Assert.assertTrue(spinnerChangeIndex(3));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility2)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility3)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility4)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility5)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Assert.assertTrue(spinnerChangeIndex(2));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility2)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility3)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility4)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility5)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));

        Assert.assertTrue(spinnerChangeIndex(1));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility2)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility3)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility4)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility5)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));

        Assert.assertTrue(spinnerChangeIndex(0));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility2)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility3)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility4)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.facility5)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
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
}
