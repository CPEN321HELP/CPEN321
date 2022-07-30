package com.example.help_m5.reviews;

import java.util.List;

public class ReviewItem {
    private String userName;
    private String userEmail;
    private String reportedUserEmail;
    private String userDate;
    private String userDescription;
    private double userRate;

    private List<Integer> voteCounts;
    private List<String> facilityInformation;

    public ReviewItem(String userName, String userEmail, String reportedUserEmail, String userDate, String userDescription, double userRate, List<Integer> voteCounts, List<String> facilityInformation) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.reportedUserEmail = reportedUserEmail;
        this.userDate = userDate;
        this.userDescription = userDescription;
        this.userRate = userRate;
        this.voteCounts = voteCounts;
        this.facilityInformation = facilityInformation;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getReportedUserEmail() {
        return reportedUserEmail;
    }

    public String getUserDate() {
        return userDate;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public double getUserRate() {
        return userRate;
    }

    public List<Integer> getVoteCounts() {
        return voteCounts;
    }

    public List<String> getFacilityInformation() {
        return facilityInformation;
    }
}
