package com.dgreat.rfid;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbCon {
	
	static String dburl = "";
	static String dbname = "rfid_xp";
	static String dbuser = "root";
	static String dbpass = "";
	
	public DbCon(){
		try {
			// Local MySQL instance to use during development.
			Class.forName("com.mysql.cj.jdbc.Driver");
			//Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jwdb?useTimezone=true&serverTimezone=UTC", "root", "admin");

			DbCon.dburl = "jdbc:mysql://127.0.0.1:3306/rfid_xp?useTimezone=true&serverTimezone=UTC";
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection connect() {
		Connection conn = null;
		try { 
			conn = DriverManager.getConnection(dburl,dbuser,dbpass); 
			//conn = DriverManager.getConnection(dburl);
		} catch (SQLException e) { System.out.println(e); }	
		return conn;
	}
	
	public static void main(String[] args) {
		DbCon dbc = new DbCon();
		Connection con = dbc.connect();
	}

}
