package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;


public class TestMatrixGoogleMapJavaCilent {
	public static void main(String[] args) throws ApiException, InterruptedException, IOException {
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCv7lDkj6Yd3cMbujJcHTKIo_AzLEga-7c");
		String[] origins = new String[] { "135 cu chinh lan ,da nang"};
		String[] destinations1 = new String[] { "16.067056, 108.207089",
												"16.052675, 108.217832" };
		DistanceMatrix matrix = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations1).await();
		int count;
		for(int i=0;i<matrix.rows[0].elements.length;i++){
			count=(int)matrix.rows[0].elements[i].distance.inMeters;
			System.out.println(count);
		}
		
		/*GeocodingResult[] results = GeocodingApi.geocode(context, "453 hoang dieu,da nang").await();
			System.out.println(results[0].geometry.location.lat);*/
		
		/*String [] destination= {};
		destination=addElement(destination, "haha");
		System.out.println(destination[0]);*/
	}
	
	private static  String[] addElement(String[] strArr,String element){
		strArr=Arrays.copyOf(strArr, strArr.length+1);
		System.out.println(strArr.length);
		strArr[strArr.length-1]=element;
		System.out.println(strArr[0]);
		return strArr;
	}
}
