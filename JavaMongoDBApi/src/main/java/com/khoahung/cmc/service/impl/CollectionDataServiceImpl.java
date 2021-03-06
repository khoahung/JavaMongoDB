package com.khoahung.cmc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.khoahung.cmc.dao.CollectionDataDao;
import com.khoahung.cmc.entity.Data;
import com.khoahung.cmc.service.CollectionDataService;

@Repository
public class CollectionDataServiceImpl implements CollectionDataService{
	
	@Autowired
	CollectionDataDao dao;
	
	@Override
	public String getDataFromMongo(Data data) {
		// TODO Auto-generated method stub
		return dao.getDataFromMongoDB(data);
	}

}
