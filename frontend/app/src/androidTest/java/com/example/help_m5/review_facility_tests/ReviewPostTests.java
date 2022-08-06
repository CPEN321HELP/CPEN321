package com.example.help_m5.review_facility_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.help_m5.R;
import com.example.help_m5.ToastMatcher;
import com.example.help_m5.database.DatabaseConnection;
import com.example.help_m5.menu.BrowseFragment;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReviewPostTests {

    DatabaseConnection db;
    FragmentScenario<BrowseFragment> mfragment;
    @Before
    public void setUp() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable");
        db = new DatabaseConnection();
        mfragment = FragmentScenario.launchInContainer(BrowseFragment.class, null, R.style.MyMaterialTheme, Lifecycle.State.STARTED);
    }

    @Test
    public void testButtonsAndLayout() throws InterruptedException {
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_close_or_refresh)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.facility1)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.rate_button)).check(matches(withText("COMMENT")));
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));
        onView(withId(R.id.RateFacilityTitle)).check(matches(withText("Comment on a Post")));
        onView(withId(R.id.RateFacilityDescription))
                .check(matches(withText("Please leave your comments\nbelow")));
        onView(withId(R.id.ratingBar2)).check(matches(Matchers.not(isDisplayed())));
        onView(withId(R.id.cancel_button_review)).check(matches(isEnabled()));
        onView(withId(R.id.submit_button_review)).check(matches(isEnabled()));
        onView(withId(R.id.cancel_button_review)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));

    }

    @Test
    public void testEmptySubmission()  throws InterruptedException {
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_close_or_refresh)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.facility1)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));
        onView(withId(R.id.submit_button_review)).perform(click());
        Thread.sleep(1000);
        onView(withText("Please do not submit an empty form")).inRoot(new ToastMatcher())
                .check(matches(withText("Please do not submit an empty form")));
        onView(withId(R.id.cancel_button_review)).perform(click());
        Thread.sleep(500);

    }

    @Test
    public void testFullSubmission() throws InterruptedException {
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_close_or_refresh)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.facility1)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextTextMultiLine)).perform(typeText("Nice Post!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_review)).perform(click());
        Thread.sleep(1000);
        try{
            onView(withText("Success!")).inRoot(new ToastMatcher()).check(matches(withText("Success!")));
        }catch (Throwable t){
            t.printStackTrace();
            try{
                onView(withText("You have commented in the past.")).inRoot(new ToastMatcher()).check(matches(withText("You have commented in the past.")));
            }catch (Throwable tt){
                t.printStackTrace();
                Assert.fail();
            }
        }
//        onView(withText("You have reviewed in the past.")).inRoot(new ToastMatcher()).check(matches(withText("You have reviewed in the past.")));
    }

}
