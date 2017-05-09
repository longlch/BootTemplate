package com.guru.model;

public class Bus {
	public String busName;
	public String busRoute;
	public boolean turn;
	public int distance;
	public String time;
	public int speed;
	
	public Bus() {
		// TODO Auto-generated constructor stub
	}
	
	public Bus(String busName, String busRoute, boolean turn, int distance, int speed, String time) {
		super();
		this.busName = busName;
		this.busRoute = busRoute;
		this.turn = turn;
		this.distance = distance;
		this.time = time;
		this.speed = speed;
	}

	public String getBusName() {
		return busName;
	}
	public void setBusName(String busName) {
		this.busName = busName;
	}
	public String getBusRoute() {
		return busRoute;
	}
	public void setBusRoute(String busRoute) {
		this.busRoute = busRoute;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public boolean isTurn() {
		return turn;
	}
	public void setTurn(boolean turn) {
		this.turn = turn;
	}
	
	
	
	
	
}
