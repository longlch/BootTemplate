package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;


public class TestMatrixGoogleMapJavaCilent {
	public static void main(String[] args) throws ApiException, InterruptedException, IOException {
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCv7lDkj6Yd3cMbujJcHTKIo_AzLEga-7c");
		String[] origins = new String[] { "16.065248, 108.185853"};
		String[] destinations1 = new String[] { "16.067056, 108.207089",
												"16.052675, 108.217832" };
		DistanceMatrix matrix = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations1).await();
		System.out.println(matrix.rows[0].elements[0].distance.humanReadable);
		
	}
}
