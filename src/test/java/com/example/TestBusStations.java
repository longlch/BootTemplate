package com.example;


import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class TestBusStations {
	public static void main(String[] args) {
		getAllBusStationsByRoute();
	}
	public static String getAllBusStationsByRoute(){
		JSONParser parse= new JSONParser();
		Resource resource= new ClassPathResource("static/bus_route/busStation.json");
		try {
			File file= resource.getFile();
			Object obj = parse.parse(new FileReader(file.toString()));
			
			JSONArray jsonArray=(JSONArray) obj;
			JSONObject jsonObject0=(JSONObject) jsonArray.get(0);
			JSONArray jsArrBuses= (JSONArray) jsonObject0.get("busList");
			JSONObject jsObjInBusses= (JSONObject) jsArrBuses.get(0);
			System.out.println(jsObjInBusses);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
