package com.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class TestGoogleApi {
//	@autowire
	static GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAB2LvImfNA4Z4iZ_Rib15YsLYJR5AOqO8");
	
	public static void main(String[] args) throws Exception {
		System.out.println("get long and lat by geo code");
//		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAB2LvImfNA4Z4iZ_Rib15YsLYJR5AOqO8");
		GeocodingResult[] results = GeocodingApi.geocode(context, "453 hoang dieu,da nang").await();
		System.out.println(results[0].geometry.location.lat);
		System.out.println(results[0].geometry.location.lng);
		
	}
	private static void testTextSearchGoogleAPI(){
		RestTemplate restTemplate = new RestTemplate();
		String jsonString=restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?query=bus+station&location=16.065599,108.184212&radius=10000&key=AIzaSyC2qVKwmfiNY6DNyIIFOqZ4HTJYJ_E1pz4", String.class);
		JSONObject jsonObject= new JSONObject(jsonString);
		JSONArray jsonArray= jsonObject.getJSONArray("results");
		for(int i=0;i<jsonArray.length();i++){
			System.out.println(jsonArray.getJSONObject(i).get("name"));
		}
	}
	


}

