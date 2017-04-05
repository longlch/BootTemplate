package com.guru.service;

import java.util.List;

import com.guru.model.BusStop;
public interface IMapService {
	 
//	 this method will return a json object for google map direction 
	 String findBusRoute(String route,String trend);
	 
//	 get data and show it into lef side bar
	 List<BusStop> getBusStops(String route,String trend);
	 
	 String getRouteName(String id);
	 
}
