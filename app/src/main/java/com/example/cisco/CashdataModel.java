package com.example.cisco;

public class CashdataModel {
    String category;
    String goal;
    String booking;
    String backlog;

    public CashdataModel(String category, String goal, String booking,String backlog) {
        this.category = category;
        this.goal = goal;
        this.booking = booking;
        this.backlog = backlog;
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
    public String getBacklog() {
        return backlog;
    }
}
