package com.sick.tracks.pojo;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by i.shabaev on 24.09.2015.
 */
public class Position {

    private LatLng latLng;

    private ArrayList<LatLng> points;

    public Position(LatLng latLng, ArrayList<LatLng> points){
        this.latLng = latLng;
        this.points = points;
    }


    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setPoints(ArrayList<LatLng> points) {
        this.points = points;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public ArrayList<LatLng> getPoints() {
        return points;
    }
}
