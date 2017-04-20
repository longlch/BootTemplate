package com.guru.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
	private List<RouteElement> grapRouteElement = dataUtil.graphRouteElement();

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

	public double mileToKilometer(double mile) {
		return mile * 1.609344;
	}

	public double kilometerToMeter(double kilometer) {
		return kilometer * 1000;
	}

	public String[] addElement(String[] strArr, String element) {
		strArr = Arrays.copyOf(strArr, strArr.length + 1);
		strArr[strArr.length - 1] = element;
		return strArr;
	}

	public List<RouteElement> routeElementsWithOrigin(LatLng originLatLng) {
		/*
		 * find the nearly busStation in busStation.json file by lat lng then
		 * call Google matrix to calculate excatly distance
		 */
		List<RouteElement> routeElements = new ArrayList<>();
		List<BusStation> nearlyBStations = new ArrayList<>();
		double distance = 0;
		int nearlyBusStationsLength = 0;
		int realDistance = 0;
		int destinationsLength = 0;
		int matrixLength = 0;
		String[] destinations = {};
		String originLatLngStr = originLatLng.lat + "," + originLatLng.lng;
		String[] origins = { originLatLngStr };
		String latLng = "";
		DistanceMatrix matrix;
		List<Integer> realDistances = new ArrayList<>();

		// get nearly bus station and it's id
		for (BusStation busStation : bStations) {
			distance = this.distanceToMileByLatLng(originLatLng.lat, originLatLng.lng, busStation.getLat(),
					busStation.getLng());
			if (distance < 0.3 && distance > 0) {
				nearlyBStations.add(busStation);
			}
		}
		nearlyBusStationsLength = nearlyBStations.size();
		System.out.println("nearly busStation length with origin " + nearlyBStations.size());

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

	public List<RouteElement> routeElementsWithDestination(LatLng destinationLatLng) {
		List<RouteElement> routeElements = new ArrayList<>();
		List<BusStation> nearlyBStations = new ArrayList<>();
		double distance = 0;
		int realDistance = 0;
		int destinationsLength = 0;
		int nearlyBusStationsLength = 0;
		int matrixLength = 0;
		String destinationLatLngStr = destinationLatLng.lat + "," + destinationLatLng.lng;
		String[] destinations = {};
		String[] origins = { destinationLatLngStr };
		String latLng = "";
		DistanceMatrix matrix;
		List<Integer> realDistances = new ArrayList<>();

		for (BusStation busStation : bStations) {
			distance = this.distanceToMileByLatLng(destinationLatLng.lat, destinationLatLng.lng, busStation.getLat(),
					busStation.getLng());
			if (distance < 1 && distance > 0) {
				nearlyBStations.add(busStation);
			}
		}
		nearlyBusStationsLength = nearlyBStations.size();
		System.out.println("nearly busStation length with destination " + nearlyBusStationsLength);

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
		return routeElements;
	}

	// we need to refresh or delete grapRouteElement after using this function
	public List<RouteElement> createGraphWithOrignDestination(LatLng originLatLng, LatLng destinationLatLng) {
		grapRouteElement.addAll(this.routeElementsWithOrigin(originLatLng));
		grapRouteElement.addAll(this.routeElementsWithDestination(destinationLatLng));
		return grapRouteElement;
	}

	public List<RouteElement> findDirection(String originAddress, String destinationAddress) {

		ArrayList<RouteElement> routeDirection = new ArrayList<RouteElement>();
		List<RouteElement> routeElementsWithOriginDestination = new ArrayList<>();
		List<Vertex> vertexes = new ArrayList<Vertex>();
		List<Edge> edges = new ArrayList<Edge>();

		// geocode here
		GeocodingResult[] result = ggMatrix.geocodeFromAddress(originAddress);
		LatLng originLatLng = new LatLng(result[0].geometry.location.lat, result[0].geometry.location.lng);
		result = ggMatrix.geocodeFromAddress(destinationAddress);
		LatLng destinationLatLng = new LatLng(result[0].geometry.location.lat, result[0].geometry.location.lng);
		System.out.println("" + originLatLng.toString());
		System.out.println("" + destinationLatLng.toString());

		vertexes.add(new Vertex(-1, "Origin", originLatLng.lat, originLatLng.lng));
		for (BusStation bs : bStations) {
			vertexes.add(new Vertex(bs.getId(), bs.getName(), bs.getLat(), bs.getLng()));
		}
		vertexes.add(new Vertex(9999, "Destination", destinationLatLng.lat, destinationLatLng.lng));

		routeElementsWithOriginDestination
				.addAll(this.createGraphWithOrignDestination(originLatLng, destinationLatLng));

		int count = 0;
		for (RouteElement route : routeElementsWithOriginDestination) {
			count++;
		}
		System.out.println("routeElementsWithOriginDestination" + count);

		int lengthRouteElementsOriDesti = routeElementsWithOriginDestination.size();
		for (int i = 1; i < lengthRouteElementsOriDesti; i++) {
			int source = 0, des = 0;
			for (Vertex v : vertexes) {
				if (v.getId() == routeElementsWithOriginDestination.get(i).getStationFromId())
					source = vertexes.indexOf(v);
				if (v.getId() == routeElementsWithOriginDestination.get(i).getStationToId())
					des = vertexes.indexOf(v);
			}
			edges.add(new Edge(i - 1 + "", vertexes.get(source), vertexes.get(des),
					routeElementsWithOriginDestination.get(i).getDistanceOnBus()
							+ routeElementsWithOriginDestination.get(i).getDistanceWalking()));
		}

		Graph graph = new Graph(vertexes, edges);
		DijkstraAlgorithm da = new DijkstraAlgorithm(graph);
		da.execute(vertexes.get(0));
		LinkedList<Vertex> path = da.getPath(vertexes.get(vertexes.size() - 1));
		for (int i = 1; i < path.size(); i++) {
			for (int j = 1; j < routeElementsWithOriginDestination.size(); j++) {
				if (routeElementsWithOriginDestination.get(j).getStationFromId() == path.get(i - 1).getId()
						&& routeElementsWithOriginDestination.get(j).getStationToId() == path.get(i).getId()) {
					routeDirection.add(routeElementsWithOriginDestination.get(j));
				}
			}
		}
		
//		temp short direction
//		routeDirection=(ArrayList<RouteElement>)this.shortDirection(routeDirection);
		return routeDirection;
	}

//	temp short direction
//	cut off busStation have a same id
	public List<RouteElement> shortDirection(List<RouteElement> routeElements) {
		int rouEleSize = routeElements.size();
		List<RouteElement> shortRouEles = new ArrayList<>();
		for (int i = 0; i < rouEleSize; i++) {
			
			if (routeElements.get(i).getStationFromId() == -1) {
				shortRouEles.add(routeElements.get(i));
			} else if (i == rouEleSize - 1) {
				shortRouEles.add(routeElements.get(i));
			}else if(routeElements.get(i).getBusRoute() != null && 
					routeElements.get(i+1).getBusRoute() != null){
				if(routeElements.get(i).getBusRoute().getId() != routeElements.get(i+1).getBusRoute().getId()){
					shortRouEles.add(routeElements.get(i));
				}
			}
		}
		return shortRouEles;
	}
	public List<BusStation> getBusStation(List<RouteElement> routeElements){
		List<Integer> stationIds= new ArrayList<>();
		List<BusStation> busStations= new ArrayList<>();
		for (RouteElement routeElement : routeElements) {
			if(routeElement.getStationFromId() != -1 || routeElement.getStationToId() !=9999){
				stationIds.add(routeElement.getStationFromId());
			}
		}
		for (BusStation busStation : bStations) {
			for (Integer id : stationIds) {
				if(busStation.getId() == id){
					busStations.add(busStation);
				}
			}
		}
		return busStations;
	}

	public static void main(String[] args) {
		Direction direction = new Direction();
		
	/*	 * int count = 0; // test routeElementsWithDestination() LatLng
		 * originLatLng = new LatLng(16.065238, 108.185810); LatLng
		 * destinationLatLng = new LatLng(16.065238, 108.185810);
		 * List<RouteElement> routeElements
		 * =direction.routeElementsWithOrigin(originLatLng);
		 * 
		 * for (RouteElement routeElement : routeElements) {
		 * System.out.println(routeElement.toString()); }
		 
		
		 * List<RouteElement> routeElements2 =
		 * direction.routeElementsWithDestination(originLatLng); for
		 * (RouteElement routeElement : routeElements2) {
		 * System.out.println(routeElement.toString()); }
		 
		
		 * List<RouteElement> routeElements =
		 * direction.createGraphWithOrignDestination(originLatLng,
		 * destinationLatLng); for (RouteElement routeElement : routeElements) {
		 * count++; } System.out.println(count);
		 
		
		 * List<RouteElement> routeElements=direction.grapRouteElement;
		 * 
		 * for (RouteElement routeElement : routeElements) { count++; }
		 * System.out.println(count);
*/		 

		
		/*  List<RouteElement> routeElementDirection =
		  direction.findDirection("435 hoang dieu, da nang",
		  "163 dung si thanh khe,da nang");*/
		 
		List<RouteElement> routeElementDirection = direction.findDirection("435 hoang dieu, da nang",
				"vincom ,da nang");
		System.out.println("cac tuyen va size la"+routeElementDirection.size());
		for (RouteElement routeElement : routeElementDirection) {
			System.out.println(routeElement);
		}

	}
}
