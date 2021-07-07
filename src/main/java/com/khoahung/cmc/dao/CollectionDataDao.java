package com.khoahung.cmc.dao;

import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.springframework.stereotype.Component;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientSettings;

@Component
public class CollectionDataDao {
	public String getDataFromMongoDB() {
		ConnectionString connString = new ConnectionString(
			    "mongodb+srv://root:123123a%40@localhost/rootdb?w=majority"
			);
			MongoClientSettings settings = MongoClientSettings.builder()
			    .applyConnectionString(connString)
			    .retryWrites(true)
			    .build();
			MongoClient mongoClient = MongoClients.create(settings);
			MongoDatabase database = mongoClient.getDatabase("rootdb");
			MongoCollection<Document> toys = database.getCollection("toys");
			final JsonWriterSettings set = JsonWriterSettings.builder( ).outputMode( JsonMode.SHELL ).build( );
			return toys.toJson(set)+"";
	}
}
