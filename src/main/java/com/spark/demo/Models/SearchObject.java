package com.spark.demo.Models;

public class SearchObject {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public SearchObject(String name, String category, String availability) {
        this.name = name;
        this.category = category;
        this.availability = availability;
    }

    public SearchObject() {
    }
    
    private String name;
    private String category;
    private String availability;
    
}
