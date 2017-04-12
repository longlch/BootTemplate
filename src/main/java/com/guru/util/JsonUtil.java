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
		BusStation busStation;
		JSONArray jsonRouteArray;
		int busRouteLength=0;
		try {
			Resource resource = new ClassPathResource("static/bus_route/busStation.json");
			resource.toString();
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			
			for(int i = 0; i < jsonArray.length(); i++){
				jsonRouteArray=jsonArray.getJSONObject(i).getJSONArray("busList");
				busRouteLength=jsonRouteArray.length();
//				when put busRoute.clear() here then nothing happen
				busRoutes.clear();
				for(int j=0;j<busRouteLength;j++){
					BusRoute busRoute=new BusRoute(jsonRouteArray.getJSONObject(j).getBoolean("turn"),
							jsonRouteArray.getJSONObject(j).getInt("id"),
							jsonRouteArray.getJSONObject(j).getString("name"));
					busRoutes.add(busRoute);
				}
				busStations.add(new BusStation(jsonArray.getJSONObject(i).getInt("id"),
						jsonArray.getJSONObject(i).getString("name"), 
						jsonArray.getJSONObject(i).getDouble("lat"),
						jsonArray.getJSONObject(i).getDouble("lng"),busRoutes));
				System.out.println("id "+i+" "+busRoutes);
				
				/*Why busRoute clear here will empty busRoutes[i] in BusStations[i]
				busRoutes.clear();*/
			}
			return busStations;
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
		BusStation busStation;
		JSONArray jsonRouteArray;
		int busRouteLength=0;
		try {
			Resource resource = new ClassPathResource("static/bus_route/busStation.json");
			resource.toString();
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			
			for(int i = 0; i < jsonArray.length(); i++){
				jsonRouteArray=jsonArray.getJSONObject(i).getJSONArray("busList");
				busRouteLength=jsonRouteArray.length();
				
				busRoutes.clear();
				for(int j=0;j<busRouteLength;j++){
					BusRoute busRoute=new BusRoute(jsonRouteArray.getJSONObject(j).getBoolean("turn"),
							jsonRouteArray.getJSONObject(j).getInt("id"),
							jsonRouteArray.getJSONObject(j).getString("name"));
					busRoutes.add(busRoute);
				}
				
				busStations.add(new BusStation(jsonArray.getJSONObject(i).getInt("id"),
						jsonArray.getJSONObject(i).getString("name"), 
						jsonArray.getJSONObject(i).getDouble("lat"),
						jsonArray.getJSONObject(i).getDouble("lng"),busRoutes));
				System.out.println("id "+i+" "+busRoutes);
				
				/*Why busRoute clear here will empty busRoutes[i] in BusStations[i]
				busRoutes.clear();*/
			}
			for (BusStation busStation1 : busStations) {
				System.out.println("hihi"+busStation1.getBusList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
