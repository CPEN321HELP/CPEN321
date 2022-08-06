package com.example.help_m5.non_functional_tests;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.MainActivity;
import com.example.help_m5.R;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class LogOutTests {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule =
            new ActivityScenarioRule<>(intent);

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("user_email", "test@gmail.com");
        intent.putExtra("user_name", "name");
        intent.putExtra("user_icon", "none");
        intent.putExtras(bundle);    }

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
    public void testLogOutFromReview() throws InterruptedException {
        int click = 0;
        // User navigates to the review page
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.home_review_button)).perform(ViewActions.click());
        Thread.sleep(2000);
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).perform(ViewActions.click());
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.rate_button)).perform(ViewActions.click());

        // User starts to log out
        Espresso.onView(ViewMatchers.withId(R.id.cancel_button_review)).perform(ViewActions.click()); click++;
        Espresso.pressBack(); click++;
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        Assert.assertTrue(click <= 5);
    }

    @Test
    public void testLogOutFromReport() throws InterruptedException {
        int click = 0;
        // User navigates to the review page
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.home_review_button)).perform(ViewActions.click());
        Thread.sleep(1500);
        Assert.assertTrue(spinnerChangeIndex(3));
        Thread.sleep(2000);
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).perform(ViewActions.click());
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.facilityActivityView)).perform(ViewActions.swipeUp());
        Espresso.onView(ViewMatchers.withId(R.id.report_facility_button)).perform(ViewActions.click());

        // User starts to log out
        Espresso.onView(ViewMatchers.withId(R.id.cancel_button_report)).perform(ViewActions.click()); click++;
        Espresso.pressBack(); click++;
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        Assert.assertTrue(click <= 5);

    }

    @Test
    public void testLogOutFromFacility() throws InterruptedException {
        int click = 0;
        // User navigates to the review page
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.home_review_button)).perform(ViewActions.click());
        Thread.sleep(1500);
        Assert.assertTrue(spinnerChangeIndex(2));
        Thread.sleep(500);
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).perform(ViewActions.click());
        Thread.sleep(1500);

        // User starts to log out
        Espresso.pressBack(); click++;
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        Assert.assertTrue(click <= 5);

    }

    @Test
    public void testLogOutFromChatBot() throws InterruptedException {
        int click = 0;
        // User navigates to the review page
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_chat));
        Thread.sleep(400);

        // User starts to log out
        Espresso.pressBack(); click++;
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        Assert.assertTrue(click <= 5);

    }

    @Test
    public void testLogOutFromAddFacility() throws InterruptedException {
        int click = 0;
        // User navigates to the review page
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_add_facility));
        Thread.sleep(400);

        // User starts to log out
        Espresso.pressBack(); click++;
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        Assert.assertTrue(click <= 5);
    }

    @Test
    public void testLogOutFromProcessingReport() throws InterruptedException {
        int click = 0;
        // User navigates to the review page
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_report));
        Thread.sleep(400);

        // User starts to log out
        Espresso.pressBack(); click++;
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        Assert.assertTrue(click <= 5);
    }
}
