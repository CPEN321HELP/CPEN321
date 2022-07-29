package com.example.help_m5.manage_facility_tests;

import static androidx.core.util.Preconditions.checkNotNull;
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
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.FacilityActivity;
import com.example.help_m5.R;
import com.example.help_m5.RecyclerViewMatcher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

public class VoteSystemTests {

    @Rule
    public ActivityScenarioRule<FacilityActivity> mActivityRule =
            new ActivityScenarioRule<FacilityActivity>(intent);

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), FacilityActivity.class);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ApplicationProvider.getApplicationContext());
        Bundle bundle = new Bundle();
        intent.putExtra("userEmail", account.getEmail());
        intent.putExtra("facility_id", "11");
        intent.putExtra("facilityType", 2);
        intent.putExtra("facility_json", "{\"_id\":11,\"facility\":{\"facilityType\":\"entertainments\",\"facility_status\":\"normal\",\"facilityTitle\":\"Iona Island (British Columbia)\",\"facilityDescription\":\"Iona Island in Richmond, British Columbia, Canada was formerly an island, but is now a peninsula physically connected to Sea Island via a causeway and Ferguson Road. Iona is home to a primary sewage treatment plant (located in the middle), an animal refuge and a park (Iona Beach Regional Park). The Iona Sewage Plant is located near the centre of the island and has tours for the public. Iona Beach Regional Park also features a beach adjacent to wildlife from the nearby animal refuge. The park is managed by Metro Vancouver. Iona Island is located almost adjacent to the Vancouver International Airport. The park is mostly visited by birders, as the sewage ponds have attracted many rare shorebirds such as Spoon-billed Sandpiper, Great Knot, and Red-necked Stint.\",\"facilityImageLink\":\"https:\\/\\/imgtu.com\\/i\\/jTfY8I\",\"facilityOverallRate\":null,\"numberOfRates\":1,\"timeAdded\":\"2022\\/6\\/19\",\"longitude\":-123.1685486,\"latitude\":49.2056385},\"rated_user\":[{}],\"reviews\":[{},{\"replierID\":\"lufei8351@gmail.com\",\"userName\":\"Peter Na\",\"rateScore\":4,\"upVotes\":0,\"downVotes\":0,\"replyContent\":\"Nice place\",\"timeOfReply\":\"2022\\/6\\/29\\/9\\/29\\/48\"}],\"adderID\":\"l2542293790@gmail.com\",\"ratedUser\":[{\"replierID\":\"lufei8351@gmail.com\"}]}");
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

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void checkVotingLayout() {
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
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
    public void upVoteTest() {
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
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
    public void downVoteTest() {
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
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
    public void VoteChangeTest() {
        onView(withId(R.id.facilityActivityView)).perform(swipeUp());
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
