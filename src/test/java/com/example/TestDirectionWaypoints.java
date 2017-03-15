package com.example;

import java.io.FileReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.JsonParser;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.guru.model.BusStation;

public class TestDirectionWaypoints {
	public static void main(String[] args) throws Exception {
//		parse json object(Array) to class Java
		/*JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("/home/larry/Desktop/SeniorProject/map/DemoLocation.js"));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			ArrayList<BusStation> busStations= new ArrayList<>();
			for (int i = 0; i < jsonArray.length(); i++) {
				BusStation busStation= new BusStation(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getDouble("lng"), jsonArray.getJSONObject(i).getDouble("lng"));
				busStations.add(busStation);
			}
			for (BusStation busStation : busStations) {
				System.out.println(busStation.getLatitude());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCv7lDkj6Yd3cMbujJcHTKIo_AzLEga-7c");
		DirectionsResult result = DirectionsApi.newRequest(context)
				.origin("108.22085169497,16.084605795666")
				.destination("108.222735,16.0814")
				.waypoints("108.158338,16.084032")
				.await();
//		System.out.println(result.routes[0].legs[0].distance);
//		System.out.println(result.routes[0].bounds.northeast.lat);

		
		
	}
}
