package com.guru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/map")
public class MapController {
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String home(Model model){
		model.addAttribute("haha","hios");
		return "map";
	}
}
