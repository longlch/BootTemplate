package com.guru.model;

public class BusStop {
	private String name;
	private String latLng;
	
	public BusStop() {
		// TODO Auto-generated constructor stub
	}

	public BusStop(String name, String latLng) {
		super();
		this.name = name;
		this.latLng = latLng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLatLng() {
		return latLng;
	}

	public void setLatLng(String latLng) {
		this.latLng = latLng;
	}
	
	
}
