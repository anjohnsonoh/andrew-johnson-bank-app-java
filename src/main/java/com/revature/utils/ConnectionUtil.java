package com.revature.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton utility for creating and retrieving database connection
 */
public class ConnectionUtil {
	private static ConnectionUtil cu = null;
	private static Properties prop;
	private static Connection connection;
	/**
	 * This method should read in the "database.properties" file and load
	 * the values into the Properties variable
	 */
	private ConnectionUtil() {
	}
	
	public static synchronized ConnectionUtil getConnectionUtil() {
		if(cu == null)
			cu = new ConnectionUtil();
		return cu;
	}
	
	/**
	 * This method should create and return a Connection object
	 * @return a Connection to the database
	 */
	public static Connection getConnection() {
		Connection connection;
		Properties p = new Properties();
		InputStream input;
		try {
			input = new FileInputStream("src/main/resources/database.properties");
			p.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = p.getProperty("url");
		String username = p.getProperty("usr");
		String pass = p.getProperty("pswd");
		try {
			connection = DriverManager.getConnection(url,username,pass);
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(url, username, pass);
			}
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
