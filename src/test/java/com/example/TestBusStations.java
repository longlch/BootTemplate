package com.example;


import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.Gson;
import com.guru.model.BusStop;

public class TestBusStations {
	public static void main(String[] args) {
//		convertJsArrToArr();
		convertJsArrToListPoJo();
	}
	

	public static BusStop[] convertJsArrToArr(){
		JSONParser parse= new JSONParser();
		Resource resource= new ClassPathResource("static/bus_route/route7_go.js");
		ObjectMapper mapper = new ObjectMapper();
		BusStop[] busStops;
		try {
			File file= resource.getFile();
			Object obj = parse.parse(new FileReader(file.toString()));
			String jsonString=obj.toString();
			Gson gson=new Gson();
			busStops=gson.fromJson(jsonString, BusStop[].class);
			for (BusStop busStop : busStops) {
				System.out.println(busStop.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void convertJsArrToListPoJo(){
		Resource resource= new ClassPathResource("static/bus_route/route7_back.js");
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			File file= resource.getFile();
//			Object obj = parse.parse(new FileReader(file.toString()));
//			String jsonString=obj.toString();
		
			List<BusStop> busStops=mapper.readValue(new File(file.toString()), mapper.getTypeFactory().constructCollectionType(List.class, BusStop.class));
			for (BusStop busStop : busStops) {
				System.out.println(busStop.getName());
				System.out.println(busStop.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getAllBusStationsInDetail(){
		
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
			
//			find busList have id is 5 and turn is true
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
