package com.example.help_m5.manage_facility_tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.help_m5.R;
import com.example.help_m5.ToastMatcher;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.example.help_m5.ui.report.ReportFragment;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    public void refresh(){
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        String serverResponse = readFromJson();
        Assert.assertNotNull(serverResponse);
        try {
            JSONObject jsonFormat = new JSONObject(serverResponse);
            JSONArray ja = jsonFormat.getJSONArray("report_content");
            JSONObject report1 = ja.getJSONObject(0);
            JSONObject report2 = ja.getJSONObject(1);
            Espresso.onView(ViewMatchers.withId(R.id.report_title_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText(report1.getString("title"))));
            Espresso.onView(ViewMatchers.withId(R.id.report_title_cont_y2)).check(ViewAssertions.matches(ViewMatchers.withText(report2.getString("title"))));
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void agree() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        String serverResponse = readFromJson();
        Assert.assertNotNull(serverResponse);
        try {
            JSONObject jsonFormat = new JSONObject(serverResponse);
            JSONArray ja = jsonFormat.getJSONArray("report_content");
            JSONObject report1 = ja.getJSONObject(0);
            JSONObject report2 = ja.getJSONObject(1);
            Log.d("TESTT", report1.toString());
            Log.d("TESTT", report2.toString());

            Thread.sleep(500);
            Espresso.onView(ViewMatchers.withId(R.id.report_title_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText(report1.getString("title"))));
            Espresso.onView(ViewMatchers.withId(R.id.report_title_cont_y2)).check(ViewAssertions.matches(ViewMatchers.withText(report2.getString("title"))));
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Espresso.onView(ViewMatchers.withId(R.id.s1)).perform(ViewActions.swipeUp());
        Espresso.onView(ViewMatchers.withId(R.id.reportApprove_y1)).perform(ViewActions.swipeUp(), ViewActions.click());
        Thread.sleep(500);
        onView(withText("Sending result to server!")).inRoot(new ToastMatcher()).check(matches(withText("Sending result to server!")));
        Thread.sleep(500);
        onView(withText("Server has received your decision!")).inRoot(new ToastMatcher()).check(matches(withText("Server has received your decision!")));
    }

    @Test
    public void reject() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.c1)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())));
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        String serverResponse = readFromJson();
        Assert.assertNotNull(serverResponse);
        try {
            JSONObject jsonFormat = new JSONObject(serverResponse);
            JSONArray ja = jsonFormat.getJSONArray("report_content");
            JSONObject report1 = ja.getJSONObject(0);
            JSONObject report2 = ja.getJSONObject(1);
            Espresso.onView(ViewMatchers.withId(R.id.report_title_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText(report1.getString("title"))));
            Espresso.onView(ViewMatchers.withId(R.id.report_title_cont_y2)).check(ViewAssertions.matches(ViewMatchers.withText(report2.getString("title"))));
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Espresso.onView(ViewMatchers.withId(R.id.s1)).perform(ViewActions.swipeUp());
        Espresso.onView(ViewMatchers.withId(R.id.reportNot_y1)).perform(ViewActions.swipeUp(), ViewActions.click());
        Thread.sleep(500);
        onView(withText("Sending result to server!")).inRoot(new ToastMatcher()).check(matches(withText("Sending result to server!")));
        Thread.sleep(500);
        onView(withText("Server has received your decision!")).inRoot(new ToastMatcher()).check(matches(withText("Server has received your decision!")));
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
