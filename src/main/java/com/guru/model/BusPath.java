package com.guru.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="bus_path")
public class BusPath {

	@EmbeddedId
	private BusPathId busPathId;

	@ManyToOne
	@JoinColumn(name="bus_route_id")
	private BusRoute busRoute;

	@ManyToOne
	@JoinColumn(name="bus_station_id")
	private BusStation busStation;

	public BusPath() {
		// TODO Auto-generated constructor stub
	}
	
	public BusPath(BusPathId busPathId, BusRoute busRoute, BusStation busStation) {
		super();
		this.busPathId = busPathId;
		this.busRoute = busRoute;
		this.busStation = busStation;
	}

	public BusPathId getBusPathId() {
		return busPathId;
	}

	public void setBusPathId(BusPathId busPathId) {
		this.busPathId = busPathId;
	}

	public BusRoute getBusRoute() {
		return busRoute;
	}

	public void setBusRoute(BusRoute busRoute) {
		this.busRoute = busRoute;
	}
	
	
}
