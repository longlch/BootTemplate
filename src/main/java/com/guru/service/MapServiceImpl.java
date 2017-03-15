package com.guru.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonArray;
import com.guru.model.BusStation;

public class MapServiceImpl implements IMapService{

	@Override
	public List<BusStation> loadDummyMarker() {
		JSONParser parser = new JSONParser();
		ArrayList<BusStation> busStations= new ArrayList<>();
		try {
			Object obj = parser.parse(new FileReader("/home/larry/Desktop/SeniorProject/map/DemoLocation.js"));
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

}
