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
			    "mongodb+srv://root:123123a%40@localhost/rootdb"
			);
			MongoClientSettings settings = MongoClientSettings.builder()
			    .applyConnectionString(connString)
			    .retryWrites(true)
			    .build();
			MongoClient mongoClient = MongoClients.create(settings);
			MongoDatabase database = mongoClient.getDatabase("rootdb");
			MongoCollection<Document> collection  = database.getCollection("toys");
			
			Set<String> namesOfTroysKids = new HashSet<>();
	        namesOfTroysKids.add("Paul");
	        namesOfTroysKids.add("Jane");
	        namesOfTroysKids.add("Mark");
	        namesOfTroysKids.add("Ivona");

	        Document doc = new Document("name", "Troy").append("height", 185).append("kids", namesOfTroysKids);
	        collection.insertOne(doc);
	        
	        FindIterable<Document> findIt = collection.find(eq("name", "Troy")).projection(fields(include("kids")));
	        Document d = findIt.first();
	        return d.toJson();
	}
}
