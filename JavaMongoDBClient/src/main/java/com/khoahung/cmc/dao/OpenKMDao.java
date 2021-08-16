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
	static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://localhost:3307/mobile_db";
    static final String USER = "root";
    static final String PASS = "root";
    
    public List<OpenKM> findAll() throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection( DB_URL, USER, PASS);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM t_okm_mapping"); 
		List<OpenKM> data = new ArrayList<OpenKM>();
		while(rs.next()) {
			OpenKM d = new OpenKM();
			d.setFile_name(rs.getString("file_name"));
			d.setOkm_hdpath(rs.getString("okm_hdpath"));
			data.add(d);
		}
		stmt.close();
		conn.close();
		return data;
	}
}
