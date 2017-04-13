package com.example;

import com.google.maps.model.Geometry;

public class TestNearlyPoint {
	public static void main(String[] args) {
		double lat1=16.095451;
		double lng1=108.146206;
		
		double lat2=16.084785;
		double lng2=108.158735;
		
		double lat3=16.08257;
		double lng3=108.157655;
		
		
		double result=distanceMelesLatLng(16.095451,108.146206,16.084785,108.158735);
//		System.out.println(result);
		
//		bStation1 to bStation2
		double result2=distanceTo(16.095451,108.146206,16.084785,108.158735);
		System.out.println(result2);
		
//		bStation2 to bStation3
		double result3=distanceTo(16.084785,108.158735,16.08257,108.157655);
		System.out.println(result3*1.609);
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
	
	 public static double distanceTo(double lat1, double lng1, double lat2, double lng2) {
	        double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	        
	        lat1 = Math.toRadians(lat1);
	        lng1 = Math.toRadians(lng1);
	        lat2 = Math.toRadians(lat2);
	        lng2 = Math.toRadians(lng2);

	        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
	                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng1 - lng2));

	        double nauticalMiles = 60 * Math.toDegrees(angle);
	        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	        return statuteMiles;
	    }
	 public static double distanceToTruman(double lat1, double lng1, double lat2, double lng2) {
	        double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	        
	        lat1 = Math.toRadians(lat1);
	        lng1 = Math.toRadians(lng1);
	        lat2 = Math.toRadians(lat2);
	        lng2 = Math.toRadians(lng2);

	        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
	                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng1 - lng2));

	        double nauticalMiles = 60 * Math.toDegrees(angle);
	        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	        return statuteMiles;
	    }
}
