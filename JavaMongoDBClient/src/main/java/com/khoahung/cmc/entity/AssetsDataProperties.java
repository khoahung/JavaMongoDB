package com.khoahung.cmc.entity;

import java.io.Serializable;
import java.util.List;

public class AssetsDataProperties implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String type;
	private String path;
	private List<Properties> nodeProperties;
	private List<Properties> assetProperties;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<Properties> getNodeProperties() {
		return nodeProperties;
	}
	public void setNodeProperties(List<Properties> nodeProperties) {
		this.nodeProperties = nodeProperties;
	}
	public List<Properties> getAssetProperties() {
		return assetProperties;
	}
	public void setAssetProperties(List<Properties> assetProperties) {
		this.assetProperties = assetProperties;
	}
	
}
