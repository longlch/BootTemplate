package com.guru.util;

import java.util.List;

import com.guru.model.BusRoute;
import com.guru.model.BusStation;

public interface IJsonUtil {
	List<BusStation> getBusStations();
	List<BusRoute> getBusRoute();
}
