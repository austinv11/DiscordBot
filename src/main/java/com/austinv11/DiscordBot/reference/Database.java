package com.austinv11.DiscordBot.reference;

import java.sql.*;
import java.util.HashMap;

public class Database {
	
	public final String databaseFile;
	private boolean isConnected = false;
	private Connection connection = null;
	private Statement selectStatement = null;
	private ResultSet selectSet = null;
	
	
	public Database(String databaseFile) {
		this.databaseFile = databaseFile;
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public void createTable(String tableName, Key... rows) throws SQLException {
		if (!isConnected)
			connect();
		Statement statement = connection.createStatement();
		String instruction = "CREATE TABLE "+tableName.toUpperCase()+" (";
		for (Key key : rows) {
			instruction += key.name.toUpperCase()+" ";
			instruction += key.type.toUpperCase();
			if (key.additionalOptions != null && !key.additionalOptions.isEmpty())
				instruction += " "+key.additionalOptions.toUpperCase();
			instruction += ", ";
		}
		instruction = instruction.substring(0, instruction.length()-2);
		instruction += ");";
		statement.executeUpdate(instruction);
		statement.close();
		disconnect();
	}
	
	public void insert(String tableName, String[] keys, String[] values) throws SQLException {
		if (!isConnected)
			connect();
		Statement statement = connection.createStatement();
		connection.setAutoCommit(false);
		String sql = "INSERT INTO "+tableName.toUpperCase()+" (";
		for (String key : keys)
			sql += key.toUpperCase()+",";
		sql = sql.substring(0, sql.length()-1);
		sql += ") VALUES (";
		for (Object value : values)
			sql += value+", ";
		sql = sql.substring(0, sql.length()-2);
		sql += ");";
		statement.executeUpdate(sql);
		connection.commit();
		statement.close();
		disconnect();
	}
	
	public ResultSet openSelect(String tableName) throws SQLException {
		if (selectStatement != null)
			closeSelect();
		if (!isConnected)
			connect();
		selectStatement = connection.createStatement();
		selectSet = selectStatement.executeQuery("SELECT * FROM "+tableName.toUpperCase()+";");
		return selectSet;
	}
	
	public void closeSelect() throws SQLException {
		if (selectStatement == null)
			throw new SQLException("No select statement in progress");
		selectSet.close();
		selectStatement.close();
		selectSet = null;
		selectStatement = null;
		disconnect();
	}
	
	public void uodate(String tableName, String primaryKey, String primaryKeyValue, HashMap<String, String> keysToUpdate) throws SQLException {
		if (!isConnected)
			connect();
		Statement statement = connection.createStatement();
		for (String key : keysToUpdate.keySet()) {
			String sql = "UPDATE "+tableName.toUpperCase()+" set "+key.toUpperCase()+" = "+keysToUpdate.get(key)+
					" where "+primaryKey.toUpperCase()+"="+primaryKeyValue+";";
			statement.executeUpdate(sql);
		}
		statement.close();
		disconnect();
	}
	
	public void delete(String tableName, String primaryKey, Object primaryKeyValue) throws SQLException {
		if (!isConnected)
			connect();
		Statement statement = connection.createStatement();
		String sql = "DELETE from "+tableName.toUpperCase()+" where "+primaryKey.toUpperCase()+"="+primaryKeyValue+";";
		statement.executeUpdate(sql);
		statement.close();
		disconnect();
	}
	
	public void connect() throws SQLException {
		if (!isConnected) {
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:"+databaseFile);
				isConnected = true;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void disconnect() throws SQLException {
		if (isConnected) {
			connection.close();
			isConnected = false;
		}
	}
	
	public static class Key {
		
		private String name;
		private String type;
		private String additionalOptions;
		
		public Key(String name, String type, String additionalOptions) {
			this.name = name;
			this.type = type;
			this.additionalOptions = additionalOptions;
		}
	} 
}
