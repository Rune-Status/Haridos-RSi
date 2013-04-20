package com.runecore.rsi.env.db;

import java.sql.*;

public class DatabaseConnection {
	
	/**
	 * Creates a new instance 
	 * @param args Details for the connection
	 * @return
	 */
	public static DatabaseConnection create(String path) {
		return new DatabaseConnection(path);
	}
	
	/**
	 * The database connection
	 */
	private Connection connection;
	
	/**
	 * Connection details
	 */
	private final String filePath;
	
	/**
	 * Constructor
	 * @param host
	 * @param username
	 * @param password
	 * @param database
	 */
	private DatabaseConnection(String filePath) {
		this.filePath = filePath;
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Connects to the database
	 * @throws Exception
	 */
	private void connect() throws Exception {
		Connection c = DriverManager.getConnection("jdbc:sqlite:" + filePath);
		setConnection(c);
	}
	
	/**
	 * Kills the connection
	 * @throws Exception 
	 */
	public void killConnection() throws Exception {
		getConnection().close();
	}
	
	/**
	 * Gets a statement
	 * @param query
	 * @return
	 */
	public PreparedStatement getStatement(String query) {
		PreparedStatement statement = null;
		try {
			if(getConnection().isClosed() || (getConnection() == null)) {
				connect();
			}
			statement = getConnection().prepareStatement(query);
			return statement;
		} catch(Exception e) {
			e.printStackTrace();
			try {
				connect();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * Get the connection details
	 * @return the details
	 */
	public String getPath() {
		return filePath;
	}

	/**
	 * Sets the connection
	 * @param databaseConnection the databaseConnection to set
	 */
	private void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return the databaseConnection
	 */
	private Connection getConnection() {
		return connection;
	}

}
