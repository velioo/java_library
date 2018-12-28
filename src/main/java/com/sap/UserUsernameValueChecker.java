package com.sap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.beryx.textio.InputReader.ValueChecker;

public class UserUsernameValueChecker implements ValueChecker<String> {
	private DBInteraceImpl dbh;
	private LibraryMenu libraryMenu;

	public UserUsernameValueChecker(LibraryMenu libraryMenu, DBInteraceImpl dbh) {
		this.dbh = dbh;
		this.libraryMenu = libraryMenu;
	}

	@Override
	public List<String> getErrorMessages(String val, String itemName) {
		ArrayList<String> errMsgs = new ArrayList<String>();

		String query = "SELECT username FROM users WHERE username = ?";
		try {
			PreparedStatement ps = dbh.createPreparedStatement(query);
			ps.setString(1, val);
			ps.execute();

			ResultSet rs = ps.getResultSet();

			if (rs.next()) {
				errMsgs.add("This username is already taken");
			}

		} catch (SQLException e) {
			libraryMenu.showError("Failed to check username", 1009);
			e.printStackTrace();
		}

		return errMsgs.isEmpty() ? null : errMsgs;
	}

}
