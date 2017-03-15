package com.guru.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.guru.model.BusStation;
import com.guru.service.IMapService;
import com.guru.service.MapServiceImpl;

@Controller
@RequestMapping(value="/map")
public class MapController {
//	@autowire
	public static IMapService serviceMap;
	
	@RequestMapping(method=RequestMethod.GET)
	public String home(Model model){
		serviceMap= new MapServiceImpl();
		List<BusStation> busStations= serviceMap.loadDummyMarker();
	
		model.addAttribute("busStations",busStations);
		return "map";
	}
	
	
}
