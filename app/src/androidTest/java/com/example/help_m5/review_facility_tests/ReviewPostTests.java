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

import com.example.help_m5.ui.faclity.FacilityActivity;
import com.example.help_m5.R;
import com.example.help_m5.ToastMatcher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

public class ReviewPostTests {

    @Rule
    public ActivityScenarioRule<FacilityActivity> mActivityRule =
            new ActivityScenarioRule<FacilityActivity>(intent);

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), FacilityActivity.class);
        GoogleSignInAccount account = GoogleSignIn
                .getLastSignedInAccount(ApplicationProvider.getApplicationContext());
        Bundle bundle = new Bundle();
        intent.putExtra("userEmail", "test@gmail.com");
        intent.putExtra("facility_id", "13");
        intent.putExtra("facilityType", 0);
        intent.putExtra("facility_json", "{\"_id\":13,\"facility\":{\"facility_status\":\"normal\",\"facilityType\":\"posts\",\"facilityTitle\":\"CPEN321 Midterm is coming\",\"facilityDescription\":\"• Requirements and Design  \\n– types of requirements \\n– use cases \\n– components and interfaces \\n– sequence diagrams \\n• Code quality, code smells \\n• Testing and analysis \\n– test coverage \\n– control flow \\n– data flow \\n– symbolic execution\",\"timeAdded\":\"2022\\/6\\/19\",\"facilityImageLink\":\"https:\\/\\/imgtu.com\\/i\\/jTiAY9\",\"facilityOverallRate\":0,\"numberOfRates\":2,\"longitude\":null,\"latitude\":null},\"rated_user\":[{}],\"reviews\":[{\"replierID\":\"lufei8351@gmail.com\",\"userName\":\"Peter Na\",\"rateScore\":0,\"upVotes\":0,\"downVotes\":0,\"replyContent\":\"Nice Post!\",\"timeOfReply\":\"2022\\/6\\/29\\/1\\/11\\/52\"}],\"adderID\":\"wuyuheng0525@gmail.com\",\"ratedUser\":[{\"replierID\":\"lufei8351@gmail.com\"}]}");
        intent.putExtras(bundle);
    }

    @Test
    public void testButtonsAndLayout() {
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
    public void testFullSubmission() throws InterruptedException {
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextTextMultiLine)).perform(typeText("Nice Post!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_review)).perform(click());

        onView(withText("Success!")).inRoot(new ToastMatcher())
                .check(matches(withText("Success!")));

        Thread.sleep(1500);

        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextTextMultiLine)).perform(typeText("Nice Post!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_review)).perform(click());

        onView(withText("You have commented in the past.")).inRoot(new ToastMatcher())
                .check(matches(withText("You have commented in the past.")));

        Thread.sleep(1500);
        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));
    }
}
