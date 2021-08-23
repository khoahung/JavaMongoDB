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
				CreateAssetsOpenKM assetsThread = new  CreateAssetsOpenKM(open, properties);
				assetsThread.start();
		        Thread.sleep(100);		        
			}
			 logger.info("==============Finish copy data===================");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
