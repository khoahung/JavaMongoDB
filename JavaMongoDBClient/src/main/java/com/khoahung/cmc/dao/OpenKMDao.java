package com.khoahung.cmc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.khoahung.cmc.entity.LogData;
import com.khoahung.cmc.entity.OpenKM;

public class OpenKMDao {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/mobile_db";
    static final String USER = "root";
    static final String PASS = "root";
    
    public List<OpenKM> findAll() throws Exception {
		Class.forName(JDBC_DRIVER);
		Connection conn = DriverManager.getConnection( DB_URL, USER, PASS);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM t_okm_mapping tom "
				+ " left join a_customer_detail acd on acd.certify_front_pic  like CONCAT('%', tom.file_name ,'%') OR "
				+ " acd.certify_back_pic  like CONCAT('%', tom.file_name ,'%') OR "
				+ " acd.sinature_pic  like CONCAT('%', tom.file_name ,'%') OR "
				+ " acd.income_document_pic  like CONCAT('%', tom.file_name ,'%') OR "
				+ " acd.additional_pic1  like CONCAT('%', tom.file_name ,'%') OR "
				+ " acd.additional_pic2  like CONCAT('%', tom.file_name ,'%') OR "
				+ " acd.additional_pic3  like CONCAT('%', tom.file_name ,'%') "); 
		List<OpenKM> data = new ArrayList<OpenKM>();
		while(rs.next()) {
			OpenKM d = new OpenKM();
			d.setFile_name(rs.getString("file_name"));
			d.setOkm_hdpath(rs.getString("okm_hdpath"));
			d.setUser_name(rs.getString("user_name"));
			d.setReal_name(rs.getString("real_name"));
			d.setFirst_name(rs.getString("first_name"));
			d.setMiddle_name(rs.getString("middle_name"));
			d.setLast_name(rs.getString("last_name"));
			d.setMon_name(rs.getString("mon_name"));
			d.setMobile(rs.getString("mobile"));
			d.setEmail(rs.getString("email"));
			data.add(d);
		}
		stmt.close();
		conn.close();
		return data;
	}
}
