package com.guru.model;

public class BusStop {
	private int id;
	private String name;
	private String latLng;
	
	public BusStop() {
		// TODO Auto-generated constructor stub
	}

	
	public BusStop(int id, String name, String latLng) {
		super();
		this.id = id;
		this.name = name;
		this.latLng = latLng;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
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
