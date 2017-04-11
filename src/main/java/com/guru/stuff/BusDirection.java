package com.guru.stuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.maps.model.LatLng;
import com.guru.model.BusRoute;
import com.guru.model.BusStation;
import com.guru.model.BusStationDistance;
import com.guru.model.RouteElement;
import com.guru.model.WalkingPath;

public class BusDirection {
	private ArrayList<BusRoute> bRoute = new ArrayList<BusRoute>();
	private ArrayList<BusStation> bStation = new ArrayList<BusStation>();
	private ArrayList<BusStationDistance> bDistance = new ArrayList<BusStationDistance>();
	private ArrayList<WalkingPath> wPath = new ArrayList<WalkingPath>();

	public BusDirection(ArrayList<BusRoute> bRoute, ArrayList<BusStation> bStation,
			ArrayList<BusStationDistance> bDistance, ArrayList<WalkingPath> wPath) {
		this.bRoute = bRoute;
		this.bStation = bStation;
		this.bDistance = bDistance;
		this.wPath = wPath;
	}

	private static double distanceMelesLatLng(double lat1, double lng1, double lat2, double lng2) {
		double r = 3959;
		double dLat = (Math.PI / 180) * (lat1 - lat2);
		double dLng = (Math.PI / 180) * (lng1 - lng2);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos((Math.PI / 180) * lat1)
				* Math.cos((Math.PI / 180) * lat2) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * r * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return c;
	}

	private static double distanceMelesToKilometer(double meles) {
		return meles * 1.609344;
	}

	private static double distanceKilometerToMeter(double kilometer) {
		return kilometer * 1000;
	}

	private static int distanceFromLatLng(double lat1, double lng1, double lat2, double lng2) {
		return (int) distanceKilometerToMeter(distanceMelesToKilometer(distanceMelesLatLng(lat1, lng1, lat2, lng2)));
	}

	public ArrayList<RouteElement> findDirection(LatLng origin, LatLng destination) {
		ArrayList<RouteElement> direction = new ArrayList<RouteElement>();
		List<Vertex> vertexes = new ArrayList<Vertex>();
		List<Edge> edges = new ArrayList<Edge>();

		ArrayList<RouteElement> list = new ArrayList<RouteElement>();
		list.addAll(this.createGraph(origin, destination, bStation, bRoute, bDistance, wPath));
		// Log.d("Graph", list.toString());

		// create vertexes from bus station
		vertexes.add(new Vertex(-1, "Origin", origin.lat, origin.lng));
		for (BusStation bs : bStation) {
			vertexes.add(new Vertex(bs.getId(), bs.getName(), bs.getLat(), bs.getLng()));
		}
		vertexes.add(new Vertex(999, "Destination", destination.lat, destination.lng));
		// create edges from distance list
		for (int i = 1; i < list.size(); i++) {
			int source = 0, des = 0;
			for (Vertex v : vertexes) {
				if (v.getId() == list.get(i).getStationFromId())
					source = vertexes.indexOf(v);
				if (v.getId() == list.get(i).getStationToId())
					des = vertexes.indexOf(v);
			}
			edges.add(new Edge(i - 1 + "", vertexes.get(source), vertexes.get(des),
					list.get(i).getDistanceOnBus() + list.get(i).getDistanceWalking()));
		}

		Graph graph = new Graph(vertexes, edges);
		DijkstraAlgorithm da = new DijkstraAlgorithm(graph);
		da.execute(vertexes.get(0));
		LinkedList<Vertex> path = da.getPath(vertexes.get(vertexes.size() - 1));

		direction.add(list.get(0));
		for (int i = 1; i < path.size(); i++) {
			for (int j = 1; j < list.size(); j++) {
				if (list.get(j).getStationFromId() == path.get(i - 1).getId()
						&& list.get(j).getStationToId() == path.get(i).getId()) {
					direction.add(list.get(j));
				}
			}
		}
		return direction;
	}

	private ArrayList<RouteElement> createGraph(LatLng origin, LatLng destination, ArrayList<BusStation> bStation,
			ArrayList<BusRoute> bRoute, ArrayList<BusStationDistance> bDistance, ArrayList<WalkingPath> wPath) {

		ArrayList<RouteElement> rElement = new ArrayList<RouteElement>();
		rElement.addAll(createGraphElementFromOrigin(origin));
		for (int i = 0; i < bDistance.size(); i++) {
			rElement.add(new RouteElement(bDistance.get(i).getStationFromId(), bDistance.get(i).getStationToId(), 0,
					bDistance.get(i).getDistance(), bDistance.get(i).getBusRoute()));
		}
		for (int i = 0; i < wPath.size(); i++) {
			rElement.add(new RouteElement(wPath.get(i).getStationFromId(), wPath.get(i).getStationToId(),
					wPath.get(i).getDistance(), 0, null));
		}
		rElement.addAll(createGraphElementToDestination(destination));
		return rElement;
	}
	
	 private ArrayList<RouteElement> createGraphElementFromOrigin(LatLng origin) {
	        ArrayList<RouteElement> nearOrigin = new ArrayList<RouteElement>();
//	        create origin point
	        nearOrigin.add(new RouteElement(-1, -1, 0, 0, null));

	        IGoogleDistanceApi clientDistanceGG = GoogleDistanceApiClient.getDistanceService();
	        Call<GoogleDistanceApi> ggDistanceR;
	        String destinationString = "";
	        int count25 = 0;
	        ArrayList<Integer> listIdCollected = new ArrayList<Integer>();
	        for(int i=0; i<bStation.size(); i++) {
	            if((count25==24 && i<bStation.size()-1) || (i==bStation.size()-1 && count25<=24)) {
//					System.out.println("destination startpoint "+count25+": "+destinationString);
	                ggDistanceR = clientDistanceGG.getDistance(origin.lat+","+origin.lng, destinationString.substring(0, destinationString.length()-1));

	                ArrayList<Element> resultElements = new ArrayList<Element>();
	                try {
	                    resultElements.addAll(ggDistanceR.execute().body().getRows().get(0).getElements());
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }

	                for(int r=0; r<resultElements.size(); r++) {
	                    int distance = resultElements.get(r).getDistance().getValue();
	                    RouteElement re = new RouteElement(-1, listIdCollected.get(r), distance, 0, null);
	                    nearOrigin.add(re);
	                }
	                count25=-1;
	                destinationString="";
	                listIdCollected.clear();
	            }
	            int cal = 0;
	            cal = distanceFromLatLng(origin.latitude, origin.longitude, bStation.get(i).getLat(), bStation.get(i).getLng());
	            if(cal<1000) {
	                destinationString = destinationString+bStation.get(i).getLat()+","+bStation.get(i).getLng()+"|";
	                count25++;
	                listIdCollected.add(bStation.get(i).getId());
	            }
//				System.out.println("count25: "+count25);
	        }
	        return nearOrigin;
	    }

	private ArrayList<RouteElement> createGraphElementToDestination(LatLng destination) {
		ArrayList<RouteElement> nearDestination = new ArrayList<RouteElement>();

		IGoogleDistanceApi clientDistanceGG = GoogleDistanceApiClient.getDistanceService();
		Call<GoogleDistanceApi> ggDistanceR;
		String destinationString = "";
		int count25 = 0;
		ArrayList<Integer> listIdCollected = new ArrayList<Integer>();
		for (int i = 0; i < bStation.size(); i++) {
			if ((count25 == 24 && i < bStation.size() - 1) || (i == bStation.size() - 1 && count25 <= 24)) {
				// System.out.println("destination endpoint "+count25+":
				// "+destinationString);
				ggDistanceR = clientDistanceGG.getDistance(destination.latitude + "," + destination.longitude,
						destinationString.substring(0, destinationString.length() - 1));

				ArrayList<Element> resultElements = new ArrayList<Element>();
				try {
					resultElements.addAll(ggDistanceR.execute().body().getRows().get(0).getElements());
				} catch (IOException e) {
					System.out.println(e.toString());
				}

				for (int r = 0; r < resultElements.size(); r++) {
					int distance = resultElements.get(r).getDistance().getValue();
					RouteElement re = new RouteElement(listIdCollected.get(r), 999, distance, 0, null);
					nearDestination.add(re);
				}
				count25 = -1;
				destinationString = "";
				listIdCollected.clear();
			}
			int cal = 0;
			cal = distanceFromLatLng(destination.latitude, destination.longitude, bStation.get(i).getLat(),
					bStation.get(i).getLng());
			if (cal < 1000) {
				destinationString = destinationString + bStation.get(i).getLat() + "," + bStation.get(i).getLng() + "|";
				count25++;
				listIdCollected.add(bStation.get(i).getId());
			}
		}
		return nearDestination;
	}
}
