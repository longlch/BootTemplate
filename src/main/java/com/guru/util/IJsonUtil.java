package com.guru.util;

import java.util.List;

import com.guru.model.BusRoute;
import com.guru.model.BusStation;
import com.guru.model.BusStationDistance;
import com.guru.model.RouteElement;
import com.guru.model.WalkingPath;

public interface IJsonUtil {
	List<BusStation> getBusStations();
	List<BusRoute> getBusRoutes();
	List<WalkingPath> getWalkingPaths();
	List<BusStationDistance> getBusStationDistances();
	
	List<RouteElement> graphRouteElement();
	List<BusStation> getBusStationFromBusRoute(int route,String trend);
	BusRoute getBusRouteDetail(int route,String trend);
}
