package com.sap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBInterface {
	public PreparedStatement createPreparedStatement(String query) throws SQLException;
}
