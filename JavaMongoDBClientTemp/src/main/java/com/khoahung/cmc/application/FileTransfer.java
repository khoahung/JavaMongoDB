package com.khoahung.cmc.application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class FileTransfer extends Thread {
	public void run(){
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost("http://localhost:8083/getFile");
			File file = new File("D:\\project\\Example\\JavaMongoDB\\SourceFile");
			for (final File fileEntry : file.listFiles()) {
				if(!fileEntry.isDirectory()) {
					MultipartEntityBuilder  builder = MultipartEntityBuilder.create();
					builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					builder.addBinaryBody("file", fileEntry, ContentType.DEFAULT_BINARY, fileEntry.getName());
					HttpEntity entity = builder.build();
					httpPost.setEntity(entity);
					HttpResponse response = httpClient.execute(httpPost);
					String message = EntityUtils.toString(response.getEntity());
					System.out.println(message);
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
