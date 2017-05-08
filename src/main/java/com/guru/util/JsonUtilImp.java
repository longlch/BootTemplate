package com.guru.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.guru.model.BusRoute;
import com.guru.model.BusStation;
import com.guru.model.BusStationDistance;
import com.guru.model.RouteElement;
import com.guru.model.WalkingPath;

@Service
public class JsonUtilImp implements IJsonUtil {

	@Override
	public List<BusStation> getBusStations() {
		JSONParser parser = new JSONParser();
		List<BusStation> busStations = new ArrayList<>();
		List<BusRoute> busRoutes;
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
				// alert here, initial too much array list
				busRoutes= new ArrayList<>();
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
	public List<BusRoute> getBusRoutes() {
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
											jsonArray.getJSONObject(i).getString("name"),jsonArray.getJSONObject(i).getString("information")));
			}
			return busRoutes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	

	@Override
	public List<BusStationDistance> getBusStationDistances() {
		JSONParser parser = new JSONParser();
		List<BusStationDistance> busStDistances= new ArrayList<>();
		try {
			Resource resource = new ClassPathResource("static/bus_route/busStationDistance.json");
			resource.toString();
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			
			for(int i = 0; i < jsonArray.length(); i++){
				busStDistances.add(new BusStationDistance(jsonArray.getJSONObject(i).getInt("stationFromId"),
						jsonArray.getJSONObject(i).getInt("stationToId"), 
						jsonArray.getJSONObject(i).getInt("distance"),
						new BusRoute(jsonArray.getJSONObject(i).getJSONObject("busRoute").getBoolean("turn"),
								jsonArray.getJSONObject(i).getJSONObject("busRoute").getInt("id"),
								jsonArray.getJSONObject(i).getJSONObject("busRoute").getString("name"))));
			}
			return busStDistances;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<WalkingPath> getWalkingPaths() {
		JSONParser parser = new JSONParser();
		List<WalkingPath> walkingPaths= new ArrayList<>();
		try {
			Resource resource = new ClassPathResource("static/bus_route/walkingPath.json");
			resource.toString();
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			
			for (int i = 0; i < jsonArray.length(); i++) {
				walkingPaths.add(new WalkingPath(jsonArray.getJSONObject(i).getInt("stationFromId"), 
						jsonArray.getJSONObject(i).getInt("stationToId"),
						jsonArray.getJSONObject(i).getInt("distance")));
			}
			return walkingPaths;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<RouteElement> graphRouteElement() {
		List<RouteElement> routeElements= new ArrayList<>();
		
		for (BusStationDistance busStationDistance: this.getBusStationDistances()) {
			routeElements.add(new RouteElement(busStationDistance.getStationFromId(), busStationDistance.getStationToId(), 0,
					busStationDistance.getDistance(), busStationDistance.getBusRoute()));
		}
		for (WalkingPath wPath :this.getWalkingPaths()) {
			routeElements.add(new RouteElement(wPath.getStationFromId(), wPath.getStationToId(),
					wPath.getDistance(), 0, null));
		}
		return routeElements;
	}

	@Override
	public List<BusStation> getBusStationFromBusRoute(int route,String trend) {
		JSONParser parser = new JSONParser();
		int[] busStationIds=null;
		Boolean turn = Boolean.valueOf(trend);
		List<BusStation> busStations= new ArrayList<>();
		try {
			Resource resource = new ClassPathResource("static/bus_route/busRoute.json");
			resource.toString();
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			String jsonString = obj.toString();
			JSONArray jsonArray = new JSONArray(jsonString);
			
			for (int i = 0; i < jsonArray.length(); i++) {
				if(jsonArray.getJSONObject(i).getInt("id") == route){
					if(turn){
						busStationIds=this.getStationIds(jsonArray.getJSONObject(i).getJSONArray("wayToGo"));
						break;
					}else{
						i++;
						busStationIds=this.getStationIds(jsonArray.getJSONObject(i).getJSONArray("wayToReturn"));
						break;
					}
				}
			}
			for (int i : busStationIds) {
				for (BusStation busStation : this.getBusStations()) {
					if(i==busStation.getId()){
						busStations.add(busStation);
						break;
					}
				}
			}
			return busStations;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int[] getStationIds (JSONArray jsonArray){
		int[] number= new int[jsonArray.length()];
		for(int i=0;i<jsonArray.length();i++){
			number[i]=jsonArray.optInt(i);
		}
		return number;
	}
	

	@Override
	public BusRoute getBusRouteDetail(int route, String trend) {
		List<BusRoute> busRoutes=this.getBusRoutes();
		Boolean turn = Boolean.valueOf(trend);

		for (BusRoute busRoute : busRoutes) {
			if(busRoute.getId()== route && busRoute.isTurn() == turn){
				return busRoute;
			}
		}
		return null;
	}

	@Override
	public String drawPolyline(int id, String trend) {
		JSONParser parser = new JSONParser();
		Resource resource= new ClassPathResource("static/bus_route/bus_route_polyline.json");
		String jsonString="";
		String jsArrStr="";
		String route="route_"+id;
		Boolean turn = Boolean.valueOf(trend);
		String turnStr="";
		if(turn){
			turnStr="way_to_go";
		}else{
			turnStr="way_to_return";
		}
		try {
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			jsonString = obj.toString();
			
			JSONObject jsObj= new JSONObject(jsonString);
			JSONObject jsObjRoute=jsObj.getJSONArray(route).getJSONObject(0);
			JSONArray jsWayToGo=jsObjRoute.getJSONArray(turnStr);
			jsArrStr=jsWayToGo.toString();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return jsArrStr;
		
	}
	
}
