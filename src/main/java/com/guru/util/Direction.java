package com.guru.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
		// System.out.println("nearly busStation length with origin " +
		// nearlyBStations.size());

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
		// System.out.println("nearly busStation length with destination " +
		// nearlyBusStationsLength);

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
		List<RouteElement> routeElements = new ArrayList<>();

		routeElements.addAll(grapRouteElement);
		routeElements.addAll(this.routeElementsWithOrigin(originLatLng));
		routeElements.addAll(this.routeElementsWithDestination(destinationLatLng));
		return routeElements;
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
		System.out.println("origin " + originLatLng.lat + "," + originLatLng.lng);
		System.out.println("desti " + destinationLatLng.lat + "," + destinationLatLng.lng);
		vertexes.add(new Vertex(-1, "Origin", originLatLng.lat, originLatLng.lng));

		for (BusStation bs : bStations) {
			vertexes.add(new Vertex(bs.getId(), bs.getName(), bs.getLat(), bs.getLng()));
		}
		vertexes.add(new Vertex(9999, "Destination", destinationLatLng.lat, destinationLatLng.lng));

		routeElementsWithOriginDestination
				.addAll(this.createGraphWithOrignDestination(originLatLng, destinationLatLng));

		System.out.println("size routeElementsWithOriginDestination la " + routeElementsWithOriginDestination.size());

		/*
		 * int count = 0; for (RouteElement route :
		 * routeElementsWithOriginDestination) { count++; }
		 * System.out.println("routeElementsWithOriginDestination" + count);
		 */

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

		// temp short direction
		// routeDirection=(ArrayList<RouteElement>)this.shortDirection(routeDirection);
		return routeDirection;
	}

	// temp short direction
	// cut off busStation have a same id
	public static List<RouteElement> shortDirection(List<RouteElement> routeElements) {
		int rouEleSize = routeElements.size();
		List<RouteElement> shortRouEles = new ArrayList<>();
		for (int i = 0; i < rouEleSize; i++) {

			if (routeElements.get(i).getStationFromId() == -1) {
				shortRouEles.add(routeElements.get(i));
			} else if (i == rouEleSize - 1) {
				shortRouEles.add(routeElements.get(i));
			} else if (routeElements.get(i).getBusRoute() != null && routeElements.get(i + 1).getBusRoute() != null) {
				if (routeElements.get(i).getBusRoute().getId() != routeElements.get(i + 1).getBusRoute().getId()) {
					shortRouEles.add(routeElements.get(i));
				}
			}
		}
		return shortRouEles;
	}

	public List<BusStation> getBusStation(List<RouteElement> routeElements) {
		List<Integer> stationIds = new ArrayList<>();
		List<BusStation> busStations = new ArrayList<>();
		for (RouteElement routeElement : routeElements) {
			if (routeElement.getStationFromId() != -1 || routeElement.getStationToId() != 9999) {
				stationIds.add(routeElement.getStationFromId());
			}
		}
		for (BusStation busStation : bStations) {
			for (Integer id : stationIds) {
				if (busStation.getId() == id) {
					busStations.add(busStation);
				}
			}
		}
		return busStations;
	}

	public static List<RouteElement> minimizeRouteElement3(List<RouteElement> routeElements) {
		List<RouteElement> rouEleMini = new ArrayList<>();
		int distanceWalking = 0;
		int distanceOnBus = 0;
		int stationFromId = 0;
		int stationToId = 0;

		ListIterator<RouteElement> lI = routeElements.listIterator();

		return rouEleMini;
	}

	public static List<RouteElement> minimizeRouteElement(List<RouteElement> routeElements) {
		List<RouteElement> mininRouEles = new ArrayList<>();
		int distanceWalking = 0;
		int distanceOnBus = 0;
		int index = -1;
		RouteElement routeElement = null;
		RouteElement temp = null;
		for (int i = 0; i < routeElements.size(); i++) {
			routeElement = routeElements.get(i);
			if (i == 0) {
				mininRouEles.add(routeElements.get(i));
			} else if (routeElement.getBusRoute() == null) {
				temp = routeElements.get(i);
				temp.setStationFromId(routeElement.getStationToId());
				mininRouEles.add(temp);
			} else {
				if (i > 1) {
					if (routeElement.getBusRoute().getId() == routeElements.get(i - 1).getBusRoute().getId()) {
						temp = routeElements.get(i - 1);

					}
				}
			}
			/*
			 * if(i==0){ mininRouEles.add(routeElements.get(i)); }else
			 * if(routeElement.getBusRoute() != null){ mininRouEles.add(new
			 * RouteElement(routeElements.get(i-1).getStationFromId(),
			 * routeElement.getStationToId(),0,0,routeElement.getBusRoute()));
			 * }else{
			 * 
			 * }
			 */
			/*
			 * if(i==0){ mininRouEles.add(routeElements.get(i)); } else{
			 * if(routeElements.get(i).getBusRoute() != null &&
			 * routeElements.get(i-1).getBusRoute() != null){ distanceWalking=0;
			 * if(routeElements.get(i).getBusRoute().getId() ==
			 * routeElements.get(i-1).getBusRoute().getId() ){ if(index == -1){
			 * index=routeElements.get(i).getStationFromId(); }
			 * distanceOnBus=routeElements.get(i).getDistanceOnBus()+
			 * routeElements.get(i-1).getDistanceOnBus(); }else{
			 * mininRouEles.add(new RouteElement(index,
			 * routeElements.get(i).getStationToId(), 0, distanceOnBus,
			 * routeElements.get(i).getBusRoute())); distanceOnBus=0; index=-1;
			 * } } }
			 */
		}
		return mininRouEles;
	}

	public static List<RouteElement> miniDirection2(List<RouteElement> direction) {
		ArrayList<RouteElement> mdirectionResult = new ArrayList<>();
		for (int i = 0, j = direction.size(); i < j; i++) {
			if (i == 0)
				i++;
			if (i > 0 && direction.get(i).getDistanceWalking() > 0) {
				if (i < j - 1 && direction.get(i + 1).getDistanceWalking() > 0) {
					int _from, _to;
					int _distance = 0;
					_from = direction.get(i).getStationFromId();
					while (i <= j - 2 && direction.get(i + 1).getDistanceWalking() > 0) {
						_distance += direction.get(i).getDistanceWalking();
						i++;
					}
					_distance += direction.get(i).getDistanceWalking();
					_to = direction.get(i).getStationToId();
					mdirectionResult.add(new RouteElement(_from, _to, _distance, 0, direction.get(i).getBusRoute()));
				} else
					mdirectionResult.add(direction.get(i));
			} else if (i > 0 && direction.get(i).getDistanceOnBus() > 0 && direction.get(i).getBusRoute() != null) {
				int _from, _to;
				int _distance = 0;
				_from = direction.get(i).getStationFromId();
				while (direction.get(i + 1).getDistanceOnBus() > 0
						&& direction.get(i).getBusRoute().getId().equals(direction.get(i + 1).getBusRoute().getId())
						&& direction.get(i).getBusRoute().isTurn() == direction.get(i + 1).getBusRoute().isTurn()) {
					_distance += direction.get(i).getDistanceOnBus();
					i++;
				}
				_distance += direction.get(i).getDistanceOnBus();
				_to = direction.get(i).getStationToId();
				mdirectionResult.add(new RouteElement(_from, _to, 0, _distance, direction.get(i).getBusRoute()));
			}
		}
		return direction;
	}

	public static void directionHint(List<RouteElement> routeElements) {
		List<RouteElement> rouEleMini = new ArrayList<>();
		int distanceWalking = 0;
		int distanceOnBus = 0;
		int stationFromId = 0;
		int stationToId = 0;
//		if route 

		ListIterator<RouteElement> lI = routeElements.listIterator();
		while (lI.hasNext()) {
			RouteElement routeElement = (RouteElement) lI.next();
			System.out.println(routeElement.toString());
		}

	}

	public static void main(String[] args) {
		Direction direction = new Direction();

		// * int count = 0; // test routeElementsWithDestination() LatLng
		// * originLatLng = new LatLng(16.065238, 108.185810); LatLng
		// * destinationLatLng = new LatLng(16.065238, 108.185810);
		// * List<RouteElement> routeElements
		// * =direction.routeElementsWithOrigin(originLatLng);
		// *
		// * for (RouteElement routeElement : routeElements) {
		// * System.out.println(routeElement.toString()); }
		//
		//
		// * List<RouteElement> routeElements2 =
		// * direction.routeElementsWithDestination(originLatLng); for
		// * (RouteElement routeElement : routeElements2) {
		// * System.out.println(routeElement.toString()); }
		//
		//
		// * List<RouteElement> routeElements =
		// * direction.createGraphWithOrignDestination(originLatLng,
		// * destinationLatLng); for (RouteElement routeElement : routeElements)
		// {
		// * count++; } System.out.println(count);
		//
		//
		// * List<RouteElement> routeElements=direction.grapRouteElement;
		// *
		// * for (RouteElement routeElement : routeElements) { count++; }
		// * System.out.println(count);


/*		List<RouteElement> routeElementDirection = direction.findDirection("435 hoang dieu, da nang",
				"163 dung si thanh khe,da nang");*/
		List<RouteElement> routeElementDirection = direction.findDirection("135 cu chinh lan, da nang",
				"466 le duan,da nang");
		System.out.println("cac tuyen va size la" + routeElementDirection.size());
		for (RouteElement routeElement : routeElementDirection) {
			System.out.println(routeElement);
		}
		System.out.println("short here");
		directionHint(routeElementDirection);
		// short direction
		System.out.println("short here");
		List<RouteElement> shortRouteDirect = miniDirection2(routeElementDirection);
		System.out.println(shortRouteDirect.size());
		for (RouteElement routeElement : shortRouteDirect) {
			System.out.println(routeElement);
		}

	}
}
