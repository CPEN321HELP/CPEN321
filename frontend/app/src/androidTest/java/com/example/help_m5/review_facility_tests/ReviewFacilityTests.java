package com.example.help_m5.review_facility_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.FacilityActivity;
import com.example.help_m5.R;
import com.example.help_m5.ToastMatcher;
import com.example.help_m5.SetRating;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Rule;
import org.junit.Test;

public class ReviewFacilityTests {

    @Rule
    public ActivityScenarioRule<FacilityActivity> mActivityRule =
            new ActivityScenarioRule<FacilityActivity>(intent);

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), FacilityActivity.class);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ApplicationProvider.getApplicationContext());
        Bundle bundle = new Bundle();
        intent.putExtra("userEmail", account.getEmail());
        intent.putExtra("facility_id", "6");
        intent.putExtra("facilityType", 3);
        intent.putExtra("facility_json", "{\"_id\":6,\"facility\":{\"facilityType\":\"restaurants\",\"facility_status\":\"normal\",\"facilityTitle\":\"McDonald's\",\"facilityDescription\":\"Famous fast food restaurant that serves burgers, fries, soft drinks, and a variety of other fast food options. \",\"facilityImageLink\":\"https:\\/\\/s3-media0.fl.yelpcdn.com\\/bphoto\\/13GWBclQVEzXzkMkxZXIRA\\/o.jpg\",\"facilityOverallRate\":0,\"numberOfRates\":0,\"timeAdded\":\"2022\\/6\\/11\",\"longitude\":-123.24253759999999,\"latitude\":49.266646699999995},\"rated_user\":[],\"reviews\":[],\"adderID\":\"\",\"ratedUser\":[]}");
        intent.putExtras(bundle);
    }

    @Test
    public void testButtonsAndLayout() {
        onView(withId(R.id.rate_button)).check(matches(withText("RATE")));
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));
        onView(withId(R.id.RateFacilityTitle)).check(matches(withText("Rate this Facility")));
        onView(withId(R.id.RateFacilityDescription))
                .check(matches(withText("Please select some stars and give\nyour feedback")));
        onView(withId(R.id.ratingBar2)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_button_review)).check(matches(isEnabled()));
        onView(withId(R.id.submit_button_review)).check(matches(isEnabled()));
        onView(withId(R.id.cancel_button_review)).perform(click());
        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptySubmission() {
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.submit_button_review)).perform(click());
        onView(withText("Please do not submit an empty form")).inRoot(new ToastMatcher())
                .check(matches(withText("Please do not submit an empty form")));

        onView(withId(R.id.cancel_button_review)).perform(click());
    }

    @Test
    public void testPartialSubmissionRating() {
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextTextMultiLine)).perform(typeText("Great overall experience!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_review)).perform(click());
        onView(withText("Please rate the facility from 0.5 to 5")).inRoot(new ToastMatcher())
                .check(matches(withText("Please rate the facility from 0.5 to 5")));

        onView(withId(R.id.cancel_button_review)).perform(click());
    }

    @Test
    public void testPartialSubmissionComment() {
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.ratingBar2)).perform(SetRating.setRatingBar());
        onView(withId(R.id.submit_button_review)).perform(click());
        onView(withText("Please add a comment")).inRoot(new ToastMatcher())
                .check(matches(withText("Please add a comment")));

        onView(withId(R.id.cancel_button_review)).perform(click());
    }

    @Test
    public void testFullSubmission() throws InterruptedException {
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.ratingBar2)).perform(SetRating.setRatingBar());
        onView(withId(R.id.editTextTextMultiLine)).perform(typeText("Great overall experience!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_review)).perform(click());

        onView(withText("Success!")).inRoot(new ToastMatcher())
                .check(matches(withText("Success!")));

        Thread.sleep(1500);

        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.ratingBar2)).perform(SetRating.setRatingBar());
        onView(withId(R.id.editTextTextMultiLine))
                .perform(typeText("Great overall experience!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_review)).perform(click());

        onView(withText("You have reviewed in the past.")).inRoot(new ToastMatcher())
                .check(matches(withText("You have reviewed in the past.")));

        Thread.sleep(1500);

        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));


    }

}
