package com.uminas.protectora.model;

public class RescueReport {

    private String phone;
    private String description;
    private String location;

    public RescueReport() {
    }

    public RescueReport(String phone, String description, String location) {
        this.phone = phone;
        this.description = description;
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}