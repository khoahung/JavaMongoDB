package com.khoahung.cmc.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mongodb.ConnectionString;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.MongoClientSettings;

import static com.mongodb.client.model.Aggregates.*;
@Component
public class CollectionDataDao {
	public ResponseEntity<String> getDataFromMongoDB(String json) {
		ConnectionString connString = new ConnectionString(
			    "mongodb://root:123123Abc@localhost:27018/admin?retryWrites=true&w=majority"
			);
			MongoClientSettings settings = MongoClientSettings.builder()
			    .applyConnectionString(connString)
			    .retryWrites(true)
			    .build();
			MongoClient mongoClient = MongoClients.create(settings);
			MongoDatabase database = mongoClient.getDatabase("rootdb");
			MongoCollection<Document> collection  = database.getCollection("khoahung");
			ObjectMapper mapper = new ObjectMapper();
			CollectionType javaType = mapper.getTypeFactory()
				      .constructCollectionType(List.class, Document.class);
			List<Document> listData=null;
			try {
				listData = mapper.readValue(json, javaType);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
					
		    for (Document dbObject : listData)
		    {
		    	String check = dbObject.getString("url");
		    	AggregateIterable<Document> interator = collection.aggregate(Arrays.asList(
					      match(Filters.eq("url",check))));
		    	if(interator==null) {
		    		collection.insertOne(dbObject);
		    	}else {
		    		System.out.println("Duplication data");
		    	}
		    }
	        return ResponseEntity.ok("Success");
	}
}
