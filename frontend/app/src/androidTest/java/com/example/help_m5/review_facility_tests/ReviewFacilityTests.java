package com.example.help_m5.review_facility_tests;

import static androidx.test.espresso.Espresso.onData;
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
import com.example.help_m5.SetRatingHelper;
import com.example.help_m5.ToastMatcher;
import com.example.help_m5.database.DatabaseConnection;
import com.example.help_m5.menu.BrowseFragment;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReviewFacilityTests {

    DatabaseConnection db;
    FragmentScenario<BrowseFragment> mfragment;
    @Before
    public void setUp() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable");
        db = new DatabaseConnection();
        mfragment = FragmentScenario.launchInContainer(BrowseFragment.class, null, R.style.MyMaterialTheme, Lifecycle.State.STARTED);
    }

    private boolean spinnerChangeIndex(int indexSpinner){
        try{
            onView(withId(R.id.spinnerFacility)).perform(click());
            onData(Matchers.anything()).atPosition(indexSpinner).perform(click());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void navigateToRate() throws InterruptedException {
        Assert.assertTrue(spinnerChangeIndex(1));
        Thread.sleep(1900);
        onView(withId(R.id.facility1)).perform(click());
        Thread.sleep(1900);
    }

    @Test
    public void testButtonsAndLayout() throws InterruptedException {
        navigateToRate();
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));
        onView(withId(R.id.RateFacilityTitle)).check(matches(withText("Rate this Facility")));
        onView(withId(R.id.RateFacilityDescription)).check(matches(withText("Please select some stars and give\nyour feedback")));
        onView(withId(R.id.ratingBar2)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_button_review)).check(matches(isEnabled()));
        onView(withId(R.id.submit_button_review)).check(matches(isEnabled()));
        onView(withId(R.id.cancel_button_review)).perform(click());
        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptySubmission() throws InterruptedException {
        navigateToRate();
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.submit_button_review)).perform(click());
        Thread.sleep(1000);
        onView(withText("Please do not submit an empty form")).inRoot(new ToastMatcher())
                .check(matches(withText("Please do not submit an empty form")));

        onView(withId(R.id.cancel_button_review)).perform(click());
    }

    @Test
    public void testPartialSubmissionRating() throws InterruptedException {
        navigateToRate();

        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextTextMultiLine)).perform(typeText("Great overall experience!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_review)).perform(click());
        Thread.sleep(1000);
        onView(withText("Please rate the facility from 0.5 to 5")).inRoot(new ToastMatcher())
                .check(matches(withText("Please rate the facility from 0.5 to 5")));

        onView(withId(R.id.cancel_button_review)).perform(click());
    }

    @Test
    public void testPartialSubmissionComment() throws InterruptedException {
        navigateToRate();

        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.ratingBar2)).perform(SetRatingHelper.setRatingBar());
        onView(withId(R.id.submit_button_review)).perform(click());
        Thread.sleep(1000);
        onView(withText("Please add a comment")).inRoot(new ToastMatcher())
                .check(matches(withText("Please add a comment")));

        onView(withId(R.id.cancel_button_review)).perform(click());
    }

    @Test
    public void testFullSubmission() throws InterruptedException {
        navigateToRate();

        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.ratingBar2)).perform(SetRatingHelper.setRatingBar());
        onView(withId(R.id.editTextTextMultiLine)).perform(typeText("Great overall experience!"));
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
    }

}
