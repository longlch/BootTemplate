package com.guru.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BusPathId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="station_from_id")
	private int stationFromId;
	
	@Column(name="station_to_id")
	private int stationToId;
	
	public BusPathId() {
		// TODO Auto-generated constructor stub
	}

	public BusPathId(int stationFromId, int stationToId) {
		super();
		this.stationFromId = stationFromId;
		this.stationToId = stationToId;
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
