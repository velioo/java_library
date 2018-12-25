package com.sap;

public class App {
	public static void main(String[] args) {

		// TODO: Implement logic for adding, deletin, etc in while(true) loop
		Library library = new Library();
		LibraryMenu libraryMenu = library.getLibraryMenu();
		// library.showMenu();

		libraryMenu.authorizeUser();
		libraryMenu.runMainMenu();
//			int option = libraryMenu.readUserInput();
			
/*			switch(option) {
				case 1: System.out.println("1");;
				default: System.out.println("defualt");;
			}*/
		// after authorize => show Main Menu

		/*
		 * String url = "jdbc:postgresql://localhost/jlibrary?user=jlibrary"; try {
		 * Connection conn = DriverManager.getConnection(url);
		 * 
		 * Statement st = conn.createStatement(); ResultSet rs =
		 * st.executeQuery("SELECT * FROM books"); while (rs.next()) {
		 * System.out.print("Column 1 returned "); System.out.println(rs.getString(1));
		 * } rs.close(); st.close();
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); }
		 */

	}
}
