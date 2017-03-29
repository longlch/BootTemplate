package com.guru.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.guru.model.BusStation;
import com.guru.model.BusStop;
public interface IMapService {
	 List<BusStation> loadDummyMarker();
	 
//	 this method will return a json object for google map direction 
	 String findBusRoute(String route,String trend);
	 
//	 get data and show it into lef side bar
	 List<BusStop> getBusStops(String route,String trend);
	 
	 String getRouteName(String id);
	 
}
