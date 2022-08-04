package com.example.help_m5.manage_facility_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
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
import com.example.help_m5.ui.browse.BrowseFragment;
import com.example.help_m5.ui.database.DatabaseConnection;

import org.junit.Before;
import org.junit.Test;

public class ReportFacilityTestsNew {

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
    public void testReportFacilityButtonsAndLayout() {
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_close_or_refresh)).perform(click());
        onView(withId(R.id.facility1)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_close_or_refresh)).perform(click());
        onView(withId(R.id.facility1)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_close_or_refresh)).perform(click());
        onView(withId(R.id.facility1)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
