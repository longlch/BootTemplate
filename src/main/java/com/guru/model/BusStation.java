package com.guru.model;

import java.util.ArrayList;
import java.util.List;
public class BusStation {

	private Integer id;
    private String name;
    private Double lat;
    private Double lng;
    private List<BusRoute> busList;
    
    public BusStation() {
		// TODO Auto-generated constructor stub
	}
    
	public BusStation(Integer id, String name, Double lat, Double lng, List<BusRoute> busList) {
		super();
		this.id = id;
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.busList = busList;
	}
	
	public Integer getId() {
		return id;
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
	public List<BusRoute> getBusList() {
		return busList;
	}
	public void setBusList(List<BusRoute> busList) {
		this.busList = busList;
	}

	@Override
	public String toString() {
        return "\n{'id':" + id + ", 'name':'" + name + "', 'lat':" + lat + ", 'lng':" + lng + ", 'busList':" + busList.toString()
                + "}";
    }
	
	

	
	
}
