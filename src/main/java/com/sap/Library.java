package com.sap;

public class Library {
	private LibraryMenu libraryMenu;
	private Book[] books;

	public Library() {
		super();
		this.libraryMenu = new LibraryMenu();
	}

	public LibraryMenu getLibraryMenu() {
		return libraryMenu;
	}

	public void setLibraryMenu(LibraryMenu libraryMenu) {
		this.libraryMenu = libraryMenu;
	}

	public Book[] getBooks() {
		return books;
	}

	public void setBooks(Book[] books) {
		this.books = books;
	}

	// Add book => add new element in books => add new row in table books (DB)

}
