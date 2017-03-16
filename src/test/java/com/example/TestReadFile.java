package com.example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.guru.model.BusStation;
import com.guru.model.BusStop;

public class TestReadFile {
	public static void main(String[] args) throws IOException {
		JSONParser parser = new JSONParser();
		try {
			Resource resource= new ClassPathResource("static/bus_route/route5_go.js");
			resource.toString();
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			ArrayList<BusStop> busStops= new ArrayList<>();
			
			for (int i = 0; i < jsonArray.length(); i++) {
				BusStop busStop= new BusStop(jsonArray.getJSONObject(i).getString("strName"),jsonArray.getJSONObject(i).getString("LatLng"));
				busStops.add(busStop);
			}
			for (BusStop busStop : busStops) {
				System.out.println(busStop.getLatLng());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
