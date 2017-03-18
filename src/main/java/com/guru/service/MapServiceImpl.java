package com.guru.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.guru.model.BusStation;
import com.guru.model.BusStop;
@Service
public class MapServiceImpl implements IMapService{

	@Override
	public List<BusStation> loadDummyMarker() {
		JSONParser parser = new JSONParser();
		ArrayList<BusStation> busStations= new ArrayList<>();
		try {
			Resource resource= new ClassPathResource("static/bus_route/route1.js");
			resource.toString();
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				BusStation busStation= new BusStation(
						jsonArray.getJSONObject(i).getString("name"),
						jsonArray.getJSONObject(i).getDouble("lat"),
						jsonArray.getJSONObject(i).getDouble("lng"));
				busStations.add(busStation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return busStations;
	}

	@Override
	public String findBusRoute(String route) {
		JSONParser parser = new JSONParser();
		Resource resource= new ClassPathResource("static/bus_route/"+route+"_go.js");
		String jsonString="";
		try {
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			jsonString = obj.toString();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}
	

}
