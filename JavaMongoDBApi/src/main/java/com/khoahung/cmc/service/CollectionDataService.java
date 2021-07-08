package com.khoahung.cmc.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CollectionDataService {
	public ResponseEntity<String> getDataFromMongo(String json);
}
