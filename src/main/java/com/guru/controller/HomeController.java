package com.guru.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.guru.model.Bank;
import com.guru.model.Student;

@Controller
@RequestMapping(value="/")
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public String home(Student student){
		student.setName("long");
		student.getBanks().add(new Bank());
		logger.info("Bank size is "+student.getBanks().get(0).getId());
		return "home";
	}
	
	@RequestMapping(params={"addForm"})
	public String createForm(@ModelAttribute(value="student") Student student){
		student.getBanks().add(new Bank());
		return "home";
	}
	@RequestMapping(params={"removeForm"})
	public String remvoveForm(Student student, @RequestParam("removeForm")int rowId){
		student.getBanks().remove(rowId);
		return "home";
	}
}
