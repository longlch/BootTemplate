package com.guru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/")
public class HomeController {
	
	/*@RequestMapping(method=RequestMethod.GET)
	public String home(@RequestParam(value="name",required=false,defaultValue="Hoang Long")String greeting,Model model){
		return "home";
	}*/
	
	@RequestMapping(method=RequestMethod.GET)
	public String home(){
		return "home";
	}
	
}
