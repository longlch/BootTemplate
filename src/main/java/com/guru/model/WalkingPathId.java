package com.guru.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WalkingPathId implements Serializable{
	
	@Column(name="station_from_id")
	private int stationFromId;
	
	@Column(name="station_to_id")
	private int stationToId;
	
	public WalkingPathId() {
		// TODO Auto-generated constructor stub
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
	
}
