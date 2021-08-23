package com.khoahung.cmc.application;

import java.io.File;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahung.cmc.dao.LogOpenKMDao;
import com.khoahung.cmc.entity.AssetsDataProperties;
import com.khoahung.cmc.entity.LogOpenKM;
import com.khoahung.cmc.entity.OpenKM;

public class CreateAssetsOpenKM extends Thread{
	final static Logger logger = Logger.getLogger(CreateAssetsOpenKM.class);
	OpenKM open;
	Properties properties;
	public CreateAssetsOpenKM(OpenKM open,Properties properties) {
		this.open = open ;
		this.properties = properties;
	}
	public void run(){ 	
		try {
			LogOpenKMDao logOpenKMDao = new LogOpenKMDao();
			ObjectMapper objMapper = new ObjectMapper();
			AssetsDataProperties dp = new AssetsDataProperties();
			dp.setName(open.getOkm_hdpath());
			dp.setType("mgnl:asset");
			dp.setPath("/"+open.getOkm_hdpath());
			
			List<com.khoahung.cmc.entity.Properties> listP = new ArrayList();
			com.khoahung.cmc.entity.Properties p1 = new com.khoahung.cmc.entity.Properties();
			p1.setName("name");
			p1.setType("String");
			p1.setMultiple(false);
			p1.setValues(Arrays.asList(open.getOkm_hdpath()));
			listP.add(p1);
			
			com.khoahung.cmc.entity.Properties p2 = new com.khoahung.cmc.entity.Properties();
			p2.setName("type");
			p2.setType("String");
			p2.setMultiple(false);
			p2.setValues(Arrays.asList(open.getFile_name().split("\\.")[1]));
			listP.add(p2);
			
			com.khoahung.cmc.entity.Properties p3 = new com.khoahung.cmc.entity.Properties();
			p3.setName("title");
			p3.setType("String");
			p3.setMultiple(false);
			p3.setValues(Arrays.asList("Asset create from OpenKM"));
			listP.add(p3);
			
			com.khoahung.cmc.entity.Properties p4 = new com.khoahung.cmc.entity.Properties();
			p4.setName("customeattribute");
			p4.setType("String");
			p4.setMultiple(false);
			p4.setValues(Arrays.asList("my custom value"));
			listP.add(p4);
			
			com.khoahung.cmc.entity.Properties p5 = new com.khoahung.cmc.entity.Properties();
			p5.setName("user_name");
			p5.setType("String");
			p5.setMultiple(false);
			p5.setValues(Arrays.asList(open.getUser_name()));
			listP.add(p5);
			
			com.khoahung.cmc.entity.Properties p6 = new com.khoahung.cmc.entity.Properties();
			p6.setName("real_name");
			p6.setType("String");
			p6.setMultiple(false);
			p6.setValues(Arrays.asList(open.getReal_name()));
			listP.add(p6);
			
			com.khoahung.cmc.entity.Properties p7 = new com.khoahung.cmc.entity.Properties();
			p7.setName("first_name");
			p7.setType("String");
			p7.setMultiple(false);
			p7.setValues(Arrays.asList(open.getFirst_name()));
			listP.add(p7);
			
			com.khoahung.cmc.entity.Properties p8 = new com.khoahung.cmc.entity.Properties();
			p8.setName("middle_name");
			p8.setType("String");
			p8.setMultiple(false);
			p8.setValues(Arrays.asList(open.getMiddle_name()));
			listP.add(p8);
			
			com.khoahung.cmc.entity.Properties p9 = new com.khoahung.cmc.entity.Properties();
			p9.setName("last_name");
			p9.setType("String");
			p9.setMultiple(false);
			p9.setValues(Arrays.asList(open.getLast_name()));
			listP.add(p9);
			
			com.khoahung.cmc.entity.Properties p10 = new com.khoahung.cmc.entity.Properties();
			p10.setName("mon_name");
			p10.setType("String");
			p10.setMultiple(false);
			p10.setValues(Arrays.asList(open.getMon_name()));
			listP.add(p10);
			
			com.khoahung.cmc.entity.Properties p11 = new com.khoahung.cmc.entity.Properties();
			p11.setName("mobile");
			p11.setType("String");
			p11.setMultiple(false);
			p11.setValues(Arrays.asList(open.getMobile()));
			listP.add(p11);
			
			com.khoahung.cmc.entity.Properties p12 = new com.khoahung.cmc.entity.Properties();
			p12.setName("email");
			p12.setType("String");
			p12.setMultiple(false);
			p12.setValues(Arrays.asList(open.getEmail()));
			listP.add(p12);
			
			dp.setNodeProperties(listP);
			
			List<com.khoahung.cmc.entity.Properties> assetsP = new ArrayList();
			
			com.khoahung.cmc.entity.Properties p13 = new com.khoahung.cmc.entity.Properties();
			p13.setName("jcr:data");
			p13.setType("Binary");
			p13.setMultiple(false);
			p13.setValues(Arrays.asList(getFileImage(open.getOkm_hdpath())));
			assetsP.add(p13);
			
			com.khoahung.cmc.entity.Properties p14 = new com.khoahung.cmc.entity.Properties();
			p14.setName("fileName");
			p14.setType("String");
			p14.setMultiple(false);
			p14.setValues(Arrays.asList(open.getFile_name()));
			assetsP.add(p14);
			
			com.khoahung.cmc.entity.Properties p15 = new com.khoahung.cmc.entity.Properties();
			p15.setName("jcr:mimeType");
			p15.setType("String");
			p15.setMultiple(false);
			p15.setValues(Arrays.asList("image/"+open.getFile_name().split("\\.")[1]));
			assetsP.add(p15);
			
			dp.setAssetProperties(assetsP);
			
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
				logger.info("Assets node have id = "+open.getOkm_hdpath()+" has copy");
				LogOpenKM log = new LogOpenKM();
				log.setOkm_hdpath(open.getOkm_hdpath());
				logOpenKMDao.save(log);
			}else {
				logger.error("Asset have id = "+open.getOkm_hdpath()+" create Error");
			} 
	        Thread.sleep(100);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}	
	
	public String getFileImage(String fileId) throws Exception {
		String args = fileId.split("-")[0];
		String[] path= new String[4];
		path[0] = args.substring(0,2);
		path[1] = args.substring(2,4);
		path[2] = args.substring(4,6);
		path[3] = args.substring(6,8);
		String filePath = properties.getProperty("openkm_root_file")+"/"+path[0]+
				"/"+path[1]+"/"+path[2]+"/"+path[3];
		final File folder = new File(filePath);		
		File image = folder.listFiles()[0];
		byte[] fileContent = Files.readAllBytes(image.toPath());
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		return encodedString;
	}
}

