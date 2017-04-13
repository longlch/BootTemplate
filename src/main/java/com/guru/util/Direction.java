package com.guru.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.guru.model.BusRoute;
import com.guru.model.BusStation;
import com.guru.model.BusStationDistance;
import com.guru.model.RouteElement;
import com.guru.model.WalkingPath;

public class Direction {

	// how to autowire this
	// @Autowired
	IJsonUtil dataUtil = new JsonUtilImp();

	// @Autowired
	IGoogleMapMatrixApi ggMatrix = new GoogleMapMatrixApiImp();

	// how to get bean from xml file from another Source
	private List<BusRoute> bRoutes = dataUtil.getBusRoutes();
	private List<BusStation> bStations = dataUtil.getBusStations();
	private List<BusStationDistance> bDistances = dataUtil.getBusStationDistances();
	private List<WalkingPath> wPaths = dataUtil.getWalkingPaths();

	public Direction() {
	}

	public double distanceToMileByLatLng(double lat1, double lng1, double lat2, double lng2) {
		double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
		lat1 = Math.toRadians(lat1);
		lng1 = Math.toRadians(lng1);
		lat2 = Math.toRadians(lat2);
		lng2 = Math.toRadians(lng2);
		double angle = Math
				.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng1 - lng2));
		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}

	private double mileToKilometer(double mile) {
		return mile * 1.609344;
	}

	private double kilometerToMeter(double kilometer) {
		return kilometer * 1000;
	}

	private  String[] addElement(String[] strArr,String element){
		strArr=Arrays.copyOf(strArr, strArr.length+1);
		strArr[strArr.length-1]=element;
		return strArr;
	}
	private List<RouteElement> routeElementsWithOrigin(String originAddress) {
		/*
		 * find the nearly busStation in busStation.json file by lat lng then
		 * call Google matrix to calculate excatly distance
		 */
		List<RouteElement> routeElements = new ArrayList<>();
		List<BusStation> nearlyBStations = new ArrayList<>();
		List<Integer> stationToIds = new ArrayList<>();
		GeocodingResult[] results=ggMatrix.geocodeFromAddress(originAddress);
		LatLng originLatLng= new LatLng(results[0].geometry.location.lat, results[0].geometry.location.lng);
		double distance;
		
//		get nearly bus station and it's id
		for (BusStation busStation : bStations) {
			distance = this.distanceToMileByLatLng(originLatLng.lat, originLatLng.lng, busStation.getLat(), busStation.getLng());
			if (distance < 0.3 && distance > 0) {
				nearlyBStations.add(busStation);
			}
		}
		
//		metter
		int realDistance=0;
//		calculate exactly distance from origin to bus
		String [] destinations= {};
		String [] origins= {originAddress};
		String latLng = "";
		routeElements.add(new RouteElement(-1, -1, 0, 0, null));
		DistanceMatrix matrix;
		List<Integer> realDistances= new ArrayList<>();
		int destinationsLength=destinations.length;
		int count = 0;
		System.out.println(nearlyBStations.toString());
		for(int i=0, k=nearlyBStations.size(); i < k ; i++){
			if(count==24 && i<k-1 || count<=24 && i==k-1) {
				
				if(latLng.length()>0) destinations = latLng.substring(0, latLng.length()-1).split("|");
				else this.addElement(destinations, nearlyBStations.get(i).getLat()+", "+nearlyBStations.get(i).getLng());
				System.out.println(destinations);
				matrix= ggMatrix.getDistanceMatrixUser(origins, destinations);
				for(int j=0;j<matrix.rows[0].elements.length;j++){
					realDistance=(int)matrix.rows[0].elements[j].distance.inMeters;
					realDistances.add(realDistance);
					routeElements.add(new RouteElement(-1, nearlyBStations.get(i-j).getId(), realDistance, 0, null));
				}
				count=0;
				latLng = "";				
			}
			else {
				latLng=latLng+nearlyBStations.get(i).getLat()+", "+nearlyBStations.get(i).getLng()+"|";
				count++;
			}
		}
		
		for(int i=0; i<nearlyBStations.size();i++){
			routeElements.add(new RouteElement(-1,nearlyBStations.get(i).getId(),
					realDistances.get(i),0, null));
		}
		
		return routeElements;
	}

	public static void main(String[] args) {
		Direction direction = new Direction();
		// test init gg matrix object
		/*
		 * List<BusStation> busStations= direction.bStation; for (BusStation
		 * busStation : busStations) {
		 * System.out.println(busStation.toString()); }
		 * System.out.println(direction.ggMatrix);
		 */

		// test distanceToMileByLatLng function
		/*LatLng origin = new LatLng(16.065331, 108.185810);
		List<RouteElement> routeElements = new ArrayList<>();
		List<BusStation> nearlyBStations = new ArrayList<>();
		double distance;
		for (BusStation busStation : direction.bStations) {
			distance = direction.distanceToMileByLatLng(origin.lat, origin.lng, busStation.getLat(),
					busStation.getLng());
			if (distance < 0.3 && distance > 0) {
				System.out.println("distance" + distance);
				nearlyBStations.add(busStation);
			}
		}
		int count = 0;
		for (BusStation busStation : nearlyBStations) {
			System.out.println(++count + " " + busStation.toString());
		}*/
		List<RouteElement> routeElements= direction.routeElementsWithOrigin("135 cu chinh lan, da nang");
		for (RouteElement routeElement : routeElements) {
			System.out.println(routeElement.toString());
		}
	}
}
