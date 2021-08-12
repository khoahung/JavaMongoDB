package com.khoahung.cmc.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AgentJavaMongoClient {
	public static void main(String[] args) {
		//Agent agent = new Agent();
		//agent.start();
		try {
			InputStream inputStream = AgentJavaMongoClient.class.getClassLoader().getResourceAsStream("config.properties");
			// load properties from file
			Properties properties = new Properties();
			properties.load(inputStream);
			AgentMangolia agentMangolia = new AgentMangolia(properties);
			agentMangolia.start();
		} catch (IOException e) {
            e.printStackTrace();
        } 
	}
}
