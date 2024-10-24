package com.example.se1731_houserentailproject_group1.Model;

public class PropertyImage {
    private int id;
    private int propertyId;
    private String imageUrl;
    private boolean isPrimary;


    public PropertyImage(int id, int propertyId, String imageUrl, boolean isPrimary) {
        this.id = id;
        this.propertyId = propertyId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
