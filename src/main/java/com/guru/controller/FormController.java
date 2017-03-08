package com.guru.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.guru.model.Post1;
import com.guru.repository.PostRepository;

@Controller
public class FormController {

	@Autowired
	PostRepository repository;
	
	@RequestMapping(value = "/form",method=RequestMethod.GET)
	public String processForm(Model model) {
		model.addAttribute("postModel",new Post1());
		return "form";
	}

	@RequestMapping(value = "/form",method=RequestMethod.POST)
	public String ResultForm(@Valid Post1 postModel, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "form";
		} else {
			model.addAttribute("post",postModel);
			repository.save(postModel);
			return "result";
		}
	}

}
