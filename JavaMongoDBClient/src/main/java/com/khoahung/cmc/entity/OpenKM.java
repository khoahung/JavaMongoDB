package com.khoahung.cmc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_okm_mapping")
public class OpenKM implements Serializable {
 
    private static final long serialVersionUID = 1L;
    
    @Column
    private String file_name;
    
    @Column
    private String okm_hdpath;

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getOkm_hdpath() {
		return okm_hdpath;
	}

	public void setOkm_hdpath(String okm_hdpath) {
		this.okm_hdpath = okm_hdpath;
	}

}
