package com.khoahung.cmc.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.khoahung.cmc.dao.LogProcessingDao;
import com.khoahung.cmc.entity.Data;
import com.khoahung.cmc.entity.LogData;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


class Agent extends Thread{
	
	public void run(){ 	
		LogProcessingDao logProcessingDao = new LogProcessingDao();
		while(true) {
			System.out.println("==============Starting Migration database===================");
			try {
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
					Iterator<Document> it = iterDoc.iterator();
					List<Document> list = new ArrayList<Document>();
					Set<String> keySet = new HashSet<String>();							
					Iterable<LogData> dataH2 = logProcessingDao.findAll();
					for (LogData item : dataH2) {
						keySet.add(item.getRecordId());
				    }
					while (it.hasNext()) {
						Document d = (Document)it.next();
						if(keySet.contains(d.get("_id").toString())) {
							System.out.println(d.get("_id")+ " has synchronize");
							continue;
						}else {
							System.out.println("this id = "+d.get("_id")+" has copy");
							LogData log = new LogData();
							log.setRecordId(d.get("_id").toString());
							logProcessingDao.save(log);
							list.add(d);
						}
					}
					Data data = new Data();
					data.setList(list);
					
					RestTemplate restTemplate = new RestTemplate();
					
			        HttpHeaders headers = new HttpHeaders();
			        headers.set("X-COM-PERSIST", "true");    
			        headers.set("X-COM-LOCATION", "USA");
			        
		    		
		    		HttpEntity<Data> requestEntity = new HttpEntity<>(data, headers);
		    		ResponseEntity<String> rateResponse = null;
					try {
						rateResponse = restTemplate.postForEntity(new URI("http://localhost:8082/getData"),requestEntity,String.class);
					} catch (RestClientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(rateResponse.getBody());	
					System.out.println("==============Finish copy data===================");
					Thread.sleep(60000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}