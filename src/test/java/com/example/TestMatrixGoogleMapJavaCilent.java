package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.guru.model.BusStation;


public class TestMatrixGoogleMapJavaCilent {
	public static void main(String[] args) throws ApiException, InterruptedException, IOException {
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCv7lDkj6Yd3cMbujJcHTKIo_AzLEga-7c");
		String[] origins = new String[] { "135 cu chinh lan ,da nang"};
		String[] destinations1 = new String[] { "16.067350, 108.207132",
												"16.052675, 108.217832" };
		String[] wayPoints= new String[]{
				"49 đường 3/2, Đà Nẵng",
				"20 Đống Đa, Đà Nẵng",
				"40 Lê Lợi, Đà Nẵng",
				"24 Trần Phú, Đà Nẵng",
				"2 Quang Trung, Đà Nẵng",
				"126 Lê Lợi, Đà Nẵng",
				"154 Lê Lợi, Đà Nẵng",
				"86 Hng Vương, Đà Nẵng",
				"106 Phan Châu Trinh, Đà Nẵng",
				"174 Phan Châu Trinh, Đà Nẵng",
				"391 Phan Châu Trinh, Đà Nẵng",
				"60 Núi Thành, Đà Nẵng",
				"161 Nguyễn Tất Thành, Đà nẵng",
				"62 Lê Thanh Nghị, Đà Nẵng",
				"108 Phan Đăng Lưu, Đà Nẵng",
				"Chợ đầu mối Hòa Cường Đà Nẵng", 
				"Siêu thị Metro, Đà Nẵng", 
				"64 Cách Mạng Tháng 8, Đà Nẵng", 
				"178 Cách Mạng Tháng 8, Đà Nẵng",
				"58 Ông Ích Đường, Đà Nẵng",
				"166 Ông Ích Đường, Đà Nẵng",
				"50 Phạm Hùng, Hòa Phước,Đà Nẵng",
				"182 Pham Hùng, Đà Nẵng",
				};
		
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
	  /*  DirectionsResult result = DirectionsApi.newRequest(context)
	    						.origin("Night Sky Hotel, Xuân Diệu,Hà Lam,Đà Nẵng")
	    						.destination("A199 (97) Phạm Hùng, Cẩm Lệ, Đà Nẵng")
	    						.waypoints(wayPoints).await();
	    
	    System.out.println(result.routes[0].overviewPolyline.getEncodedPath().toString());
	    List<LatLng> latLngs= new ArrayList<>();
	    int resultLength=result.routes[0].legs.length;
	    DirectionsLeg dirLeg = null;
	    for(int i=0;i<resultLength;i++){
	    		dirLeg=result.routes[0].legs[i];
	    		System.out.println(dirLeg.startAddress+" to "+dirLeg.endAddress);
		    	System.out.println(dirLeg.startLocation+","+dirLeg.endLocation);
	    }*/
		
//		direction with waypoint(3 points)
		DirectionsResult result = DirectionsApi.newRequest(context)
				.origin("135 cù chính lan,Đà Nẵng")
				.destination(destinations1[1])
				.waypoints(destinations1[0]).await();
		int resultLength=result.routes[0].legs.length;
		int dirLegLength=0;
		String polyLine="";
		String polLinTem="";
		String realPoly="";
		String[] realPoly2= {""};
		DirectionsLeg dirLeg=null;
		for(int i=0;i<resultLength;i++){
			dirLeg=result.routes[0].legs[i];
			dirLegLength=dirLeg.steps.length;
			System.out.println(dirLegLength);
			for(int j=0;j<dirLegLength;j++){
				polLinTem=dirLeg.steps[j].polyline.getEncodedPath();
				polyLine=polyLine+polLinTem;
				realPoly=realPoly+polyLine;
				polyLine="";
			}
		}
		System.out.println(realPoly);
		/*System.out.println("hihi "+realPoly2.toString());
		for(int i=0;i<realPoly2.length;i++){
			System.out.println(realPoly2[i]);
		}*/
	    
	}
	
	private static  String[] addElement(String[] strArr,String element){
		strArr=Arrays.copyOf(strArr, strArr.length+1);
		strArr[strArr.length-1]=element;
		return strArr;
	}
}
