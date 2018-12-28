package com.sap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.beryx.textio.InputReader.ValueChecker;

public class UserEmailValueChecker implements ValueChecker<String> {
	private DBInteraceImpl dbh;
	private LibraryMenu libraryMenu;

	public UserEmailValueChecker(LibraryMenu libraryMenu, DBInteraceImpl dbh) {
		this.dbh = dbh;
		this.libraryMenu = libraryMenu;
	}

	@Override
	public List<String> getErrorMessages(String val, String itemName) {
		ArrayList<String> errMsgs = new ArrayList<String>();

		String query = "SELECT email FROM users WHERE email = ?";
		try {
			PreparedStatement ps = dbh.createPreparedStatement(query);
			ps.setString(1, val);
			ps.execute();

			ResultSet rs = ps.getResultSet();

			if (rs.next()) {
				errMsgs.add("This email is already taken");
			}

		} catch (SQLException e) {
			libraryMenu.showError("Failed to check email", 1008);
			e.printStackTrace();
		}

		return errMsgs.isEmpty() ? null : errMsgs;
	}

}
