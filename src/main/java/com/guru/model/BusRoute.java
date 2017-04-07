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
@Table(name="bus_route")
public class BusRoute {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private boolean turn;
	
	private String name;
	
	private int routeId;
	
	@OneToMany(mappedBy = "busRoute", cascade = CascadeType.ALL)
	private List<BusPath> busPaths;
	
	public BusRoute() {
		// TODO Auto-generated constructor stub
	}
	
	public BusRoute(boolean turn, String name, int routeId, List<BusPath> busPaths) {
		super();
		this.turn = turn;
		this.name = name;
		this.routeId = routeId;
		this.busPaths = busPaths;
	}
	

	public BusRoute(boolean turn, String name, int routeId) {
		super();
		this.turn = turn;
		this.name = name;
		this.routeId = routeId;
	}

	public boolean isTurn() {
		return turn;
	}
	public void setTurn(boolean turn) {
		this.turn = turn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<BusPath> getBusPaths() {
		return busPaths;
	}
	public void setBusPaths(List<BusPath> busPaths) {
		this.busPaths = busPaths;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	
	
}
