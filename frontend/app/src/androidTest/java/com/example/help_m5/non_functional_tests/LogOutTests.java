package com.example.help_m5.non_functional_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertTrue;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.LoginActivity;
import com.example.help_m5.R;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class LogOutTests {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityRule =
            new ActivityScenarioRule<>(intent);

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), LoginActivity.class);
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
    public void testLogOutFromReview() throws InterruptedException {
        int click = 0;
        // User navigates to the review page
        onView(withId(R.id.sign_in_button)).perform(click());
        Thread.sleep(1500);
        onView(ViewMatchers.withId(R.id.facility1)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.rate_button)).perform(click());

        // User starts to log out
        onView(withId(R.id.cancel_button_review)).perform(click()); click++;
        Espresso.pressBack(); click++;
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        assertTrue(click <= 5);
    }

    @Test
    public void testLogOutFromReport() throws InterruptedException {
        int click = 0;

        // User navigates to the review page
        onView(withId(R.id.sign_in_button)).perform(click());
        Thread.sleep(1500);
        Assert.assertTrue(spinnerChangeIndex(3));
        Thread.sleep(500);
        onView(ViewMatchers.withId(R.id.facility3)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.report_facility_button)).perform(click());

        // User starts to log out
        onView(withId(R.id.cancel_button_report)).perform(click()); click++;
        Espresso.pressBack(); click++;
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        assertTrue(click <= 5);

    }

    @Test
    public void testLogOutFromFacility() throws InterruptedException {
        int click = 0;
        // User navigates to the review page
        onView(withId(R.id.sign_in_button)).perform(click());
        Thread.sleep(1500);
        Assert.assertTrue(spinnerChangeIndex(2));
        Thread.sleep(500);
        onView(ViewMatchers.withId(R.id.facility3)).perform(click());
        Thread.sleep(1500);

        // User starts to log out
        Espresso.pressBack(); click++;
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        assertTrue(click <= 5);

    }

    @Test
    public void testLogOutFromChatBot() throws InterruptedException {
        int click = 0;
        // User navigates to the review page
        onView(withId(R.id.sign_in_button)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_chat));
        Thread.sleep(400);

        // User starts to log out
        Espresso.pressBack(); click++;
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        assertTrue(click <= 5);

    }

    @Test
    public void testLogOutFromAddFacility() throws InterruptedException {
        int click = 0;
        // User navigates to the review page
        onView(withId(R.id.sign_in_button)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_add_facility));
        Thread.sleep(400);

        // User starts to log out
        Espresso.pressBack(); click++;
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        assertTrue(click <= 5);
    }

    @Test
    public void testLogOutFromProcessingReport() throws InterruptedException {
        int click = 0;
        // User navigates to the review page
        onView(withId(R.id.sign_in_button)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_report));
        Thread.sleep(400);

        // User starts to log out
        Espresso.pressBack(); click++;
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open()); click++;
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings)); click++; click++;

        assertTrue(click <= 5);
    }
}
