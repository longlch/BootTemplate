package com.guru.model;

import java.util.ArrayList;
public class BusStation {

	private Integer id;
    private String name;
    private Double lat;
    private Double lng;
    private ArrayList<BusRoute> busList;

    public BusStation(Integer id, String name, Double lat, Double lng, ArrayList<BusRoute> busList) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.busList = busList;
    }

    @Override
    public String toString() {
        return "\n{'id':" + id + ", 'name':'" + name + "', 'lat':" + lat + ", 'lng':" + lng + ", 'busList':" + busList
                + "}";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public ArrayList<BusRoute> getBusList() {
        return busList;
    }

    public void setBusList(ArrayList<BusRoute> busList) {
        this.busList = busList;
    }

    public void addBusRoute(BusRoute busRoute) {
        this.busList.add(busRoute);
}	
	
	
	
}
