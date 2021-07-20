package com.khoahung.cmc.service;


import org.springframework.stereotype.Service;

import com.khoahung.cmc.entity.Data;

@Service
public interface CollectionDataService {
	public String getDataFromMongo(Data data);
}
