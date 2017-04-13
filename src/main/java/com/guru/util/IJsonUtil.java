package com.guru.util;

import java.util.List;

import com.guru.model.BusRoute;
import com.guru.model.BusStation;
import com.guru.model.BusStationDistance;
import com.guru.model.WalkingPath;

public interface IJsonUtil {
	List<BusStation> getBusStations();
	List<BusRoute> getBusRoutes();
	List<WalkingPath> getWalkingPaths();
	List<BusStationDistance> getBusStationDistances();
}
