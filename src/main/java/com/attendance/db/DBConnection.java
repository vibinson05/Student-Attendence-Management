package com.attendance.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	 private static final String URL = "jdbc:mysql://localhost:3306/attendance_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
	    private static final String USER = "root";
	    private static final String PASSWORD = "ROOT";  

	    public static Connection getConnection() {
	        Connection conn = null;
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");  
	            conn = DriverManager.getConnection(URL, USER, PASSWORD);
	            System.out.println("Database Connected Successfully!");
	        } catch (ClassNotFoundException e) {
	            System.err.println("MySQL JDBC Driver Not Found!");
	            e.printStackTrace();
	        } catch (SQLException e) {
	            System.err.println(" Database Connection Failed!");
	            e.printStackTrace();
	        }
	        return conn;
	    }

	    public static void main(String[] args) {
	        getConnection();
	    }
	}
	

