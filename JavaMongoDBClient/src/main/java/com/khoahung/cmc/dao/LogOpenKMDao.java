package com.khoahung.cmc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.khoahung.cmc.entity.LogData;
import com.khoahung.cmc.entity.LogOpenKM;
@Transactional
public class LogOpenKMDao{
	
	static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://localhost:3307/logdb";
    static final String USER = "root";
    static final String PASS = "root";
    
    @Transactional
    public void save(LogOpenKM longData) throws Exception{
    	Class.forName("org.mariadb.jdbc.Driver");
    	Connection conn = DriverManager.getConnection( DB_URL, USER, PASS);
    	PreparedStatement ps= conn.prepareStatement("INSERT INTO LogOpenKM(okm_hdpath, updateTime) values(?,?)");
    	ps.setString(1,longData.getOkm_hdpath());
    	java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
    	ps.setDate(2,date);
    	ps.executeUpdate();
    	ps.close();
    	conn.close();
    }

	public List<LogOpenKM> findAll() throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection( DB_URL, USER, PASS);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM LogOpenKM"); 
		List<LogOpenKM> data = new ArrayList<LogOpenKM>();
		while(rs.next()) {
			LogOpenKM d = new LogOpenKM();
			d.setId(rs.getInt("id"));
			d.setOkm_hdpath(rs.getString("okm_hdpath"));
			d.setUpdateTime(rs.getDate("updateTime"));
			data.add(d);
		}
		conn.close();
		return data;
	}
}
