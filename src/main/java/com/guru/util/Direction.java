package com.guru.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.guru.exception.DestiNearbyException;
import com.guru.exception.DirectionException;
import com.guru.exception.OriginNearlyException;
import com.guru.model.BusRoute;
import com.guru.model.BusStation;
import com.guru.model.BusStationDistance;
import com.guru.model.NDirection;
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
	public List<BusStation> oriDestiBusStation=new ArrayList<>();
	
	

	public Direction() {
	}

	public ArrayList<RouteElement> minimizeDirection(int numBus, List<RouteElement> direction) {
		ArrayList<RouteElement> list = new ArrayList<>();
		list.addAll(direction);
		list = new Node().getRouteWithNumberRoute(numBus, list);
		// System.out.println(list.toString());
		list = list != null ? list : null;

		return list;
	}

	public List<RouteElement> modifiedDirection(ArrayList<RouteElement> direction) {
		ArrayList<RouteElement> mdirectionResult = new ArrayList<>();

		if (direction != null) {
			for (int i = 0, j = direction.size(); i < j; i++) {
				if (direction.get(i).getDistanceWalking() > 0) {
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
						mdirectionResult
								.add(new RouteElement(_from, _to, _distance, 0, direction.get(i).getBusRoute()));
					} else
						mdirectionResult.add(direction.get(i));
				} else if (direction.get(i).getDistanceOnBus() > 0 && direction.get(i).getBusRoute() != null) {
					int _from, _to;
					int _distance = 0;
					_from = direction.get(i).getStationFromId();
					while (direction.get(i + 1).getDistanceOnBus() > 0
							&& direction.get(i).getBusRoute().getId() == direction.get(i + 1).getBusRoute().getId()
							&& direction.get(i).getBusRoute().isTurn() == direction.get(i + 1).getBusRoute().isTurn()) {
						_distance += direction.get(i).getDistanceOnBus();
						i++;
					}
					_distance += direction.get(i).getDistanceOnBus();
					_to = direction.get(i).getStationToId();
					mdirectionResult.add(new RouteElement(_from, _to, 0, _distance, direction.get(i).getBusRoute()));
				}
			}
		}
		return mdirectionResult;
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

	public List<RouteElement> routeElementsWithOrigin(LatLng originLatLng) throws OriginNearlyException{
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
			if (distance < 0.6 && distance > 0) {
				nearlyBStations.add(busStation);
			}
		}
		HashMap<String, Integer> listBusStation = new HashMap<>();
        HashMap<String, Double> listDistance = new HashMap<>();
        for(int i = 0; i < nearlyBStations.size() - 1; i++) {
            BusStation a = nearlyBStations.get(i);
            double distanceA = (a != null) ? this.distanceToMileByLatLng(originLatLng.lat, originLatLng.lng, a.getLat(), a.getLng()) : 0;
            if(listBusStation.size() == 0 && listDistance.size() == 0) {
                for(BusRoute br : a.getBusList()) {
                    listBusStation.put(br.getId()+""+br.isTurn(), a.getId());
                    listDistance.put(br.getId()+""+br.isTurn(), distanceA);
                }
            } else {
                for(BusRoute br : a.getBusList()) {
                    if(listDistance.containsKey(br.getId()+""+br.isTurn())) {
                        if(listDistance.get(br.getId()+""+br.isTurn()) > distanceA) {
                            listBusStation.put(br.getId()+""+br.isTurn(), a.getId());
                            listDistance.put(br.getId()+""+br.isTurn(), distanceA);
                        }
                    }
                    else {
                        listBusStation.put(br.getId()+""+br.isTurn(), a.getId());
                        listDistance.put(br.getId()+""+br.isTurn(), distanceA);
                    }
                }
            }
        }
        List<BusStation> tmp = new ArrayList<>();
        for(BusStation b : nearlyBStations) {
        	if(listBusStation.containsValue(b.getId())) tmp.add(b);
        }
        nearlyBStations.clear();
        nearlyBStations.addAll(tmp);
		nearlyBusStationsLength = nearlyBStations.size();
		System.out.println("nearly busStation length with origin " +
				 nearlyBStations.size());
		if(nearlyBusStationsLength == 0){
			throw new OriginNearlyException("No Bus nearby Ori");
		}
		 

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

	public List<RouteElement> routeElementsWithDestination(LatLng destinationLatLng) throws DestiNearbyException{
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
			if (distance < 0.6 && distance > 0) {
				nearlyBStations.add(busStation);
			}
		}
		HashMap<String, Integer> listBusStation = new HashMap<>();
        HashMap<String, Double> listDistance = new HashMap<>();
        for(int i = 0; i < nearlyBStations.size() - 1; i++) {
            BusStation a = nearlyBStations.get(i);
            double distanceA = (a != null) ? this.distanceToMileByLatLng(destinationLatLng.lat, destinationLatLng.lng, a.getLat(), a.getLng()) : 0;
            if(listBusStation.size() == 0 && listDistance.size() == 0) {
                for(BusRoute br : a.getBusList()) {
                    listBusStation.put(br.getId()+""+br.isTurn(), a.getId());
                    listDistance.put(br.getId()+""+br.isTurn(), distanceA);
                }
            } else {
                for(BusRoute br : a.getBusList()) {
                    if(listDistance.containsKey(br.getId()+""+br.isTurn())) {
                        if(listDistance.get(br.getId()+""+br.isTurn()) > distanceA) {
                            listBusStation.put(br.getId()+""+br.isTurn(), a.getId());
                            listDistance.put(br.getId()+""+br.isTurn(), distanceA);
                        }
                    }
                    else {
                        listBusStation.put(br.getId()+""+br.isTurn(), a.getId());
                        listDistance.put(br.getId()+""+br.isTurn(), distanceA);
                    }
                }
            }
        }
        List<BusStation> tmp = new ArrayList<>();
        for(BusStation b : nearlyBStations) {
        	if(listBusStation.containsValue(b.getId())) tmp.add(b);
        }
        nearlyBStations.clear();
        nearlyBStations.addAll(tmp);
		nearlyBusStationsLength = nearlyBStations.size();
		System.out.println("nearly busStation length with destination " +nearlyBusStationsLength);
		if(nearlyBusStationsLength == 0){
			throw new DestiNearbyException("No Bus nearby Desti");
		}
		 

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
	public List<RouteElement> createGraphWithOrignDestination(LatLng originLatLng, LatLng destinationLatLng) throws OriginNearlyException,DestiNearbyException{
		List<RouteElement> routeElements = new ArrayList<>();

		routeElements.addAll(grapRouteElement);
		routeElements.addAll(this.routeElementsWithOrigin(originLatLng));
		routeElements.addAll(this.routeElementsWithDestination(destinationLatLng));
		return routeElements;
	}


	
	public List<RouteElement> directInMap2(String originAddress, String destinationAddress, int maxBusRoute) throws OriginNearlyException,DestiNearbyException,DirectionException{
		
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
		////////////
		BusStation bs1= new BusStation(-1,"origin",originLatLng.lat,originLatLng.lng,null);
		BusStation bs2= new BusStation(9999,"desti",destinationLatLng.lat,destinationLatLng.lng,null);
		oriDestiBusStation.add(0,bs1);
		oriDestiBusStation.add(1,bs2);
		///////////
		for (BusStation bs : bStations) {
			vertexes.add(new Vertex(bs.getId(), bs.getName(), bs.getLat(), bs.getLng()));
		}
		vertexes.add(new Vertex(9999, "Destination", destinationLatLng.lat, destinationLatLng.lng));
		
		routeElementsWithOriginDestination
		.addAll(this.createGraphWithOrignDestination(originLatLng, destinationLatLng));
		
		
		/*
		 * int count = 0; for (RouteElement route :
		 * routeElementsWithOriginDestination) { count++; }
		 * System.out.println("routeElementsWithOriginDestination" + count);
		 */
		
//		int lengthRouteElementsOriDesti = routeElementsWithOriginDestination.size();
//		for (int i = 1; i < lengthRouteElementsOriDesti; i++) {
//			int source = 0, des = 0;
//			for (Vertex v : vertexes) {
//				if (v.getId() == routeElementsWithOriginDestination.get(i).getStationFromId())
//					source = vertexes.indexOf(v);
//				if (v.getId() == routeElementsWithOriginDestination.get(i).getStationToId())
//					des = vertexes.indexOf(v);
//			}
//			edges.add(new Edge(i - 1 + "", vertexes.get(source), vertexes.get(des),
//					routeElementsWithOriginDestination.get(i).getDistanceOnBus()
//					+ routeElementsWithOriginDestination.get(i).getDistanceWalking()));
//		}
		
//		Graph graph = new Graph(vertexes, edges);
//		DijkstraAlgorithm da = new DijkstraAlgorithm(graph);
//		da.execute(vertexes.get(0));
//		LinkedList<Vertex> path = da.getPath(vertexes.get(vertexes.size() - 1));
//		for (int i = 1; i < path.size(); i++) {
//			for (int j = 1; j < routeElementsWithOriginDestination.size(); j++) {
//				if (routeElementsWithOriginDestination.get(j).getStationFromId() == path.get(i - 1).getId()
//						&& routeElementsWithOriginDestination.get(j).getStationToId() == path.get(i).getId()) {
//					routeDirection.add(routeElementsWithOriginDestination.get(j));
//				}
//			}
//		}
		ArrayList<ArrayList<RouteElement>> direction = new ArrayList<>();
		List<List<String>> path = new ArrayList<>();
		com.guru.model.Graph newGraph = new com.guru.model.Graph();
		for(int i = 0, j = routeElementsWithOriginDestination.size(); i < j; i++) {
            newGraph.add(routeElementsWithOriginDestination.get(i).getStationFromId()+"", routeElementsWithOriginDestination.get(i).getStationToId()+"",
            		routeElementsWithOriginDestination.get(i).getDistanceWalking() + routeElementsWithOriginDestination.get(i).getDistanceOnBus());
        }
        NDirection directionAlgorithm = new NDirection();
        path = directionAlgorithm.findAllShortestPaths(newGraph, "-1", "9999");
        
        for (int i = 0, k = path.size(); i < k; i++) {
            ArrayList<RouteElement> dr = new ArrayList<>();
            for(int j = 0, m = path.get(i).size() - 1; j < m; j++) {
                dr.add(findRouteElement(routeElementsWithOriginDestination, path.get(i).get(j), path.get(i).get(j+1)));
            }
            direction.add(dr);
        }
        for(int k = 0, l = direction.size(); k < l; k++)
            if(direction.get(k).size() > 3)
                for(int i = 0, j = direction.get(k).size() - 3; i < j; i++) {
                    if(direction.get(k).get(i).getBusRoute() != null && direction.get(k).get(i+2).getBusRoute() != null
                            && direction.get(k).get(i).getBusRoute().equals(direction.get(k).get(i+2).getBusRoute())
                            && direction.get(k).get(i+1).getBusRoute() == null) {
                        direction.get(k).get(i+1).setBusRoute(direction.get(k).get(i).getBusRoute());
                    }
                }
        routeDirection.addAll(direction.get(0));
		if(routeDirection.size() == 0  || routeDirection.size() == 2){
			throw new DirectionException("can't find the direction at map");
		}
//		routeDirection = this.minimizeDirection(maxBusRoute, routeDirection);
		routeDirection = (ArrayList<RouteElement>) this.modifiedDirection(routeDirection);
		return routeDirection;
	}
	private static RouteElement findRouteElement(List<RouteElement> list, String from, String to) {
        for(int i = 0, j = list.size(); i < j; i++)
            if(from.equals(list.get(i).getStationFromId()+"") && to.equals(list.get(i).getStationToId()+"")) {
                return list.get(i);
            }
        return null;
    }

	public List<BusStation> getBusStation(List<RouteElement> routeElements) throws DirectionException{
		List<BusStation> busStations= new ArrayList<>();
//		HashSet<Integer> stationIds= new HashSet<>();
		List<Integer> stationIds= new ArrayList<>();
//		when walk from A to B it will throw exception
		if(routeElements.size()<2){
			throw new DirectionException("No direction was found");
		}
		
		for (RouteElement routeElement : routeElements) {
			if (routeElement.getStationFromId() == -1) {
			}else if(routeElement.getStationToId() == 9999){
				stationIds.add(routeElement.getStationFromId());
			}else{
				stationIds.add(routeElement.getStationFromId());
			}
		}
		
		System.out.println("debug: stationIds size is"+stationIds.size());
		for (Integer id : stationIds) {
			for (BusStation busStation1 : bStations) {
				if (id.intValue() == busStation1.getId().intValue()) {
//					System.out.println("debug: busTation id "+busStation1.getBusList().get(0).getId());
					busStations.add(busStation1);
					break;
				}
			}
		}
		busStations.add(0,oriDestiBusStation.get(0));
		busStations.add(oriDestiBusStation.get(1));
		return busStations;
	}

	public List<RouteElement> directInSideBar(String originAddress, String destinationAddress, int maxBusRoute) throws OriginNearlyException,DestiNearbyException,DirectionException{

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

//		int lengthRouteElementsOriDesti = routeElementsWithOriginDestination.size();
//		for (int i = 1; i < lengthRouteElementsOriDesti; i++) {
//			int source = 0, des = 0;
//			for (Vertex v : vertexes) {
//				if (v.getId() == routeElementsWithOriginDestination.get(i).getStationFromId())
//					source = vertexes.indexOf(v);
//				if (v.getId() == routeElementsWithOriginDestination.get(i).getStationToId())
//					des = vertexes.indexOf(v);
//			}
//			edges.add(new Edge(i - 1 + "", vertexes.get(source), vertexes.get(des),
//					routeElementsWithOriginDestination.get(i).getDistanceOnBus()
//							+ routeElementsWithOriginDestination.get(i).getDistanceWalking()));
//		}
//
//		Graph graph = new Graph(vertexes, edges);
//		DijkstraAlgorithm da = new DijkstraAlgorithm(graph);
//		da.execute(vertexes.get(0));
//		LinkedList<Vertex> path = da.getPath(vertexes.get(vertexes.size() - 1));
//		for (int i = 1; i < path.size(); i++) {
//			for (int j = 1; j < routeElementsWithOriginDestination.size(); j++) {
//				if (routeElementsWithOriginDestination.get(j).getStationFromId() == path.get(i - 1).getId()
//						&& routeElementsWithOriginDestination.get(j).getStationToId() == path.get(i).getId()) {
//					routeDirection.add(routeElementsWithOriginDestination.get(j));
//				}
//			}
//		}
		ArrayList<ArrayList<RouteElement>> direction = new ArrayList<>();
		List<List<String>> path = new ArrayList<>();
		com.guru.model.Graph newGraph = new com.guru.model.Graph();
		for(int i = 0, j = routeElementsWithOriginDestination.size(); i < j; i++) {
            newGraph.add(routeElementsWithOriginDestination.get(i).getStationFromId()+"", routeElementsWithOriginDestination.get(i).getStationToId()+"",
            		routeElementsWithOriginDestination.get(i).getDistanceWalking() + routeElementsWithOriginDestination.get(i).getDistanceOnBus());
        }
        NDirection directionAlgorithm = new NDirection();
        path = directionAlgorithm.findAllShortestPaths(newGraph, "-1", "9999");
        
        for (int i = 0, k = path.size(); i < k; i++) {
            ArrayList<RouteElement> dr = new ArrayList<>();
            for(int j = 0, m = path.get(i).size() - 1; j < m; j++) {
                dr.add(findRouteElement(routeElementsWithOriginDestination, path.get(i).get(j), path.get(i).get(j+1)));
            }
            direction.add(dr);
        }
        for(int k = 0, l = direction.size(); k < l; k++)
            if(direction.get(k).size() > 3)
                for(int i = 0, j = direction.get(k).size() - 3; i < j; i++) {
                    if(direction.get(k).get(i).getBusRoute() != null && direction.get(k).get(i+2).getBusRoute() != null
                            && direction.get(k).get(i).getBusRoute().equals(direction.get(k).get(i+2).getBusRoute())
                            && direction.get(k).get(i+1).getBusRoute() == null) {
                        direction.get(k).get(i+1).setBusRoute(direction.get(k).get(i).getBusRoute());
                    }
                }
        routeDirection.addAll(direction.get(0));
		
//		handle exception
		if(routeDirection.size() == 0  || routeDirection.size() == 2){
			throw new DirectionException("can't find the direction at map");
		}
		
		///// filter direction
//		routeDirection = this.minimizeDirection(maxBusRoute, routeDirection);
		routeDirection = (ArrayList<RouteElement>) this.modifiedDirection(routeDirection);
		return routeDirection;
	}
	public static void main(String[] args) {
		Direction direction = new Direction();
		List<RouteElement> routeElementDirection=null;
		try {
			routeElementDirection= direction.directInSideBar(" 455 hoàng diệu, đà nẵng","chợ cồn, đà nẵng", 5);
//			routeElementDirection = direction.directInSideBar("435 hoàng diệu, da nang","cầu rồng,da nang", 2);
//			routeElementDirection = direction.directInSideBar("435 hoàng diệu, da nang","cầu rồng,da nang", 2);
//			routeElementDirection = direction.directInSideBar("435 hoàng diệu, da nang","88 nguyễn văn thoại,đà nẵng", 2);
//			routeElementDirection = direction.directInMap2("435 hoàng diệu, da nang","88 nguyễn văn thoại,đà nẵng", 2);
			
//			routeElementDirection = direction.directInMap2("18 Ngô Quyền, Đà Nẵng, Việt Nam","20 Bà Huyện Thanh Quan, Mỹ An, Đà Nẵng, Việt Nam", 2);
//			routeElementDirection = direction.directInSideBar("18 Ngô Quyền, Đà Nẵng, Việt Nam","20 Bà Huyện Thanh Quan, Mỹ An, Đà Nẵng, Việt Nam", 18);
			
//			routeElementDirection = direction.directInMap2("5 Nguyễn Thông, Sơn Trà, Đà Nẵng, Việt Nam","Lê Tấn Trung, Sơn Trà, Đà Nẵng, Việt Nam",2);
//			routeElementDirection = direction.directInSideBar("18 Ngô Quyền, Đà Nẵng, Việt Nam","20 Bà Huyện Thanh Quan, Mỹ An, Đà Nẵng, Việt Nam", 18);
		} catch (OriginNearlyException e) {
			e.printStackTrace();
		}catch(DirectionException  e){
			e.printStackTrace();
		}catch(DestiNearbyException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("list route element");
		for (RouteElement routeElement : routeElementDirection) {
			System.out.println(routeElement.toString());
		}
		
//		direct in map
		System.out.println("debug: getBusStation ");
		List<BusStation> getBusStation=direction.getBusStation(routeElementDirection);
		for (BusStation busStation : getBusStation) {
			System.out.println(busStation.getId());
		}
		
		direction.oriDestiBusStation.clear();
		
	}
}
