package com.khoahung.cmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.khoahung.cmc.service.CollectionDataService;

@RestController
public class MainController {
	
	@Autowired
	CollectionDataService service;
	
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	public @ResponseBody String getEmployees() {
		return service.getDataFromMongo();
	}
}
