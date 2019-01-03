package com.sap;

public class App {
	public static void main(String[] args) {
		System.setProperty("textio.properties.location", "..\\textio.properties");
		
		Library library = new Library();
		LibraryMenu libraryMenu = library.getLibraryMenu();

		libraryMenu.authorizeUser();
		libraryMenu.runMainMenu();
	}
}
