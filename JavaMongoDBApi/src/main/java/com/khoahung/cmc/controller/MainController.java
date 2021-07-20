package com.khoahung.cmc.controller;

import java.io.BufferedWriter;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahung.cmc.entity.Data;
import com.khoahung.cmc.service.CollectionDataService;

@RestController
public class MainController {

	@Autowired
	CollectionDataService service;

	@Autowired
	private HttpServletRequest context;

	@RequestMapping(value = "/getData", method = RequestMethod.POST)
	public @ResponseBody String getEmployees() {

		try {
			ObjectInputStream obj = new ObjectInputStream(context.getInputStream());
			Data data = (Data) obj.readObject();
			return service.getDataFromMongo(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Not get data base from client";

	};

	@RequestMapping(value = "/getFile", method = RequestMethod.POST)
	public @ResponseBody String uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			save(file);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return message;
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return message;
		}
	}
	
	public void save(MultipartFile file) {
		try {
			Path filepath = Paths.get("D:\\project\\Example\\JavaMongoDB\\DestFile", file.getOriginalFilename());
			OutputStream os = Files.newOutputStream(filepath);
	        os.write(file.getBytes());
	    }catch(Exception ex) {
	    	ex.printStackTrace();
	    }
	}
}
