package com.guru.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guru.model.PersonTest;
import com.guru.restcontroller.EmployeeRestController;

@Controller
@RequestMapping(value = "ajax")
public class AjaxTestController {
	public List<PersonTest> persons = new ArrayList<>();
	private static final Logger logger = LoggerFactory.getLogger(AjaxTestController.class);
	@RequestMapping(method = RequestMethod.GET)
	public String home() {
		logger.info("ajax home");
		return "ajax_home";
	}
	@RequestMapping(value = "/person/new", method = RequestMethod.GET)
	public @ResponseBody String addPerson(@RequestParam(value = "name") String name,
											@RequestParam(value = "age") String age) {
		logger.info("ajax has come");
		PersonTest person= new PersonTest(name, age);
		persons.add(person);
		ObjectMapper mapper= new ObjectMapper();
		String ajaxReponse="";
		try {
			ajaxReponse=mapper.writeValueAsString(person);
			logger.info("response la"+ajaxReponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return ajaxReponse;
	}
	
	@RequestMapping(value = "/persons", method = RequestMethod.GET)
	public @ResponseBody String allPerson(){
		logger.info("get all person");
		String reponse="";
		ObjectMapper mapper= new ObjectMapper();
		try {
			reponse=mapper.writeValueAsString(persons);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return reponse;
	}

}
