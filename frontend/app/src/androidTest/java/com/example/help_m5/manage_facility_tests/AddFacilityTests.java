package com.example.help_m5.manage_facility_tests;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.hamcrest.Matchers;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import com.example.help_m5.R;
import com.example.help_m5.menu.AddFacilityFragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AddFacilityTests {

    FragmentScenario<AddFacilityFragment> mfragment;
    @Before
    public void setUp() throws Exception {
        mfragment = FragmentScenario.launchInContainer(AddFacilityFragment.class);
        mfragment.moveToState(Lifecycle.State.STARTED);
    }
    @Test
    public void testBadTitle(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityTitle)).perform(ViewActions.typeText("Bad"));
        Espresso.onView(ViewMatchers.withId(R.id.imageNewFacilityTitle)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("bad"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testGoodTitle(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityTitle)).perform(ViewActions.typeText("Long enough"));
        Espresso.onView(ViewMatchers.withId(R.id.imageNewFacilityTitle)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("good"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testBadDescription(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityDescription)).perform(ViewActions.typeText("is too short"));
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityDescription)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("bad"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testGoodDescription(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityDescription)).perform(ViewActions.typeText("This is a excellent description because it is Matchers.not too short, and more than 50 characters"));
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityDescription)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("good"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testSpinnerPost(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityType)).perform(ViewActions.click());
        Espresso.onData(Matchers.anything()).atPosition(1).perform(ViewActions.click());
        try {
            Espresso.onView(ViewMatchers.withId(R.id.newFacilityLocation)).perform(ViewActions.typeText("cause error"));
        }catch (Exception e){
            //should go here since it is not visible so can not be scrollTo
        }
    }

    @Test
    public void testSpinnerNotPost(){
        Espresso.onData(Matchers.anything()).atPosition(2).perform(ViewActions.click());
        Espresso.onData(Matchers.anything()).atPosition(2).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
            Espresso.onView(ViewMatchers.withId(R.id.newFacilityLocation)).perform(ViewActions.typeText("not cause error"));
        }catch (Exception e){
            Assert.fail();
            //should go here since it is not visible so can not be scrollTo
        }
    }

    @Test
    public void testBadImageLink() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityImageLink)).perform(ViewActions.typeText("BAD URL"));
        Thread.sleep(1000);
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityImageLink)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("bad"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testGoodImageLink() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityImageLink)).perform(ViewActions.typeText("https://imgtu.com/i/jwCDjH"));
        Thread.sleep(1000);
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityImageLink)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("good"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testBadLocation() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityLocation)).perform(ViewActions.typeText("Matchers.Not_A_Address"));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityDescription)).perform(ViewActions.click());
        Thread.sleep(1000);
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityLocation)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("bad"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testGoodLocation() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityLocation)).perform(ViewActions.typeText("650 W 41st Ave, Vancouver, BC V5Z 2M9"));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityDescription)).perform(ViewActions.click());
        Thread.sleep(1000);
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityLocation)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("good"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testGoodType(){
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityType)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("bad"))));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityType)).perform(ViewActions.click());
        Espresso.onData(Matchers.anything()).atPosition(1).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityType)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("good"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void ReadyToSubmit() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityLocation)).perform(ViewActions.typeText("650 W 41st Ave, Vancouver, BC V5Z 2M9"));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityTitle)).perform(ViewActions.typeText("Oakridge Centre"));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityDescription)).perform(ViewActions.typeText("Oakridge Centre is a shopping centre in the Oakridge neighborhood of Vancouver, British Columbia, Canada."));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityImageLink)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityImageLink)).perform(ViewActions.typeText("https://imgtu.com/i/jwCDjH"));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityImageLink)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityType)).perform(ViewActions.click());
        Espresso.onData(Matchers.anything()).atPosition(2).perform(ViewActions.click());
        Thread.sleep(1000);
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(ViewMatchers.isEnabled()));
    }

}