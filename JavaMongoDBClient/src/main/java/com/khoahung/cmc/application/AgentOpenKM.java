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
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahung.cmc.dao.LogOpenKMDao;
import com.khoahung.cmc.dao.OpenKMDao;
import com.khoahung.cmc.entity.DataProperties;
import com.khoahung.cmc.entity.LogData;
import com.khoahung.cmc.entity.LogOpenKM;
import com.khoahung.cmc.entity.OpenKM;

public class AgentOpenKM extends Thread{
	final static Logger logger = Logger.getLogger(AgentOpenKM.class);
	Properties properties;
	public AgentOpenKM(Properties p) {
		this.properties = p ;
	}
	public void run(){ 	
		OpenKMDao openKMDao = new OpenKMDao();
		LogOpenKMDao logOpenKMDao = new LogOpenKMDao();
		ObjectMapper objMapper = new ObjectMapper();
		try{
			List<OpenKM> listData = openKMDao.findAll();
			Set<String> keySet = new HashSet<String>();							
			Iterable<LogOpenKM> logList = logOpenKMDao.findAll();
			for (LogOpenKM item : logList) {
				keySet.add(item.getOkm_hdpath());
		    }
			for(OpenKM open: listData) {
				if(keySet.contains(open.getOkm_hdpath())) {
					 logger.info(open.getOkm_hdpath()+ " has synchronize");
					continue;
				}
				DataProperties dp = new DataProperties();
				dp.setName(open.getOkm_hdpath());
				dp.setType("mgnl:asset");
				dp.setPath("/"+properties.getProperty("asset_name")+"/"+open.getOkm_hdpath());
				
				List<com.khoahung.cmc.entity.Properties> listP = new ArrayList();
				com.khoahung.cmc.entity.Properties p1 = new com.khoahung.cmc.entity.Properties();
				p1.setName("type");
				p1.setType("String");
				p1.setMultiple(false);
				p1.setValues(Arrays.asList(open.getFile_name().split("\\.")[1]));
				listP.add(p1);
				
				com.khoahung.cmc.entity.Properties p2 = new com.khoahung.cmc.entity.Properties();
				p2.setName("name");
				p2.setType("String");
				p2.setMultiple(false);
				p2.setValues(Arrays.asList(open.getOkm_hdpath()));
				listP.add(p2);
				
				com.khoahung.cmc.entity.Properties p3 = new com.khoahung.cmc.entity.Properties();
				p3.setName("title");
				p3.setType("String");
				p3.setMultiple(false);
				p3.setValues(Arrays.asList("Asset create from OpenKM"));
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
				int responseCode = conn.getResponseCode();
				logger.info("response code:"+responseCode);
				if (100 <= responseCode && responseCode <= 399) {
					logger.info("Asset have id = "+open.getOkm_hdpath()+" has copy");
					CopySubNodeOpenKM subNode = new CopySubNodeOpenKM(open, properties);
					subNode.start();
				}else {
					logger.error("Asset have id = "+open.getOkm_hdpath()+" create Error");
				} 
		        Thread.sleep(100);		        
			}
			 logger.info("==============Finish copy data===================");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
