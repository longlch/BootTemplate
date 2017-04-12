package com.guru.util;

public class Vertex {
	final private int id;
    final private String name;
    private double lat, lng;


    public Vertex(int id, String name, double lat, double lng) {
        super();
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
    public Vertex(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLng() {
        return lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{'lat':"+lat+",'lng':"+lng+"}";
}
}
