package com.example.se1731_houserentailproject_group1.Model;

public class Unit {
    private int id;
    private int propertyId;
    private String unitNumber;
    private int roomCount;
    private int bathroomCount;
    private int squareFootage;
    private String floorPlan;


    public Unit(int id, int propertyId, String unitNumber, int roomCount, int bathroomCount, int squareFootage, String floorPlan) {
        this.id = id;
        this.propertyId = propertyId;
        this.unitNumber = unitNumber;
        this.roomCount = roomCount;
        this.bathroomCount = bathroomCount;
        this.squareFootage = squareFootage;
        this.floorPlan = floorPlan;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public int getBathroomCount() {
        return bathroomCount;
    }

    public void setBathroomCount(int bathroomCount) {
        this.bathroomCount = bathroomCount;
    }

    public int getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
    }

    public String getFloorPlan() {
        return floorPlan;
    }

    public void setFloorPlan(String floorPlan) {
        this.floorPlan = floorPlan;
    }
}
