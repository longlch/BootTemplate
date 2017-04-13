package com.guru.util;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;

public interface IGoogleMapMatrixApi {
	
	DistanceMatrix getDistanceMatrixUser( String[] origins,String[]  destinations);
	GeocodingResult[] geocodeFromAddress(String address);
}
