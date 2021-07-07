package com.khoahung.cmc.dao;

import java.util.HashSet;
import java.util.Set;

import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.springframework.stereotype.Component;

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
	public String getDataFromMongoDB() {
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
			
			Set<String> namesOfTroysKids = new HashSet<>();
	        namesOfTroysKids.add("Paul");
	        namesOfTroysKids.add("Jane");
	        namesOfTroysKids.add("Mark");
	        namesOfTroysKids.add("Ivona");

	        Document document = new Document("title", "MongoDB")
	        		.append("description", "database")
	        		.append("likes", 100)
	        		.append("url", "http://www.tutorialspoint.com/mongodb/")
	        		.append("by", "tutorials point");
	        		
	        //Inserting document into the collection
	        collection.insertOne(document);
	        System.out.println("Document inserted successfully");
	        
	        FindIterable<Document> findIt = collection.find(eq("name", "Troy")).projection(fields(include("kids")));
	        Document d = findIt.first();
	        return d.toJson();
	}
}
