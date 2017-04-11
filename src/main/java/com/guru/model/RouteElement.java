package com.guru.model;

public class RouteElement {
    private int stationFromId;
    private int stationToId;
    private int distanceWalking;
    private int distanceOnBus;
    private BusRoute busRoute;

    public RouteElement(){}

    public RouteElement(int stationFromId, int stationToId, int distanceWalking, int distanceOnBus, BusRoute busRoute) {
        this.stationFromId = stationFromId;
        this.stationToId = stationToId;
        this.distanceWalking = distanceWalking;
        this.distanceOnBus = distanceOnBus;
        this.busRoute = busRoute;
    }
    @Override
    public String toString() {
        return "\n{'stationFromId':" + stationFromId + ", 'stationToId':" + stationToId
                + ", 'distanceWalking':" + distanceWalking + ", 'distanceOnBus':" + distanceOnBus + ", 'busRoute':"
                + busRoute + "}";
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
    public int getDistanceWalking() {
        return distanceWalking;
    }
    public void setDistanceWalking(int distanceWalking) {
        this.distanceWalking = distanceWalking;
    }
    public int getDistanceOnBus() {
        return distanceOnBus;
    }
    public void setDistanceOnBus(int distanceOnBus) {
        this.distanceOnBus = distanceOnBus;
    }
    public BusRoute getBusRoute() {
        return busRoute;
    }
    public void setBusRoute(BusRoute busRoute) {
        this.busRoute = busRoute;
}
}
