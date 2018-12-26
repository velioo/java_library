package com.sap;

import java.util.ArrayList;

public class Customer {
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private ArrayList<Book> takenBooks;

	public Customer(int id, String firstName, String lastName, String email) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.takenBooks = new ArrayList<Book>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void addTakenBook(Book book) {
		takenBooks.add(book);
	}

	public void removeTakenBook(Book book) {
		takenBooks.remove(book);
	}

	public int getCountOfTakenBooks() {
		return takenBooks.size();
	}

	public ArrayList<Book> getTakenBooks() {
		return takenBooks;
	}

	public void setTakenBooks(ArrayList<Book> takenBooks) {
		this.takenBooks = takenBooks;
	}

}
