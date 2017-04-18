package com.example;

import java.io.IOException;
import java.util.Arrays;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.guru.model.BusStation;


public class TestMatrixGoogleMapJavaCilent {
	public static void main(String[] args) throws ApiException, InterruptedException, IOException {
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCv7lDkj6Yd3cMbujJcHTKIo_AzLEga-7c");
		String[] origins = new String[] { "135 cu chinh lan ,da nang"};
		String[] destinations1 = new String[] { "đội56, 108.207089",
												"16.052675, 108.217832" };
		String[] wayPoints= new String[]{"466 le duan,đà nẵng","23 phan thanh,đà nẵng"};
		
//		Distance matrix
		/*DistanceMatrix matrix = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations1).await();
		int count;
		for(int i=0;i<matrix.rows[0].elements.length;i++){
			count=(int)matrix.rows[0].elements[i].distance.inMeters;
			System.out.println(count);
		}*/
		
//		geocode 
		/*GeocodingResult[] results = GeocodingApi.geocode(context, "453 hoang dieu,da nang").await();
			System.out.println(results[0].geometry.location.lat);*/

//		direction with waypoints
	    DirectionsResult result = DirectionsApi.newRequest(context)
	    						.origin("135 cu chinh lan,da nang")
	    						.destination("435 hoang dieu,da nang")
	    						.waypoints(wayPoints).await();
	    
	    /*System.out.println(result.routes[0].overviewPolyline.getEncodedPath());*/
	    int resultLength=result.routes[0].legs.length;
	    DirectionsLeg dirLeg = null;
	    for(int i=0;i<resultLength;i++){
	    	if(i>0 && i<resultLength-1){
	    		dirLeg=result.routes[0].legs[i];
	    		System.out.println(dirLeg.startAddress+" to "+dirLeg.endAddress);
		    	System.out.println(dirLeg.startLocation+","+dirLeg.endLocation);
	    	}
	    	
	    }
	    
	}
	
	private static  String[] addElement(String[] strArr,String element){
		strArr=Arrays.copyOf(strArr, strArr.length+1);
		System.out.println(strArr.length);
		strArr[strArr.length-1]=element;
		System.out.println(strArr[0]);
		return strArr;
	}
}
