package com.example.help_m5.manage_facility_tests;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.example.help_m5.ui.faclity.FacilityActivity;
import com.example.help_m5.R;
import com.example.help_m5.RecyclerViewAction;
import com.example.help_m5.ToastMatcher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

public class ReportCommentTests {

    @Rule
    public ActivityScenarioRule<FacilityActivity> mActivityRule =
            new ActivityScenarioRule<FacilityActivity>(intent);

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), FacilityActivity.class);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ApplicationProvider.getApplicationContext());
        Bundle bundle = new Bundle();
        intent.putExtra("userEmail", account.getEmail());
        intent.putExtra("facility_id", "9");
        intent.putExtra("facilityType", 3);
        intent.putExtra("facility_json", "{\"_id\":9,\"facility\":{\"facility_status\":\"normal\",\"facilityType\":\"restaurants\",\"facilityTitle\":\"Jamjar Canteen\",\"facilityDescription\":\"Jamjar Canteen is Lebanese food simplified. We believe in using only the freshest local ingredients and cooking with love. Our locations are all unique and ...\",\"timeAdded\":\"2022\\/6\\/11\",\"facilityImageLink\":\"https:\\/\\/imgtu.com\\/i\\/j6A7yn\",\"facilityOverallRate\":3.5,\"numberOfRates\":1,\"longitude\":-123.24720589999998,\"latitude\":49.2663131},\"rated_user\":[],\"reviews\":[{\"replierID\":\"thongn29798@gmail.com\",\"userName\":\"Thong Nguyen\",\"rateScore\":3.5,\"upVotes\":0,\"downVotes\":0,\"replyContent\":\"Jamjar is the jam\\n\\n\",\"timeOfReply\":\"2022\\/6\\/27\\/1\\/6\\/29\"}],\"ratedUser\":[{\"replierID\":\"thongn29798@gmail.com\"}],\"adderID\":\"\"}");
        intent.putExtras(bundle);
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    @Test
    public void testReportCommentButtonsAndLayout() {
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.facilityRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withText("Report")))));
        onView(withId(R.id.facilityRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerViewAction.clickChildViewWithId(R.id.reportCommentButton)));
        onView(withId(R.id.reportFacilityView)).check(matches(isDisplayed()));
        onView(withId(R.id.ReportTitle)).check(matches(withText("Report Content")));
        onView(withId(R.id.ReportDescription))
                .check(matches(withText("Please provide your reason for\nreporting below")));
        onView(withId(R.id.checkbox_user)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextReport)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_button_report)).check(matches(isEnabled()));
        onView(withId(R.id.submit_button_report)).check(matches(isEnabled()));
        onView(withId(R.id.cancel_button_report)).perform(click());
        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptySubmission() {
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.facilityRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerViewAction.clickChildViewWithId(R.id.reportCommentButton)));
        onView(withId(R.id.reportFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.submit_button_report)).perform(click());
        onView(withText("Please state your reason of report")).inRoot(new ToastMatcher())
                .check(matches(withText("Please state your reason of report")));

        onView(withId(R.id.cancel_button_report)).perform(click());
    }

    @Test
    public void testFullSubmissionWithoutCheckbox() {
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.facilityRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerViewAction.clickChildViewWithId(R.id.reportCommentButton)));
        onView(withId(R.id.reportFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextReport))
                .perform(typeText("Inappropriate Comment, contains fake content"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_report)).perform(click());
        onView(withText("Report successfully sent!")).inRoot(new ToastMatcher())
                .check(matches(withText("Report successfully sent!")));
    }

    @Test
    public void testFullSubmissionWithCheckbox() {
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.facilityRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerViewAction.clickChildViewWithId(R.id.reportCommentButton)));
        onView(withId(R.id.reportFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextReport))
                .perform(typeText("Inappropriate Comment, contains fake content, the commenter should be penalized"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.checkbox_user)).perform(click());
        onView(withId(R.id.checkbox_user)).check(matches(isChecked()));
        onView(withId(R.id.submit_button_report)).perform(click());
        onView(withText("Report successfully sent with associated user!")).inRoot(new ToastMatcher())
                .check(matches(withText("Report successfully sent with associated user!")));
    }

}
