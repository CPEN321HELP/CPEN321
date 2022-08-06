package com.example.help_m5.manage_facility_tests;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.MainActivity;
import com.example.help_m5.R;
import com.example.help_m5.RecyclerViewMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VoteSystemTests {

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

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
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

    /*
    @Test
    public void A_beforeAll() throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.home_add_facility_button)).perform(ViewActions.click());
        Thread.sleep(1500);
    }

     */

    @Test
    public void checkVotingLayout() throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.home_review_button)).perform(ViewActions.click());
        Thread.sleep(1500);
        Assert.assertTrue(spinnerChangeIndex(2));
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).perform(ViewActions.click());
        Thread.sleep(1500);
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        Thread.sleep(1500);
        onView(withId(R.id.facilityRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withId(R.id.upVote)))));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVote))
                .check(matches(isNotChecked()));
        onView(withId(R.id.facilityRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withId(R.id.downVote)))));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVote))
                .check(matches(isNotChecked()));
        onView(withId(R.id.facilityRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withId(R.id.upVoteCount)))));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVoteCount))
                .check(matches(withText("0")));
        onView(withId(R.id.facilityRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withId(R.id.downVoteCount)))));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVoteCount))
                .check(matches(withText("0")));
    }

    @Test
    public void upVoteTest() throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.home_review_button)).perform(ViewActions.click());
        Thread.sleep(1500);
        Assert.assertTrue(spinnerChangeIndex(2));
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).perform(ViewActions.click());
        Thread.sleep(1500);
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        Thread.sleep(1500);
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVote))
                .perform(click());
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVote))
                .check(matches(isChecked()));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVoteCount))
                .check(matches(withText("1")));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVote))
                .perform(click());
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVote))
                .check(matches(isNotChecked()));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVoteCount))
                .check(matches(withText("0")));
    }

    @Test
    public void downVoteTest() throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.home_review_button)).perform(ViewActions.click());
        Thread.sleep(1500);
        Assert.assertTrue(spinnerChangeIndex(2));
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).perform(ViewActions.click());
        Thread.sleep(1500);
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        Thread.sleep(1500);
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVote))
                .perform(click());
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVote))
                .check(matches(isChecked()));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVoteCount))
                .check(matches(withText("1")));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVote))
                .perform(click());
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVote))
                .check(matches(isNotChecked()));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVoteCount))
                .check(matches(withText("0")));
    }

    @Test
    public void VoteChangeTest() throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(ViewMatchers.withId(R.id.home_review_button)).perform(ViewActions.click());
        Thread.sleep(1500);
        Assert.assertTrue(spinnerChangeIndex(2));
        Thread.sleep(500);
        Espresso.onView(ViewMatchers.withId(R.id.facility1)).perform(ViewActions.click());
        Thread.sleep(1500);
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
        Thread.sleep(500);
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVote))
                .perform(click());
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVote))
                .check(matches(isChecked()));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVoteCount))
                .check(matches(withText("1")));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVote))
                .check(matches(isNotChecked()));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVoteCount))
                .check(matches(withText("0")));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVote))
                .perform(click());
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVote))
                .check(matches(isChecked()));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVoteCount))
                .check(matches(withText("1")));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVote))
                .check(matches(isNotChecked()));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.upVoteCount))
                .check(matches(withText("0")));
        onView(withRecyclerView(R.id.facilityRecyclerView).atPositionOnView(0, R.id.downVote))
                .perform(click());
    }

}
