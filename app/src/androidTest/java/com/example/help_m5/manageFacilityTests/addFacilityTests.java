package com.example.help_m5.manageFacilityTests;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.help_m5.R;
import com.example.help_m5.ui.add_facility.AddFacilityFragment;
import org.junit.Before;
import org.junit.Test;

public class addFacilityTests {
    FragmentScenario<AddFacilityFragment> mfragment;
    @Before
    public void setUp() throws Exception {
        mfragment = FragmentScenario.launchInContainer(AddFacilityFragment.class);
        mfragment.moveToState(Lifecycle.State.STARTED);
    }
    @Test
    public void testBadTitle(){
        onView(ViewMatchers.withId(R.id.newFacilityTitle)).perform(typeText("Bad"));
        onView(withId(R.id.imageNewFacilityTitle)).check(matches(withTagValue(equalTo("bad"))));
        onView(withId(R.id.submitAll)).perform(scrollTo());
        onView(withId(R.id.submitAll)).check(matches(not(isEnabled())));
    }

    @Test
    public void testGoodTitle(){
        onView(withId(R.id.newFacilityTitle)).perform(typeText("Long enough"));
        onView(withId(R.id.imageNewFacilityTitle)).check(matches(withTagValue(equalTo("good"))));
        onView(withId(R.id.submitAll)).perform(scrollTo());
        onView(withId(R.id.submitAll)).check(matches(not(isEnabled())));
    }

    @Test
    public void testBadDescription(){
        onView(withId(R.id.newFacilityDescription)).perform(typeText("This is a bad description because it is too short"));
        onView(withId(R.id.imageFacilityDescription)).check(matches(withTagValue(equalTo("bad"))));
        onView(withId(R.id.submitAll)).perform(scrollTo());
        onView(withId(R.id.submitAll)).check(matches(not(isEnabled())));
    }

    @Test
    public void testGoodDescription(){
        onView(withId(R.id.newFacilityDescription)).perform(typeText("This is a excellent description because it is not too short, and more than 50 characters"));
        onView(withId(R.id.imageFacilityDescription)).check(matches(withTagValue(equalTo("good"))));
        onView(withId(R.id.submitAll)).perform(scrollTo());
        onView(withId(R.id.submitAll)).check(matches(not(isEnabled())));
    }

    @Test
    public void testBadImageLink(){
        onView(withId(R.id.newFacilityImageLink)).perform(typeText("BAD URL"));
        onView(withId(R.id.imageFacilityImageLink)).check(matches(withTagValue(equalTo("bad"))));
        onView(withId(R.id.submitAll)).perform(scrollTo());
        onView(withId(R.id.submitAll)).check(matches(not(isEnabled())));
    }

    @Test
    public void testGoodImageLink(){
        onView(withId(R.id.newFacilityImageLink)).perform(typeText("https://imgtu.com/i/jwCDjH"));
        onView(withId(R.id.imageFacilityImageLink)).check(matches(withTagValue(equalTo("good"))));
        onView(withId(R.id.submitAll)).perform(scrollTo());
        onView(withId(R.id.submitAll)).check(matches(not(isEnabled())));
    }

    @Test
    public void testBadLocation(){
        onView(withId(R.id.newFacilityLocation)).perform(typeText("Not_A_Address"));
        onView(withId(R.id.newFacilityDescription)).perform(click());
        onView(withId(R.id.imageFacilityLocation)).check(matches(withTagValue(equalTo("bad"))));
        onView(withId(R.id.submitAll)).perform(scrollTo());
        onView(withId(R.id.submitAll)).check(matches(not(isEnabled())));
    }

    @Test
    public void testGoodLocation(){
        onView(withId(R.id.newFacilityLocation)).perform(typeText("650 W 41st Ave, Vancouver, BC V5Z 2M9"));
        onView(withId(R.id.newFacilityDescription)).perform(click());
        onView(withId(R.id.newFacilityLocation)).perform(click());
        onView(withId(R.id.imageFacilityLocation)).check(matches(withTagValue(equalTo("good"))));
        onView(withId(R.id.submitAll)).perform(scrollTo());
        onView(withId(R.id.submitAll)).check(matches(not(isEnabled())));
    }

    @Test
    public void testGoodType(){
        onView(withId(R.id.imageFacilityType)).check(matches(withTagValue(equalTo("bad"))));
        onView(withId(R.id.newFacilityType)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.imageFacilityType)).check(matches(withTagValue(equalTo("good"))));
        onView(withId(R.id.submitAll)).perform(scrollTo());
        onView(withId(R.id.submitAll)).check(matches(not(isEnabled())));
    }

    @Test
    public void ReadyToSubmit(){
        onView(withId(R.id.submitAll)).perform(scrollTo());
        onView(withId(R.id.submitAll)).check(matches(not(isEnabled())));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.newFacilityTitle)).perform(scrollTo());
        onView(withId(R.id.newFacilityTitle)).perform(typeText("Long enough"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.newFacilityDescription)).perform(typeText("This is a excellent description because it is not too short, and more than 50 characters"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.newFacilityLocation)).perform(typeText("650 W 41st Ave, Vancouver, BC V5Z 2M9"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.newFacilityImageLink)).perform(scrollTo(), click());
        onView(withId(R.id.newFacilityImageLink)).perform(typeText("https://imgtu.com/i/jwCDjH"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.newFacilityType)).perform(scrollTo(), click());
        onData(anything()).atPosition(2).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.submitAll)).perform(scrollTo());
        onView(withId(R.id.submitAll)).check(matches(isEnabled()));

    }
}