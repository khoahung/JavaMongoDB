package com.khoahung.cmc.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AgentJavaMongoClient {
	public static void main(String[] args) {
		try {
			InputStream inputStream = AgentJavaMongoClient.class.getClassLoader().getResourceAsStream("config.properties");
			// load properties from file
			Properties properties = new Properties();
			properties.load(inputStream);

			AgentMongoDB agentMongoDB = new AgentMongoDB(properties);
			agentMongoDB.start();
			
			AgentOpenKM openKm = new AgentOpenKM(properties);
			openKm.start();
		} catch (IOException e) {
            e.printStackTrace();
        } 
	}
}
