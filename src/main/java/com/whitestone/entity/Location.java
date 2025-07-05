package com.whitestone.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Location {
    private double lat;
    private double lon;
    private String name; // Add a name field for the location name

    // Getters and setters for lat, lon, and name
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Optionally, a constructor to initialize latitude, longitude, and name
    public Location(double lat, double lon, String name) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }
}
