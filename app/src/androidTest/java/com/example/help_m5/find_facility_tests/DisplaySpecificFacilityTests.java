package com.example.help_m5.find_facility_tests;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import static org.hamcrest.CoreMatchers.containsString;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.FacilityActivity;
import com.example.help_m5.MainActivity;
import com.example.help_m5.R;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.example.help_m5.ui.home.HomeFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class DisplaySpecificFacilityTests {
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), FacilityActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("facility_id", "6");
        intent.putExtra("facilityType", 3);
        intent.putExtra("facility_json", "{\"_id\":6,\"facility\":{\"facilityType\":\"restaurants\",\"facility_status\":\"normal\",\"facilityTitle\":\"test Title\",\"facilityDescription\":\"Famous fast food restaurant that serves burgers, fries, soft drinks, and a variety of other fast food options. \",\"facilityImageLink\":\"https:\\/\\/s3-media0.fl.yelpcdn.com\\/bphoto\\/13GWBclQVEzXzkMkxZXIRA\\/o.jpg\",\"facilityOverallRate\":0,\"numberOfRates\":0,\"timeAdded\":\"2022\\/6\\/11\",\"longitude\":-123.24253759999999,\"latitude\":49.266646699999995},\"rated_user\":[],\"reviews\":[],\"adderID\":\"\"}");
        intent.putExtras(bundle);
    }

    @Rule
    public ActivityScenarioRule<FacilityActivity> mActivityRule =
            new ActivityScenarioRule<FacilityActivity>(intent);

    @Test
    public void specificFacilityShowTest() throws InterruptedException {
        Thread.sleep(20000);
        Espresso.onView(ViewMatchers.withId(R.id.facilityTitle)).check(ViewAssertions.matches(ViewMatchers.withText(containsString("test Title"))));
        Espresso.onView(ViewMatchers.withId(R.id.facilityDescription)).check(ViewAssertions.matches(ViewMatchers.withText(containsString("Famous fast food restaurant that serves burgers, fries, soft drinks, and a variety of other fast food options."))));
        Espresso.onView(ViewMatchers.withId(R.id.facilityAddress)).check(ViewAssertions.matches(ViewMatchers.withText(("5728 University Blvd, Vancouver, BC V6T 1K6, Canada"))));
    }
}
