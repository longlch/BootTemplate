package com.guru.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guru.exception.DestiNearbyException;
import com.guru.exception.DirectionException;
import com.guru.exception.OriginNearlyException;
import com.guru.model.Bus;
import com.guru.model.BusRoute;
import com.guru.model.BusStation;
import com.guru.model.RouteElement;
import com.guru.service.IMapService;
import com.guru.util.Direction;
import com.guru.util.IJsonUtil;;

@Controller
@RequestMapping(value = "/map")
public class MapController {
	@Autowired
	public IMapService serviceMap;

	// @Autowired
	public Direction direction = new Direction();

	@Autowired
	public IJsonUtil serviceJson;

	private static final Logger logger = LoggerFactory.getLogger(MapController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String home(Model model) {
		return "map_offical";
	}

	@RequestMapping(value = MapURL.BUS_ROUTE, method = RequestMethod.GET)
	public String busRouteInDetail(@PathVariable("id") int id, @RequestParam(value = "trend") String trend,
			Model model) {
		// get list bus station
		// send it into content_map.html
		String busRoute = serviceMap.getRouteName(id);
		List<BusStation> busStations = serviceJson.getBusStationFromBusRoute(id, trend);
		model.addAttribute("busRoute", busRoute);
		model.addAttribute("busStops", busStations);
		return "stations_map";
	}
	
	@RequestMapping(value = MapURL.BUS_ROUTE_DETAIL, method = RequestMethod.GET)
	public String busDetail(@PathVariable("id") int id, @RequestParam(value = "trend") String trend,
			Model model) {
		String busRoute = serviceMap.getRouteName(id);
		BusRoute busRouteInfo= serviceJson.getBusRouteDetail(id, trend);
		model.addAttribute("busRoute", busRoute);
		model.addAttribute("busRouteInfo", busRouteInfo);
		return "detail";
	}

	@RequestMapping(value = MapURL.BUS_STATIONS, method = RequestMethod.GET)
	public @ResponseBody List<BusStation> drawBusRoute(@RequestParam(value = "busRoute") String route,
			@RequestParam(value = "trend") String trend) {
		int id = Integer.parseInt(route);
		List<BusStation> busStations = serviceJson.getBusStationFromBusRoute(id, trend);
		return busStations;
	}

	@RequestMapping(value = MapURL.BUS_ROUTE_DIRECTION_DETAIL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<BusStation> directionInDetail(@RequestParam(value = "startPoint") String startPoint,
			@RequestParam(value = "endPoint") String endPoint,
			@RequestParam(value = "maxRoute")int maxRoute) throws DestiNearbyException,DirectionException,OriginNearlyException {
		/*startPoint = startPoint + ", Đà Nẵng";
		endPoint = endPoint + ", Đà Nẵng";*/
		List<RouteElement> routeElements = new ArrayList<>();
		List<BusStation> busStations= new ArrayList<>();
		routeElements.addAll(direction.directInMap2(startPoint,endPoint, maxRoute));
		// debug here
		logger.info("route size is "+routeElements.size());
		if(routeElements.size() !=0){
			busStations.clear();
			busStations=direction.getBusStation(routeElements);
//			direction.oriDestiBusStation.clear();
		}else{
			busStations= new ArrayList<>();
			double a = 12;
			busStations.add(new BusStation(1, "dfs", a, a, null));
		}
		return busStations;
	}

	@RequestMapping(value = MapURL.BUS_ROUTE_DIRECTION_SIDE_BAR, method = RequestMethod.GET)
	public String directionInSideBar(@RequestParam(value = "startPoint") String startPoint,
			@RequestParam(value = "endPoint") String endPoint,
			@RequestParam(value = "maxRoute")int maxRoute,Model model) throws DestiNearbyException,DirectionException,OriginNearlyException {

		List<RouteElement> routeElements = new ArrayList<>();
		routeElements.addAll(direction.directInSideBar(startPoint,endPoint, maxRoute));
		model.addAttribute("routeElements",routeElements);
		model.addAttribute("size",routeElements.size());
		model.addAttribute("maxRoute",maxRoute);
		return "direction_map";
	}
	
	@RequestMapping(value=MapURL.BUS_ROUTE_DRAW,method=RequestMethod.GET)
	public @ResponseBody String directionAjax(@RequestParam(value="busRoute")int id,
												@RequestParam(value="trend") String trend){
		String reponseJson="";
		reponseJson=serviceJson.drawPolyline(id, trend);
		return reponseJson;
	}
	
	@RequestMapping(value = MapURL.BUS_REALTIME, method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public String busRealTime(@RequestBody String data1,Model model) {
		logger.info("real time "+data1);
		JSONObject jsObj= new JSONObject(data1);
		String arr=jsObj.get("data").toString();
		JSONArray jsArr= new JSONArray(arr);
		List<Bus> buses= new ArrayList<>();
		for(int i=0;i<jsArr.length();i++){
			buses.add(new Bus(jsArr.getJSONObject(i).getString("busname"), 
					jsArr.getJSONObject(i).getString("busroute"),
					jsArr.getJSONObject(i).getBoolean("turn"),
					jsArr.getJSONObject(i).getInt("distance"),
					jsArr.getJSONObject(i).getInt("speed"),
					jsArr.getJSONObject(i).getString("time")));
		}
		logger.info(" size la "+buses.size());
		model.addAttribute("buses",buses);
		return "realtime";
	}
	
	
	

}
