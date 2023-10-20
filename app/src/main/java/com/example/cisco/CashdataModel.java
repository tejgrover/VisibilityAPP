package com.example.cisco;

public class CashdataModel {
    String category;
    String goal;
    String booking;
    String noncomm;
    String backlog;
    String revoriginal;
    String revmultiplied;
    String revattainment;


    public CashdataModel(String category, String goal, String booking,String noncomm,String backlog,String revoriginal,String revmultiplied, String revattainment) {
        this.category = category;
        this.goal = goal;
        this.booking = booking;
        this.noncomm = noncomm;
        this.backlog = backlog;
        this.revoriginal = revoriginal;
        this.revmultiplied = revmultiplied;
        this.revattainment = revattainment;
    }

    public String getCategory() {
        return category;
    }

    public String getGoal() {
        return goal;
    }

    public String getBooking() {
        return booking;
    }

    public String getNoncomm() {
        return noncomm;
    }

    public String getBacklog() {
        return backlog;
    }

    public String getRevoriginal() {
        return revoriginal;
    }

    public String getRevmultiplied() {
        return revmultiplied;
    }

    public String getRevattainment() {
        return revattainment;
    }
}
