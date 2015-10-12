package com.sick.tracks.pojo;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by i.shabaev on 12.10.2015.
 */
public class LocationInf {

    private Location location;
    private ArrayList<LatLng> latLngs;
    private double distance;

    public LocationInf(Location location, ArrayList<LatLng> latLngs, double distance){
        this.location = location;
        this.latLngs = latLngs;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(ArrayList<LatLng> latLngs) {
        this.latLngs = latLngs;
    }
}
