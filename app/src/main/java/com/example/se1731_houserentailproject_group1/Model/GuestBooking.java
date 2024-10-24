package com.example.se1731_houserentailproject_group1.Model;

public class GuestBooking {
    private int id;
    private int unitId;
    private int guestId;
    private String startDate;
    private String endDate;
    private String status;


    public GuestBooking(int id, int unitId, int guestId, String startDate, String endDate, String status) {
        this.id = id;
        this.unitId = unitId;
        this.guestId = guestId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
