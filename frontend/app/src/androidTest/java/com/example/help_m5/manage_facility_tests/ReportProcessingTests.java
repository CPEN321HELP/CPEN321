package com.example.help_m5.manage_facility_tests;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.help_m5.R;
import com.example.help_m5.ToastMatcher;
import com.example.help_m5.database.DatabaseConnection;
import com.example.help_m5.menu.ReportFragment;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportProcessingTests {
    FragmentScenario<ReportFragment> mfragment;
    DatabaseConnection db;

    @Before
    public void setUp(){
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable");
        db = new DatabaseConnection();
        mfragment = FragmentScenario.launchInContainer(ReportFragment.class, null, R.style.MyMaterialTheme, Lifecycle.State.STARTED);
    }

    @Test
    public void A_refresh() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Thread.sleep(2000);
        String serverResponse = readFromJson();
        Assert.assertNotNull(serverResponse);
    }

    @Test
    public void C_agree() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Thread.sleep(500);
        Espresso.onView(ViewMatchers.withId(R.id.reportApprove_y1)).perform(ViewActions.click());
        Thread.sleep(1000);
        Espresso.onView(ViewMatchers.withText("Sending result to server!")).inRoot(new ToastMatcher()).check(ViewAssertions.matches(ViewMatchers.withText("Sending result to server!")));
        Thread.sleep(1000);
        Espresso.onView(ViewMatchers.withText("Server has received your decision!")).inRoot(new ToastMatcher()).check(ViewAssertions.matches(ViewMatchers.withText("Server has received your decision!")));
    }

    @Test
    public void B_reject() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Thread.sleep(500);
        Espresso.onView(ViewMatchers.withId(R.id.reportNot_y1)).perform(ViewActions.click());
        Thread.sleep(1000);
        Espresso.onView(ViewMatchers.withText("Sending result to server!")).inRoot(new ToastMatcher()).check(ViewAssertions.matches(ViewMatchers.withText("Sending result to server!")));
        Thread.sleep(1000);
        Espresso.onView(ViewMatchers.withText("Server has received your decision!")).inRoot(new ToastMatcher()).check(ViewAssertions.matches(ViewMatchers.withText("Server has received your decision!")));
    }


    //used by tests, not application
    public String readFromJson() {
        try {
            File file = new File("/data/data/com.example.help_m5/files/", "reports.json");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            // This responce will have Json Format String
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
