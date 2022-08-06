package com.example.help_m5.non_functional_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.ChatActivity;
import com.example.help_m5.R;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

public class ChatBotTests {

    @Rule
    public ActivityScenarioRule<ChatActivity> mActivityRule =
            new ActivityScenarioRule<>(intent);

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), ChatActivity.class);
    }

    @Test
    public void testResponseOne() throws InterruptedException {
        onView(withId(R.id.topButton)).perform(click());
        onView(withId(R.id.topButton)).perform(click());
        Thread.sleep(400);
        onView(withId(R.id.chatBackButton)).check(matches(ViewMatchers.withTagValue(Matchers.equalTo("true"))));
    }

    @Test
    public void testResponseTwo() throws InterruptedException {
        onView(withId(R.id.midButton)).perform(click());
        onView(withId(R.id.midButton)).perform(click());
        Thread.sleep(400);
        onView(withId(R.id.chatBackButton)).check(matches(ViewMatchers.withTagValue(Matchers.equalTo("true"))));
    }

    @Test
    public void testResponseThree() throws InterruptedException {
        onView(withId(R.id.botButton)).perform(click());
        onView(withId(R.id.botButton)).perform(click());
        onView(withId(R.id.midButton)).perform(click());
        Thread.sleep(400);
        onView(withId(R.id.chatBackButton)).check(matches(ViewMatchers.withTagValue(Matchers.equalTo("true"))));
    }
}
