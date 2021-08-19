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
import com.khoahung.cmc.entity.AssetsDataProperties;
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
			
			dp.setNodeProperties(listP);
			
			List<com.khoahung.cmc.entity.Properties> assetsP = new ArrayList();
			
			com.khoahung.cmc.entity.Properties p5 = new com.khoahung.cmc.entity.Properties();
			p5.setName("jcr:data");
			p5.setType("Binary");
			p5.setMultiple(false);
			p5.setValues(Arrays.asList(getFileImage(open.getOkm_hdpath())));
			assetsP.add(p5);
			
			com.khoahung.cmc.entity.Properties p6 = new com.khoahung.cmc.entity.Properties();
			p6.setName("fileName");
			p6.setType("String");
			p6.setMultiple(false);
			p6.setValues(Arrays.asList(open.getFile_name()));
			assetsP.add(p6);
			
			com.khoahung.cmc.entity.Properties p7 = new com.khoahung.cmc.entity.Properties();
			p7.setName("jcr:mimeType");
			p7.setType("String");
			p7.setMultiple(false);
			p7.setValues(Arrays.asList("image/"+open.getFile_name().split("\\.")[1]));
			assetsP.add(p7);
			
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
				logger.info("Sub node have id = "+open.getOkm_hdpath()+" has copy");
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

