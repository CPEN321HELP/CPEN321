package com.example.help_m5.manage_facility_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Rule;
import org.junit.Test;

public class ReportFacilityTests {

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

    @Test
    public void testReportFacilityButtonsAndLayout() {
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.report_facility_button)).check(matches(isDisplayed()));
        onView(withId(R.id.report_facility_button)).check(matches(withText("REPORT THIS FACILITY")));
        onView(withId(R.id.report_facility_button)).perform(click());
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
        onView(withId(R.id.report_facility_button)).perform(click());
        onView(withId(R.id.reportFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.submit_button_report)).perform(click());
        onView(withText("Please state your reason of report")).inRoot(new ToastMatcher())
                .check(matches(withText("Please state your reason of report")));

        onView(withId(R.id.cancel_button_report)).perform(click());
    }

    @Test
    public void testFullSubmissionWithoutCheckbox() throws InterruptedException {
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.report_facility_button)).perform(click());
        onView(withId(R.id.reportFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextReport))
                .perform(typeText("Facility is permanently closed on campus"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_report)).perform(click());
        onView(withText("Report successfully sent!")).inRoot(new ToastMatcher())
                .check(matches(withText("Report successfully sent!")));

        Thread.sleep(1500);
        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));
    }

    @Test
    public void testFullSubmissionWithCheckbox() throws InterruptedException {
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.report_facility_button)).perform(click());
        onView(withId(R.id.reportFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextReport))
                .perform(typeText("Not a real facility, the publisher is trolling"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.checkbox_user)).perform(click());
        onView(withId(R.id.checkbox_user)).check(matches(isChecked()));
        onView(withId(R.id.submit_button_report)).perform(click());
        onView(withText("Report successfully sent with associated user!")).inRoot(new ToastMatcher())
                .check(matches(withText("Report successfully sent with associated user!")));

        Thread.sleep(1500);
        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));
    }

}
