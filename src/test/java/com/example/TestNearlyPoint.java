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
		
		
		/*double diffLat = Math.abs(lat1 - lat2);
		double diffLng = Math.abs(lng1 - lng2);
		if (diffLng > 180) {
		    diffLng = 360 - diffLng;
		}
		double distanceSquared = diffLat * diffLat + diffLng * diffLng;
		System.out.println(distanceSquared);*/
		
		double diffLat = Math.abs(lat2 - lat3);
		double diffLng = Math.abs(lng2 - lng3);
		if (diffLng > 180) {
			diffLng = 360 - diffLng;
		}
		double distanceSquared = diffLat * diffLat + diffLng * diffLng;
		System.out.println(distanceSquared);
	}
	public static void getDistance(){
	}
}
