package com.guru.model;

public class BusPath {

	private int stationFromId;
	private int stationToId;
	private int distance;
	
	public BusPath() {
		// TODO Auto-generated constructor stub
	}
	

	public BusPath(int stationFromId, int stationToId, int distance) {
		super();
		this.stationFromId = stationFromId;
		this.stationToId = stationToId;
		this.distance = distance;
	}


	public int getStationFromId() {
		return stationFromId;
	}

	public void setStationFromId(int stationFromId) {
		this.stationFromId = stationFromId;
	}

	public int getStationToId() {
		return stationToId;
	}

	public void setStationToId(int stationToId) {
		this.stationToId = stationToId;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	
}
