package com.sap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBInteraceImpl implements DBInterface {
	private static final String dbUrl = "jdbc:postgresql://localhost/jlibrary?user=jlibrary";
	private Connection connection;

	public DBInteraceImpl() {
		try {
			this.connection = DriverManager.getConnection(dbUrl);
		} catch (SQLException e) {
			System.out.println("Failed to connect to db");
			e.printStackTrace();
		}
	}

	public PreparedStatement createPreparedStatement(String query) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		return preparedStatement;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/*
	 * @Override public ResultSet select(PreparedStatement preparedStatement)
	 * throws SQLException { // PreparedStatement st =
	 * conn.prepareStatement("SELECT * FROM mytable // WHERE columnfoo = ?");
	 * ResultSet resultSet = preparedStatement.exexexecuteQuery();
	 * 
	 * return resultSet; // while (rs.next()) // { //
	 * System.out.print("Column 1 returned "); //
	 * System.out.println(rs.getString(1)); // } rs.close(); // st.close(); //
	 * return null; }
	 */

}
