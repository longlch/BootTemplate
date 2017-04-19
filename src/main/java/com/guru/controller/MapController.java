package com.guru.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guru.model.BusStation;
import com.guru.model.BusStop;
import com.guru.model.RouteElement;
import com.guru.service.IMapService;
import com.guru.util.Direction;
import com.guru.util.IJsonUtil;;


@Controller
@RequestMapping(value="/map")
public class MapController {
	@Autowired
	public IMapService serviceMap;
	
//	@Autowired
	public Direction direction = new Direction();
	
	@Autowired
	public IJsonUtil serviceJson;
	
	
	private static final Logger logger = LoggerFactory.getLogger(MapController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public String home(Model model){
		return "map_offical";
	}
	
	@RequestMapping(value=MapURL.BUS_ROUTE,method=RequestMethod.GET)
	public String busRouteInDetail(@PathVariable("id")String id,
									@RequestParam(value="trend")String trend,
									Model model){
		// get list bus station
		// send it into content_map.html
		String busRoute= serviceMap.getRouteName(id);
		int route=Integer.parseInt(id);
		List<BusStation> busStations=serviceJson.getBusStationFromBusRoute(route, trend);
		model.addAttribute("busRoute",busRoute);
		model.addAttribute("busStops",busStations);
		return "stations_map";
	}
	
	@RequestMapping(value=MapURL.BUS_STATIONS,method=RequestMethod.GET)
	public @ResponseBody List<BusStation> drawBusRoute(@RequestParam(value="busRoute")String route,
												@RequestParam(value="trend") String trend){
		int id=Integer.parseInt(route);
		logger.info("draw direction");
		List<BusStation> busStations=serviceJson.getBusStationFromBusRoute(id, trend);
		return busStations;
	}
	
	/*@RequestMapping(value=MapURL.BUS_ROUTE_DIRECTION_DETAIL,method=RequestMethod.GET)
	public @ResponseBody String directionInDetail(@RequestParam(value="startPoint")String startPoint,
													@RequestParam(value="endPoint")String endPoint){
		logger.info(startPoint+" "+endPoint);
		
		String reponseJson="";
		return reponseJson;
	}*/
	
	@RequestMapping(value=MapURL.BUS_ROUTE_DIRECTION_DETAIL,method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<BusStation> directionInDetail(@RequestParam(value="startPoint")String startPoint,
			@RequestParam(value="endPoint")String endPoint){
		logger.info(startPoint+" "+endPoint);
		List<BusStation> busStations= new ArrayList<>();
		double a=12;
		busStations.add(new BusStation(1,"dfs",a,a,null));
		return busStations;
	}
	
	@RequestMapping(value=MapURL.BUS_ROUTE_DIRECTION_SIDE_BAR,method=RequestMethod.GET)
	public String directionInSideBar(@RequestParam(value="startPoint")String startPoint,
			@RequestParam(value="endPoint")String endPoint){
		startPoint=startPoint+", da nang";
		endPoint=startPoint+", da nang";
		List<RouteElement> routeElements= direction.findDirection(startPoint, endPoint);
		
		return "direction_map";
	}
	
	
}
