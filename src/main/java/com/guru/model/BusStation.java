package com.guru.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="bus_station")
public class BusStation {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String name;
	private double lat; 
	private double lng;
	
    @OneToMany(mappedBy = "busStation", cascade = CascadeType.ALL)
	private List<WalkingPath> walkingPaths;
    
    @OneToMany(mappedBy = "busStation", cascade = CascadeType.ALL)
    private List<BusPath> busPaths;
	
	public BusStation() {
		// TODO Auto-generated constructor stub
	}

	public BusStation(String name, double lat, double lng, List<WalkingPath> walkingPaths, List<BusPath> busPaths) {
		super();
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.walkingPaths = walkingPaths;
		this.busPaths = busPaths;
	}

	public BusStation(String name, double lat, double lng) {
		super();
		this.name = name;
		this.lat = lat;
		this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public List<WalkingPath> getWalkingPaths() {
		return walkingPaths;
	}

	public void setWalkingPaths(List<WalkingPath> walkingPaths) {
		this.walkingPaths = walkingPaths;
	}

	public List<BusPath> getBusPaths() {
		return busPaths;
	}

	public void setBusPaths(List<BusPath> busPaths) {
		this.busPaths = busPaths;
	}

	public int getId() {
		return id;
	}
	
	
	
	
}
