package com.guru.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="walking_path")
public class WalkingPath {
	
	@EmbeddedId
	private WalkingPathId walkingPathId;
	private int distance;
	
	@ManyToOne
	@JoinColumn(name="bus_station_id")
	private BusStation busStation;
	
	public WalkingPath() {
		// TODO Auto-generated constructor stub
	}
	public WalkingPath(WalkingPathId walkingPathId, int distance) {
		super();
		this.walkingPathId = walkingPathId;
		this.distance = distance;
	}
	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	public WalkingPathId getWalkingPathId() {
		return walkingPathId;
	}
	public void setWalkingPathId(WalkingPathId walkingPathId) {
		this.walkingPathId = walkingPathId;
	}
	public BusStation getBusStation() {
		return busStation;
	}
	public void setBusStation(BusStation busStation) {
		this.busStation = busStation;
	}
	
}
