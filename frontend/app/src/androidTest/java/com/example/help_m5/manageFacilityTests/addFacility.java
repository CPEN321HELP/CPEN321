package com.example.help_m5.manageFacilityTests;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.hamcrest.Matchers;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import com.example.help_m5.R;
import com.example.help_m5.ui.add_facility.AddFacilityFragment;
import org.junit.Before;
import org.junit.Test;

public class addFacility {
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
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testGoodTitle(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityTitle)).perform(ViewActions.typeText("Long enough"));
        Espresso.onView(ViewMatchers.withId(R.id.imageNewFacilityTitle)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("good"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testBadDescription(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityDescription)).perform(ViewActions.typeText("This is a bad description because it is too short"));
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityDescription)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("bad"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testGoodDescription(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityDescription)).perform(ViewActions.typeText("This is a excellent description because it is Matchers.not too short, and more than 50 characters"));
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityDescription)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("good"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testBadImageLink(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityImageLink)).perform(ViewActions.typeText("BAD URL"));
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityImageLink)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("bad"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testGoodImageLink(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityImageLink)).perform(ViewActions.typeText("https://imgtu.com/i/jwCDjH"));
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityImageLink)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("good"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testBadLocation(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityLocation)).perform(ViewActions.typeText("Matchers.Not_A_Address"));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityDescription)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityLocation)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("bad"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testGoodLocation(){
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityLocation)).perform(ViewActions.typeText("650 W 41st Ave, Vancouver, BC V5Z 2M9"));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityDescription)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityLocation)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityLocation)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("good"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void testGoodType(){
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityType)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("bad"))));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityType)).perform(ViewActions.click());
        Espresso.onData(Matchers.anything()).atPosition(1).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.imageFacilityType)).check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo("good"))));
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
    }

    @Test
    public void ReadyToSubmit(){
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityTitle)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityTitle)).perform(ViewActions.typeText("Long enough"));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityDescription)).perform(ViewActions.typeText("This is a excellent description because it is Matchers.not too short, and more than 50 characters"));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityLocation)).perform(ViewActions.typeText("650 W 41st Ave, Vancouver, BC V5Z 2M9"));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityImageLink)).perform(ViewActions.scrollTo(), ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityImageLink)).perform(ViewActions.typeText("https://imgtu.com/i/jwCDjH"));
        Espresso.onView(ViewMatchers.withId(R.id.newFacilityType)).perform(ViewActions.scrollTo(), ViewActions.click());
        Espresso.onData(Matchers.anything()).atPosition(2).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.submitAll)).check(ViewAssertions.matches(ViewMatchers.isEnabled()));
    }
}