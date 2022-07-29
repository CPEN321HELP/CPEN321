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

import com.example.help_m5.R;
import com.example.help_m5.ToastMatcher;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.example.help_m5.ui.report.ReportFragment;

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
    public void setUp() throws Exception {
        db = new DatabaseConnection();
        mfragment = FragmentScenario.launchInContainer(ReportFragment.class, null, R.style.MyMaterialTheme, Lifecycle.State.STARTED);
    }

    @Test
    public void refresh(){
        createJsonForTesting();
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.report_title_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText("Party at tom's house")));
        Espresso.onView(ViewMatchers.withId(R.id.facility_id_org_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText("14")));
        Espresso.onView(ViewMatchers.withId(R.id.reporter_id_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText("reporter1")));
        Espresso.onView(ViewMatchers.withId(R.id.reported_id_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText("reported_user1")));
        Espresso.onView(ViewMatchers.withId(R.id.reported_reason_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText("test1reson")));

        try {
            Thread.sleep(2000);
        }catch (Exception e){

        }

        Espresso.onView(ViewMatchers.withId(R.id.report_title_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText("Party at tom's house")));
        Espresso.onView(ViewMatchers.withId(R.id.facility_id_org_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText("14")));
        Espresso.onView(ViewMatchers.withId(R.id.reporter_id_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText("reporter2")));
        Espresso.onView(ViewMatchers.withId(R.id.reported_id_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText("reported_user2")));
        Espresso.onView(ViewMatchers.withId(R.id.reported_reason_cont_y1)).check(ViewAssertions.matches(ViewMatchers.withText("test2reson")));
    }

    @Test
    public void agree() throws InterruptedException {
        createJsonForTesting();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());

        String result = readFromJson();
        Assert.assertNotNull(result);

        Espresso.onView(ViewMatchers.withId(R.id.s1)).perform(ViewActions.swipeUp());
        Espresso.onView(ViewMatchers.withId(R.id.reportApprove_y1)).perform(ViewActions.swipeUp(), ViewActions.click());
        String jsonToSend = readFromJson();
        Assert.assertNotNull(jsonToSend);
        onView(withText("Sending result to server!")).inRoot(new ToastMatcher())
                .check(matches(withText("Sending result to server!")));
        try{
            onView(withText("Server has received your decision!")).inRoot(new ToastMatcher())
                    .check(matches(withText("Server has received your decision!")));
        }catch (Exception e){
            //server is not running
        }

        try {
            JSONObject jsonToSendJ = new JSONObject(result);
            Assert.assertEquals("reporter1", jsonToSendJ.get("upUserId"));
            Assert.assertEquals("reported_user1", jsonToSendJ.get("reported_user"));
            Assert.assertEquals("test1", jsonToSendJ.get("report_id"));
            Assert.assertEquals("1", jsonToSendJ.get("approve"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void reject(){
        createJsonForTesting();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fabRefresh)).perform(ViewActions.click());

        String result = readFromJson();
        Assert.assertNotNull(result);

        Espresso.onView(ViewMatchers.withId(R.id.s2)).perform(ViewActions.swipeUp());
        Espresso.onView(ViewMatchers.withId(R.id.reportNot_y2)).perform(ViewActions.swipeUp(), ViewActions.click());
        String jsonToSend = readFromJson();
        Assert.assertNotNull(jsonToSend);
        onView(withText("Sending result to server!")).inRoot(new ToastMatcher())
                .check(matches(withText("Sending result to server!")));
        try{
            onView(withText("Server has received your decision!")).inRoot(new ToastMatcher())
                    .check(matches(withText("Server has received your decision!")));
        }catch (Exception e){
            //server is not running
        }
        try {
            JSONObject jsonToSendJ = new JSONObject(result);
            Assert.assertEquals("reporter2", jsonToSendJ.get("upUserId"));
            Assert.assertEquals("reported_user2", jsonToSendJ.get("reported_user"));
            Assert.assertEquals("test2", jsonToSendJ.get("report_id"));
            Assert.assertEquals("0", jsonToSendJ.get("approve"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //used by tests, not application
    public String readFromJson() {
        try {
            File file = new File("/data/data/com.example.help_m5/files/", "testReportFragment.json");
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

    private boolean createJsonForTesting(){
        try{
            Log.d("TESTINGG", "start");
            JSONObject report1 = new JSONObject("{\"_id\":\"test1\",\"facility_id\":14,\"facility_type\":0,\"reason\":\"test1reson\",\"reporter\":\"reporter1\",\"report_type\":\"6\",\"reported_user\":\"reported_user1\",\"title\":\"Party at tom's house\",\"reportUserStatus\":\"1\"}");
            JSONObject report2 = new JSONObject("{\"_id\":\"test2\",\"facility_id\":14,\"facility_type\":0,\"reason\":\"test2reson\",\"reporter\":\"reporter2\",\"report_type\":\"6\",\"reported_user\":\"reported_user2\",\"title\":\"Party at tom's house\",\"reportUserStatus\":\"1\"}");
            JSONArray jo = new JSONArray(new Object[]{report1, report2});
            JSONObject wrapper = new JSONObject();
            wrapper.put("report_content", jo);
            wrapper.put("length", 2);
            db.writeToJsonForTesting("/data/data/com.example.help_m5/files/", wrapper, "testReportFragment.json");
            Log.d("TESTINGG", wrapper.toString());
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

}
