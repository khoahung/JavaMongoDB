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


class AgentMangolia extends Thread{
	Properties properties;
	public AgentMangolia(Properties p) {
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
		int count = 0;
		ObjectMapper objMapper = new ObjectMapper();
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
							
							list.add(d);
						}
						if(list.size()>=500) {
							//send 1000 document to server;
							break;
						}
					}
					
					
					for(Document doc : list) {
						DataProperties dp = new DataProperties();
						dp.setName(doc.getObjectId("_id").toHexString());
						dp.setType("mgnl:asset");
						dp.setPath("/"+properties.getProperty("asset_name")+"/"+doc.getObjectId("_id").toHexString());
						
						List<com.khoahung.cmc.entity.Properties> listP = new ArrayList();
						com.khoahung.cmc.entity.Properties p1 = new com.khoahung.cmc.entity.Properties();
						p1.setName("type");
						p1.setType("String");
						p1.setMultiple(false);
						p1.setValues(Arrays.asList((String)doc.get("img_ext")));
						listP.add(p1);
						
						com.khoahung.cmc.entity.Properties p2 = new com.khoahung.cmc.entity.Properties();
						p2.setName("name");
						p2.setType("String");
						p2.setMultiple(false);
						p2.setValues(Arrays.asList(doc.getObjectId("_id").toHexString()));
						listP.add(p2);
						
						com.khoahung.cmc.entity.Properties p3 = new com.khoahung.cmc.entity.Properties();
						p3.setName("title");
						p3.setType("String");
						p3.setMultiple(false);
						p3.setValues(Arrays.asList("Asset create from MongoDB"));
						listP.add(p3);
						
						dp.setProperties(listP);
					
						URL url = new URL(properties.getProperty("mangolia_url") + properties.getProperty("asset_name"));
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						String credentials = properties.getProperty("username") + ":" + properties.getProperty("password");
						String basicAuth = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
						conn.setRequestProperty("Authorization", "Basic " +  basicAuth);
						
						conn.setRequestMethod("PUT");
						conn.setDoOutput(true);
						conn.setDoInput(true);
						conn.setConnectTimeout(60000); //60 secs
						conn.setReadTimeout(60000); //60 secs
						conn.setRequestProperty("Content-Type", "application/json");
						conn.setRequestProperty("Accept", "application/json");
					
						OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
						String json = objMapper.writeValueAsString(dp);
						writer.write(json);
						writer.flush();
						writer.close();
						BufferedReader br = null;
						int responseCode = conn.getResponseCode();
						System.out.println("response code:"+responseCode);
						if (100 <= responseCode && responseCode <= 399) {
							count += 1;
							System.out.println("Asset have id = "+doc.getObjectId("_id").toHexString()+" has copy");
							CopySubnodeMangolia subNode = new CopySubnodeMangolia(doc, properties);
							subNode.start();
						    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						} else {
						    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
						}
						
				        Thread.sleep(100);
					}
			        if(list.size() != 0) {
			        	list = new ArrayList<Document>();
			        	Thread.sleep(100);
						continue;
					}
					System.out.println("==============Finish copy data===================");
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}