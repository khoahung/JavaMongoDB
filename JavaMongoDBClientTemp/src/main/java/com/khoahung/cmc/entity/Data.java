package com.khoahung.cmc.entity;

import java.io.Serializable;
import java.util.List;

import org.bson.Document;

public class Data implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Document> list;
	
	public List<Document> getList() {
		return list;
	}

	public void setList(List<Document> list) {
		this.list = list;
	}
	
	
}
