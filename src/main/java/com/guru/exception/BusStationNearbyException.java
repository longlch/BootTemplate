package com.guru.exception;

public class BusStationNearbyException extends Exception{
	private static final long serialVersionUID = 4664456874499611218L;

	private String errorOriCode = "No_Bus_Station_Nearby_Origin";
	private String errorDestiCode2 = "No_Bus_Station_Nearby_Destination";
	
	public BusStationNearbyException() {
		super();
	}

	public BusStationNearbyException(String errorOriCode, String errorDestiCode2) {
		super();
		this.errorOriCode = errorOriCode;
		this.errorDestiCode2 = errorDestiCode2;
	}
	
}
