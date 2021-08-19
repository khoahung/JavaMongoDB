package com.khoahung.cmc.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahung.cmc.dao.LogProcessingDao;
import com.khoahung.cmc.entity.Data;
import com.khoahung.cmc.entity.DataProperties;
import com.khoahung.cmc.entity.LogData;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


class AgentMongoDB extends Thread{
	final static Logger logger = Logger.getLogger(AgentMongoDB.class);
	Properties properties;
	public AgentMongoDB(Properties p) {
		this.properties = p ;
	}
	public void run(){ 	
		LogProcessingDao logProcessingDao = new LogProcessingDao();
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
		ObjectMapper objMapper = new ObjectMapper();
		while(true) {
			 logger.info("==============Starting Migration database===================");
			try {					
					FindIterable<Document> iterDoc = collection.find();
					Iterator<Document> it = iterDoc.iterator();
					List<Document> list = new ArrayList<Document>();
					Set<String> keySet = new HashSet<String>();							
					Iterable<LogData> dataH2 = logProcessingDao.findAll();
					for (LogData item : dataH2) {
						keySet.add(item.getRecordId());
				    }
					
					while (it.hasNext()) {
						Document d = (Document)it.next();
						if(keySet.contains(d.getObjectId("_id").toHexString())) {
							 logger.info(d.getObjectId("_id").toHexString()+ " has synchronize");
							continue;
						}else {
							list.add(d);
						}
						if(list.size()>=500) {
							//send 1000 document to server;
							break;
						}
					}
					
					
					for(Document doc : list) {
						CreateAssetsMongoDB assetsThread = new CreateAssetsMongoDB(doc, properties);
						assetsThread.start();
				        Thread.sleep(100);
					}
			        if(list.size() != 0) {
			        	list = new ArrayList<Document>();
			        	Thread.sleep(100);
						continue;
					}
					 logger.info("==============Finish copy data===================");
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}