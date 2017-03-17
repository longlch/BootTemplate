package com.guru.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.guru.model.BusStation;
import com.guru.model.BusStop;
@Service
public interface IMapService {
	 List<BusStation> loadDummyMarker();
	 
	 String findBusRoute(String route);
}
