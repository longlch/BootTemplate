package com.example;

import java.io.FileReader;

import org.json.JSONArray;
import org.json.simple.parser.JSONParser;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;

public class TestDirectionWaypoints {
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("/home/larry/Desktop/SeniorProject/map/route.js"));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			
			/*for (int i = 0; i < jsonArray.length(); i++) {
				
				System.out.println(jsonArray.getJSONObject(i).get("lng"));
			}*/
			
			GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCv7lDkj6Yd3cMbujJcHTKIo_AzLEga-7c");
			DirectionsResult result = DirectionsApi.newRequest(context)
			        .origin("Boston,MA")
			        .destination("Concord,MA")
			        .waypoints("Charlestown,MA", "Lexington,MA")
			.await();
			System.out.println(result.routes[0].legs[0].distance);
//			System.out.println(result.routes[0].bounds.northeast.lat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
