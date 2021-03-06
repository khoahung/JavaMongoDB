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

import org.apache.log4j.Logger;
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
	final static Logger logger = Logger.getLogger(Agent.class);
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
		int count = 0;
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
						for(Document d: list) {
							count += 1;
							logger.info("index = "+ count +" this id = "+d.getObjectId("_id").toHexString()+" has copy");
							LogData log = new LogData();
							log.setRecordId(d.getObjectId("_id").toHexString());
							logProcessingDao.save(log);
						}					    
					} else {
					    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
					}
					logger.info("response code:"+conn.getResponseCode());	
					String strCurrentLine;
			        while ((strCurrentLine = br.readLine()) != null) {
			               logger.info(strCurrentLine);
			        }	
			        if(list.size() != 0) {
			        	list = new ArrayList<Document>();
			        	Thread.sleep(5000);
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