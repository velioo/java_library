package com.sap;

public class App {
	public static void main(String[] args) {

		Library library = new Library();
		LibraryMenu libraryMenu = library.getLibraryMenu();

		libraryMenu.authorizeUser();
		libraryMenu.runMainMenu();
	}
}
