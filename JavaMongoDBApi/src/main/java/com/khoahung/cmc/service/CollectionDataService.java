package com.khoahung.cmc.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.khoahung.cmc.entity.Data;

@Service
public interface CollectionDataService {
	public ResponseEntity<String> getDataFromMongo(Data data);
}
