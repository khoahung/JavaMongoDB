package com.khoahung.cmc.application;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.Binary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahung.cmc.dao.LogProcessingDao;
import com.khoahung.cmc.entity.AssetsDataProperties;
import com.khoahung.cmc.entity.LogData;

public class CreateAssetsMongoDB extends Thread {
	final static Logger logger = Logger.getLogger(CreateAssetsMongoDB.class);
	Document doc;
	Properties properties;
	public CreateAssetsMongoDB(Document doc,Properties properties) {
		this.doc = doc ;
		this.properties = properties;
	}
	public void run(){ 		
		try {
			LogProcessingDao logProcessingDao = new LogProcessingDao();
			ObjectMapper objMapper = new ObjectMapper();
			
			AssetsDataProperties dp = new AssetsDataProperties();
			dp.setName(doc.getObjectId("_id").toHexString());
			dp.setType("mgnl:resource");
			dp.setPath("/"+doc.getObjectId("_id").toHexString());
			
			
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
			p3.setValues(Arrays.asList("Asset Create From MongoDB"));
			listP.add(p3);
			
			com.khoahung.cmc.entity.Properties p4 = new com.khoahung.cmc.entity.Properties();
			p4.setName("customeattribute");
			p4.setType("String");
			p4.setMultiple(false);
			p4.setValues(Arrays.asList("my custom value"));
			listP.add(p4);
			
			dp.setNodeProperties(listP);
			
			List<com.khoahung.cmc.entity.Properties> assetP = new ArrayList();
			com.khoahung.cmc.entity.Properties p5 = new com.khoahung.cmc.entity.Properties();
			p5.setName("jcr:data");
			p5.setType("Binary");
			p5.setMultiple(false);
			p5.setValues(Arrays.asList(new String(((Binary)doc.get("img_str")).getData())));
			assetP.add(p5);
			
			com.khoahung.cmc.entity.Properties p6 = new com.khoahung.cmc.entity.Properties();
			p6.setName("extension");
			p6.setType("String");
			p6.setMultiple(false);
			p6.setValues(Arrays.asList((String)doc.get("img_ext")));
			assetP.add(p6);
			
			com.khoahung.cmc.entity.Properties p7 = new com.khoahung.cmc.entity.Properties();
			p7.setName("fileName");
			p7.setType("String");
			p7.setMultiple(false);
			p7.setValues(Arrays.asList((String)doc.get("fn")));
			assetP.add(p7);
			
			com.khoahung.cmc.entity.Properties p8 = new com.khoahung.cmc.entity.Properties();
			p8.setName("jcr:mimeType");
			p8.setType("String");
			p8.setMultiple(false);
			p8.setValues(Arrays.asList("image/"+(String)doc.get("img_ext")));
			assetP.add(p8);
			
			dp.setAssetProperties(assetP);
			
			URL url = new URL(properties.getProperty("mangolia_url_okm"));
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
			int responseCode = conn.getResponseCode();
			logger.info("response code:"+responseCode);
			if (100 <= responseCode && responseCode <= 399) {
				logger.info("Sub node have id = "+doc.getObjectId("_id").toHexString()+" has copy");
				LogData log = new LogData();
				log.setRecordId(doc.getObjectId("_id").toHexString());
				logProcessingDao.save(log);
			} else {
				logger.error("Asset have id = "+doc.getObjectId("_id").toHexString()+" create Error");
			}
	        Thread.sleep(100);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
