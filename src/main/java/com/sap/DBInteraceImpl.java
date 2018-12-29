package com.sap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInteraceImpl implements DBInterface {
	private static final String dbUrl = "jdbc:postgresql://localhost/jlibrary?user=jlibrary";
	private Connection connection;

	public DBInteraceImpl() throws SQLException {
		this.connection = DriverManager.getConnection(dbUrl);
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public PreparedStatement createPreparedStatement(String query) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		return preparedStatement;
	}

	public Statement createStatement() throws SQLException {
		Statement statement = connection.createStatement();
		return statement;
	}
}
