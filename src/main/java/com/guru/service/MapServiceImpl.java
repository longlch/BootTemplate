package com.guru.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guru.model.BusStop;
@Service
public class MapServiceImpl implements IMapService{


	@Override
	public String findBusRoute(String route,String trend) {
		JSONParser parser = new JSONParser();
		Resource resource= new ClassPathResource("static/bus_route/"+route+"_"+trend+".js");
		String jsonString="";
		try {
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file.toString()));
			// It will convert array's obj to array's json base string by toString() method
//			class import org.json.simple.parser.JSONParser;
			jsonString = obj.toString();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
//		JsonArray obj doesn't change after using toString() method
//		The toString() method returns the string representation of the object.
		return jsonString;
	}

	@Override
	public List<BusStop> getBusStops(String route, String trend) {
		Resource resource= new ClassPathResource("static/bus_route/"+route+"_"+trend+".js");
		ObjectMapper mapper = new ObjectMapper();
		List<BusStop> busStops=null;
		try {
			File file= resource.getFile();
			busStops=mapper.readValue(new File(file.toString()), mapper.getTypeFactory().constructCollectionType(List.class, BusStop.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return busStops;
	}

	@Override
	public String getRouteName(int id) {
		switch (id) {
		case 5:return "Tuyến số 5: Nguyễn Tất Thành- Xuân Diệu ";
		case 7:return "Tuyến số 7: Xuân Diệu- Metro";
		case 8:return "Tuyến số 8: Thọ Quang- Phạm Hùng ";
		case 11:return "Tuyến số 11: Xuân Diệu- Lotte Mart ";
		case 12:return "Tuyến số 12: Thọ Quang- Trường Sa";
		}
		return null;
	}
	
	

}
