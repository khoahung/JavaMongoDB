package com.khoahung.cmc.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientSettings;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
@Component
public class CollectionDataDao {
	public ResponseEntity<String> getDataFromMongoDB() {
		ConnectionString connString = new ConnectionString(
			    "mongodb://root:123123Abc@localhost:27017/admin?retryWrites=true&w=majority"
			);
			MongoClientSettings settings = MongoClientSettings.builder()
			    .applyConnectionString(connString)
			    .retryWrites(true)
			    .build();
			MongoClient mongoClient = MongoClients.create(settings);
			MongoDatabase database = mongoClient.getDatabase("rootdb");
			MongoCollection<Document> collection  = database.getCollection("khoahung");
			
			FindIterable<Document> iterDoc = collection.find();
			Iterator it = iterDoc.iterator();
			List<Document> list = new ArrayList();
			while (it.hasNext()) {
				list.add((Document)it.next());
			}
			
	        ObjectMapper obj = new ObjectMapper();
    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.APPLICATION_JSON);
    		RestTemplate restTemplate = new RestTemplate();
    		HttpEntity<String> requestEntity=null;
			try {
				requestEntity = new HttpEntity<String>(obj.writeValueAsString(list),headers);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		ResponseEntity<String> rateResponse = restTemplate.exchange("http://localhost:8082/getData", HttpMethod.POST, requestEntity,String.class);
	       
	        return ResponseEntity.ok("Success");
	}
}
