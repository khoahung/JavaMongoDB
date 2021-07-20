package com.khoahung.cmc.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
		while(true) {
			System.out.println("==============Starting Migration database===================");
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
							System.out.println(d.getObjectId("_id").toHexString()+ " has synchronize");
							continue;
						}else {
							System.out.println("this id = "+d.getObjectId("_id").toHexString()+" has copy");
							LogData log = new LogData();
							log.setRecordId(d.getObjectId("_id").toHexString());
							logProcessingDao.save(log);
							list.add(d);
						}
					}
					if(list.size() == 0) {
						Thread.sleep(60000);
						continue;
					}
					Data data = new Data();
					data.setList(list);
					
					URL url = new URL("http://localhost:8083/getData");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					ObjectOutputStream  writer = new ObjectOutputStream (conn.getOutputStream());
					writer.writeObject(data);
					writer.flush();
					conn.connect();
					writer.close();
					BufferedReader br = null;
					if (100 <= conn.getResponseCode() && conn.getResponseCode() <= 399) {
					    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					} else {
					    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
					}
					System.out.println("response code:"+conn.getResponseCode());	
					String strCurrentLine;
			        while ((strCurrentLine = br.readLine()) != null) {
			               System.out.println(strCurrentLine);
			        }	
					System.out.println("==============Finish copy data===================");
					Thread.sleep(60000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}