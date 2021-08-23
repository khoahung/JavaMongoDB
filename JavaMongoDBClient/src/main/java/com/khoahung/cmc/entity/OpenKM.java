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
    
    @Column
    private String user_name;
    
    @Column
    private String real_name;
    
    @Column
    private String first_name;
    
    @Column
    private String middle_name;
    
    @Column
    private String last_name;
    
    @Column
    private String mon_name;
    
    @Column
    private String mobile;
    
    @Column
    private String email;

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

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getMon_name() {
		return mon_name;
	}

	public void setMon_name(String mon_name) {
		this.mon_name = mon_name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
