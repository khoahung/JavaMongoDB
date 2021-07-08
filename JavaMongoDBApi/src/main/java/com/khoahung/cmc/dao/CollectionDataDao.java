package com.khoahung.cmc.dao;

import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.khoahung.cmc.entity.Data;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientSettings;

import static com.mongodb.client.model.Filters.eq;
@Component
public class CollectionDataDao {
	
	public ResponseEntity<String> getDataFromMongoDB(Data data) {
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
			List<Document> listData=data.getList();
			int count=0;
		    for (Document dbObject : listData)
		    {
		    	Document document = collection.find(eq("_id", dbObject.get("_id"))).first();
		    	if (document == null) {
		    		count++;
		    		collection.insertOne(dbObject);
		    	}
		    }

		    return ResponseEntity.ok("Success inserted: "+ count+" document");
	}
	
	
}
