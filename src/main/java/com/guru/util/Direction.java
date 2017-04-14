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
	private List<RouteElement> grapRouteElement=dataUtil.graphRouteElement();

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

	private String[] addElement(String[] strArr, String element) {
		strArr = Arrays.copyOf(strArr, strArr.length + 1);
		strArr[strArr.length - 1] = element;
		return strArr;
	}

	private List<RouteElement> routeElementsWithOrigin(String originAddress) {
		/*
		 * find the nearly busStation in busStation.json file by lat lng then
		 * call Google matrix to calculate excatly distance
		 */
		List<RouteElement> routeElements = new ArrayList<>();
		List<BusStation> nearlyBStations = new ArrayList<>();
		GeocodingResult[] results = ggMatrix.geocodeFromAddress(originAddress);
		LatLng originLatLng = new LatLng(results[0].geometry.location.lat, results[0].geometry.location.lng);
		double distance = 0;

		// get nearly bus station and it's id
		for (BusStation busStation : bStations) {
			distance = this.distanceToMileByLatLng(originLatLng.lat, originLatLng.lng, busStation.getLat(),
					busStation.getLng());
			if (distance < 0.3 && distance > 0) {
				nearlyBStations.add(busStation);
			}
		}

		System.out.println("busStation length " + nearlyBStations.size());
		// metter
		int realDistance = 0;
		// calculate exactly distance from origin to bus
		String[] destinations = {};
		String[] origins = { originAddress };
		String latLng = "";
		routeElements.add(new RouteElement(-1, -1, 0, 0, null));
		DistanceMatrix matrix;
		List<Integer> realDistances = new ArrayList<>();
		int destinationsLength = 0;
		int nearlyBusStationsLength = nearlyBStations.size();
		int matrixLength = 0;
		for (int i = 0; i < nearlyBusStationsLength; i++) {
			latLng = nearlyBStations.get(i).getLat() + "," + nearlyBStations.get(i).getLng();
			destinations = this.addElement(destinations, latLng);
			destinationsLength = destinations.length;

			if (destinationsLength == 100) {
				matrix = ggMatrix.getDistanceMatrixUser(origins, destinations);
				matrixLength = matrix.rows[0].elements.length;
				for (int j = 0; j < matrixLength; j++) {
					realDistance = (int) matrix.rows[0].elements[j].distance.inMeters;
					realDistances.add(realDistance);
				}
				destinations = new String[] {};
			} else if (destinationsLength < 100 && i == nearlyBusStationsLength - 1) {
				matrix = ggMatrix.getDistanceMatrixUser(origins, destinations);
				matrixLength = matrix.rows[0].elements.length;
				for (int j = 0; j < matrixLength; j++) {
					realDistance = (int) matrix.rows[0].elements[j].distance.inMeters;
					realDistances.add(realDistance);
				}
				destinations = new String[] {};
			}
		}
		// add stationToId to Route Element
		for (int i = 0; i < nearlyBusStationsLength; i++) {
			routeElements.add(new RouteElement(-1, nearlyBStations.get(i).getId(), realDistances.get(i), 0, null));
		}
		return routeElements;
	}

	private List<RouteElement> routeElementsWithDestination(String destinationAddress) {
		List<RouteElement> routeElements = new ArrayList<>();
		List<BusStation> nearlyBStations = new ArrayList<>();
		double distance = 0;
		GeocodingResult[] results = ggMatrix.geocodeFromAddress(destinationAddress);
		LatLng destinationLatLng = new LatLng(results[0].geometry.location.lat, results[0].geometry.location.lng);

		for (BusStation busStation : bStations) {
			distance = this.distanceToMileByLatLng(destinationLatLng.lat, destinationLatLng.lng, busStation.getLat(),
					busStation.getLng());
			if (distance < 1 && distance > 0) {
				nearlyBStations.add(busStation);
			}
		}
		System.out.println("nearly busStation length " + nearlyBStations.size());
		// caculate a real distance
		int realDistance = 0;
		// calculate exactly distance from origin to bus
		String[] destinations = {};
		String[] origins = { destinationAddress };
		String latLng = "";
		DistanceMatrix matrix;
		List<Integer> realDistances = new ArrayList<>();
		int destinationsLength = 0;
		int nearlyBusStationsLength = nearlyBStations.size();
		int matrixLength = 0;
		for (int i = 0; i < nearlyBusStationsLength; i++) {
			latLng = nearlyBStations.get(i).getLat() + "," + nearlyBStations.get(i).getLng();
			destinations = this.addElement(destinations, latLng);
			destinationsLength = destinations.length;

			if (destinationsLength == 100) {
				matrix = ggMatrix.getDistanceMatrixUser(origins, destinations);
				matrixLength = matrix.rows[0].elements.length;
				for (int j = 0; j < matrixLength; j++) {
					realDistance = (int) matrix.rows[0].elements[j].distance.inMeters;
					realDistances.add(realDistance);
				}
				destinations = new String[] {};
			} else if (destinationsLength < 100 && i == nearlyBusStationsLength - 1) {
				matrix = ggMatrix.getDistanceMatrixUser(origins, destinations);
				matrixLength = matrix.rows[0].elements.length;
				for (int j = 0; j < matrixLength; j++) {
					realDistance = (int) matrix.rows[0].elements[j].distance.inMeters;
					realDistances.add(realDistance);
				}
				destinations = new String[] {};
			}
		}
		// add stationToId to Route Element
		for (int i = 0; i < nearlyBusStationsLength; i++) {
			routeElements.add(new RouteElement(nearlyBStations.get(i).getId(), 9999, realDistances.get(i), 0, null));
		}
		routeElements.add(new RouteElement(-1, 9999, 0, 0, null));
		return routeElements;
	}

	
	// we need to refresh or delete grapRouteElement after using this function
	private List<RouteElement> createGraphWithOrignDestination(String originAddress,String destinationAddress){
		grapRouteElement.addAll(this.routeElementsWithOrigin(originAddress));
		grapRouteElement.addAll(this.routeElementsWithDestination(destinationAddress));
		return grapRouteElement;
	}
	
	private List<RouteElement> findDirection(String originAddress,String destinationAddress){
		 ArrayList<RouteElement> routeDirection = new ArrayList<RouteElement>();
		 List<RouteElement> routeElementsWithOriginDestination=new ArrayList<>();
		 List<Vertex> vertexes = new ArrayList<Vertex>();
	     List<Edge> edges = new ArrayList<Edge>();
	     
//	     vertexes.add(new Vertex(-1, "Origin", origin.latitude, origin.longitude));
	     for(BusStation bs: bStations) {
	            vertexes.add(new Vertex(bs.getId(), bs.getName(), bs.getLat(), bs.getLng()));
	     }
//	     vertexes.add(new Vertex(999, "Destination", destination.latitude, destination.longitude));
	     
	     routeElementsWithOriginDestination.addAll(this.createGraphWithOrignDestination(originAddress, destinationAddress));
		return routeDirection;
	}
	public static void main(String[] args) {
		Direction direction = new Direction();
		
//		test routeElementsWithDestination()
/*		List<RouteElement> routeElements = direction.routeElementsWithDestination("135 cu chinh lan, da nang");
		for (RouteElement routeElement : routeElements) {
			System.out.println(routeElement.toString());
		}*/
		
		List<RouteElement> routeElements=direction.grapRouteElement;
		int count=0;
		for (RouteElement routeElement : routeElements) {
			count++;
		}
		System.out.println(count);
	}
}
