package com.khoahung.cmc.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import com.khoahung.cmc.entity.DataProperties;
import com.khoahung.cmc.entity.LogData;

public class CopySubNodeMangolia extends Thread{
	final static Logger logger = Logger.getLogger(CopySubNodeMangolia.class);
	Document doc;
	Properties properties;
	public CopySubNodeMangolia(Document doc,Properties properties) {
		this.doc = doc ;
		this.properties = properties;
	}
	public void run(){ 		
		try {
			LogProcessingDao logProcessingDao = new LogProcessingDao();
			ObjectMapper objMapper = new ObjectMapper();
			
			DataProperties dp = new DataProperties();
			dp.setName("jcr:content");
			dp.setType("mgnl:resource");
			dp.setPath("/"+properties.getProperty("asset_name")+"/"+doc.getObjectId("_id").toHexString()+"/jcr:content");
			
			List<com.khoahung.cmc.entity.Properties> listP = new ArrayList();
			com.khoahung.cmc.entity.Properties p1 = new com.khoahung.cmc.entity.Properties();
			p1.setName("jcr:data");
			p1.setType("Binary");
			p1.setMultiple(false);
			p1.setValues(Arrays.asList(new String(((Binary)doc.get("img_str")).getData())));
			listP.add(p1);
			
			com.khoahung.cmc.entity.Properties p2 = new com.khoahung.cmc.entity.Properties();
			p2.setName("extension");
			p2.setType("String");
			p2.setMultiple(false);
			p2.setValues(Arrays.asList((String)doc.get("img_ext")));
			listP.add(p2);
			
			com.khoahung.cmc.entity.Properties p3 = new com.khoahung.cmc.entity.Properties();
			p3.setName("fileName");
			p3.setType("String");
			p3.setMultiple(false);
			p3.setValues(Arrays.asList((String)doc.get("fn")));
			listP.add(p3);
			
			com.khoahung.cmc.entity.Properties p4 = new com.khoahung.cmc.entity.Properties();
			p4.setName("jcr:mimeType");
			p4.setType("String");
			p4.setMultiple(false);
			p4.setValues(Arrays.asList("image/"+(String)doc.get("img_ext")));
			listP.add(p4);
			
			dp.setProperties(listP);
			
			URL url = new URL(properties.getProperty("mangolia_url") + properties.getProperty("asset_name") +"/"+
					doc.getObjectId("_id").toHexString());
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
