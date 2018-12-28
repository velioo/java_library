package com.sap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.beryx.textio.InputReader.ValueChecker;

public class CustomerEmailValueChecker implements ValueChecker<String> {
	private DBInteraceImpl dbh;
	private LibraryMenu libraryMenu;

	public CustomerEmailValueChecker(LibraryMenu libraryMenu, DBInteraceImpl dbh) {
		this.dbh = dbh;
		this.libraryMenu = libraryMenu;
	}

	@Override
	public List<String> getErrorMessages(String val, String itemName) {
		ArrayList<String> errMsgs = new ArrayList<String>();

		String query = "SELECT email FROM customers WHERE email = ?";
		try {
			PreparedStatement ps = dbh.createPreparedStatement(query);
			ps.setString(1, val);
			ps.execute();

			ResultSet rs = ps.getResultSet();

			if (rs.next()) {
				errMsgs.add("This email is already taken");
			}

		} catch (SQLException e) {
			libraryMenu.showError("Failed to check customer email", 1010);
			e.printStackTrace();
		}

		return errMsgs.isEmpty() ? null : errMsgs;
	}

}
