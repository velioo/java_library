package com.sap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
	public static void main(String[] args) {

		// TODO: Implement logic for adding, deletin, etc in while(true) loop
		Library library = new Library();
		LibraryMenu libraryMenu = library.getLibraryMenu();
		// library.showMenu();

		libraryMenu.authorizeUser();
		// after authorize => show Main Menu

		/*
		 * String url = "jdbc:postgresql://localhost/jlibrary?user=jlibrary";
		 * try { Connection conn = DriverManager.getConnection(url);
		 * 
		 * Statement st = conn.createStatement(); ResultSet rs =
		 * st.executeQuery("SELECT * FROM books"); while (rs.next()) {
		 * System.out.print("Column 1 returned ");
		 * System.out.println(rs.getString(1)); } rs.close(); st.close();
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); }
		 */

	}
}
