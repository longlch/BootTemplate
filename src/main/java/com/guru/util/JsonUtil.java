package com.guru.util;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.guru.model.BusRoute;
import com.guru.model.BusStation;

public class JsonUtil implements IJsonUtil {

	@Override
	public List<BusStation> getBusStations() {
		JSONParser parser = new JSONParser();
		List<BusStation> busStations = new ArrayList<>();
		List<BusRoute> busRoutes= new ArrayList<>(); 
		try {
			Resource resource = new ClassPathResource("static/bus_route/busRoute.json");
			resource.toString();
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<BusRoute> getBusRoute() {
		JSONParser parser = new JSONParser();
		List<BusRoute> busRoutes = new ArrayList<>();
		try {
			Resource resource = new ClassPathResource("static/bus_route/busRoute.json");
			resource.toString();
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			
			for (int i = 0; i < jsonArray.length(); i++) {
				busRoutes.add(new BusRoute(jsonArray.getJSONObject(i).getBoolean("turn"),
											jsonArray.getJSONObject(i).getInt("id"), 
											jsonArray.getJSONObject(i).getString("name")));
			}
			return busRoutes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		List<BusStation> busStations = new ArrayList<>();
		List<BusRoute> busRoutes= new ArrayList<>(); 
		int busRouteLength=0;
		try {
			Resource resource = new ClassPathResource("static/bus_route/busStation.json");
			resource.toString();
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			
			for(int i = 0; i < jsonArray.length(); i++){
				busRouteLength=jsonArray.getJSONObject(i).getJSONArray("busList").length();
				System.out.println(busRouteLength);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
