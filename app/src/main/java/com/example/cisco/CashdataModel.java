package com.example.cisco;

public class CashdataModel {
    String category;
    String goal;
    String booking;

    public CashdataModel(String category, String goal, String booking) {
        this.category = category;
        this.goal = goal;
        this.booking = booking;
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
}
