package com.khoahung.cmc.controller;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahung.cmc.service.CollectionDataService;

@RestController
public class MainController {
	
	@Autowired
	CollectionDataService service;
	
	@RequestMapping(value = "/getData", method = RequestMethod.POST)
	public ResponseEntity<String> getEmployees(@RequestBody List<Document> json) {
		ObjectMapper obj = new ObjectMapper();
		try {
			return service.getDataFromMongo(obj.writeValueAsString(json));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error");
	}
}
