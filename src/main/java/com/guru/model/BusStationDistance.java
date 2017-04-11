package com.guru.model;

public class BusStationDistance {
	    private Integer stationFromId;
	    private Integer stationToId;
	    private Integer distance;
	    private BusRoute busRoute;

	    public BusStationDistance(Integer stationFromId, Integer stationToId, Integer distance, BusRoute busRoute) {
	        this.stationFromId = stationFromId;
	        this.stationToId = stationToId;
	        this.distance = distance;
	        this.busRoute = busRoute;
	    }

	    @Override
	    public String toString() {
	        return "BusStationDistance [stationFromId=" + stationFromId + ", stationToId=" + stationToId + ", distance="
	                + distance + ", busRoute=" + busRoute + "]";
	    }

	    public Integer getStationFromId() {
	        return stationFromId;
	    }

	    public void setStationFromId(Integer stationFromId) {
	        this.stationFromId = stationFromId;
	    }

	    public Integer getStationToId() {
	        return stationToId;
	    }

	    public void setStationToId(Integer stationToId) {
	        this.stationToId = stationToId;
	    }

	    public Integer getDistance() {
	        return distance;
	    }

	    public void setDistance(Integer distance) {
	        this.distance = distance;
	    }

	    public BusRoute getBusRoute() {
	        return busRoute;
	    }

	    public void setBusRoute(BusRoute busRoute) {
	        this.busRoute = busRoute;
	}
}
