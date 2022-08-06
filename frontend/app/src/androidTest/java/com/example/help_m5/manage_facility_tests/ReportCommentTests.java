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
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.platform.app.InstrumentationRegistry;
import com.example.help_m5.R;
import com.example.help_m5.RecyclerViewActionHelper;
import com.example.help_m5.ToastMatcher;
import com.example.help_m5.database.DatabaseConnection;
import com.example.help_m5.menu.BrowseFragment;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportCommentTests {

    DatabaseConnection db;
    FragmentScenario<BrowseFragment> mfragment;
    @Before
    public void setUp() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable");
        db = new DatabaseConnection();
        mfragment = FragmentScenario.launchInContainer(BrowseFragment.class, null, R.style.MyMaterialTheme, Lifecycle.State.STARTED);
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
    public void a_addComment() throws InterruptedException {
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_close_or_refresh)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.facility1)).perform(click());
        Thread.sleep(500);
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

    @Test
    public void testReportCommentButtonsAndLayout() throws InterruptedException {
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_close_or_refresh)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.facility1)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.facilityRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withText("Report")))));
        onView(withId(R.id.facilityRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerViewActionHelper.clickChildViewWithId(R.id.reportCommentButton)));
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
    public void testEmptySubmission() throws InterruptedException {
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_close_or_refresh)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.facility1)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.facilityRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerViewActionHelper.clickChildViewWithId(R.id.reportCommentButton)));
        Thread.sleep(1000);
        onView(withId(R.id.reportFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.submit_button_report)).perform(click());
        Thread.sleep(1000);
        onView(withText("Please state your reason of report")).inRoot(new ToastMatcher())
                .check(matches(withText("Please state your reason of report")));

        onView(withId(R.id.cancel_button_report)).perform(click());
    }

    @Test
    public void testFullSubmissionWithoutCheckbox() throws InterruptedException {
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_close_or_refresh)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.facility1)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.facilityRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerViewActionHelper.clickChildViewWithId(R.id.reportCommentButton)));
        onView(withId(R.id.reportFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextReport))
                .perform(typeText("Inappropriate Comment, contains fake content"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_report)).perform(click());
        Thread.sleep(1000);
        onView(withText("Report successfully sent!")).inRoot(new ToastMatcher())
                .check(matches(withText("Report successfully sent!")));
        Thread.sleep(1000);
    }

    @Test
    public void testFullSubmissionWithCheckbox() throws InterruptedException {
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.fab_close_or_refresh)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.facility1)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        onView(withId(R.id.facilityRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerViewActionHelper.clickChildViewWithId(R.id.reportCommentButton)));
        onView(withId(R.id.reportFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextReport))
                .perform(typeText("Inappropriate Comment, contains fake content, the commenter should be penalized"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.checkbox_user)).perform(click());
        onView(withId(R.id.checkbox_user)).check(matches(isChecked()));
        onView(withId(R.id.submit_button_report)).perform(click());
        Thread.sleep(1000);
        onView(withText("Report successfully sent with associated user!")).inRoot(new ToastMatcher())
                .check(matches(withText("Report successfully sent with associated user!")));
        Thread.sleep(1000);
    }
}
