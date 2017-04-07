package com.example;


import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guru.model.BusRoute;
import com.guru.model.BusStation;
import com.guru.repository.BusRouteRepository;
import com.guru.repository.BusStationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestJPARepository {
	
	/*@Autowired
	BusRouteRepository repositoryBusRoute;*/
	
	/*@Autowired
	BusStationRepository repositoryBusStation;*/
	
	@Test
	public void imporData(){

	}
		
	public List<BusRoute> saveBusRoute(String fileName ){
		List<BusRoute> busRoutes= new ArrayList<>();
		JSONParser parse= new JSONParser();
		Resource resource= new ClassPathResource("static/bus_route/"+fileName+".json");
		try {
			File file= resource.getFile();
			Object obj = parse.parse(new FileReader(file.toString()));
			String jsonString=obj.toString();
			
			JSONArray jsonArray= new JSONArray(jsonString);
			
			for (int i = 0; i < jsonArray.length(); i++) {
				BusRoute busRoute= new BusRoute(jsonArray.getJSONObject(i).getBoolean("turn"),
												jsonArray.getJSONObject(i).getString("name"),
												jsonArray.getJSONObject(i).getInt("id"));
				busRoutes.add(busRoute);
			}
			return busRoutes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<BusStation> saveBusSation(String fileName ){
		List<BusStation> busStations= new ArrayList<>();
		JSONParser parse= new JSONParser();
		Resource resource= new ClassPathResource("static/bus_route/"+fileName+".json");
		try {
			File file= resource.getFile();
			Object obj = parse.parse(new FileReader(file.toString()));
			String jsonString=obj.toString();
			
			JSONArray jsonArray= new JSONArray(jsonString);
			
			for (int i = 0; i < jsonArray.length(); i++) {
				BusStation busStation= new BusStation(jsonArray.getJSONObject(i).getString("name"),
														jsonArray.getJSONObject(i).getDouble("lat"),
														jsonArray.getJSONObject(i).getDouble("lng"));
				busStations.add(busStation);
			}
			return busStations;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
