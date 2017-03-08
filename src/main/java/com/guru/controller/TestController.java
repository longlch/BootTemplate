package com.guru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.guru.model.Post1;


@Controller
@RequestMapping(value="test")
public class TestController {
	
	@RequestMapping(value="/1")
	public String TestObject(Post1 post){
		return "hello";
	}
}
