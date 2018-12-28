package com.sap;

import java.sql.Date;

public class Book {
	private int id;
	private String name;
	private String author;
	private String publisher;
	private String language;
	private Date issueDate;
	private Date returnDate;
	private Date takenDate;
	private Customer customer;

	public Book(int id, String name, String author, String publisher, String language, Date issueDate) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.publisher = publisher;
		this.language = language;
		this.issueDate = issueDate;
	}

	public Book(int id, String name, String author, String publisher, String language, Date issueDate, Date returnDate,
			Date takenDate, Customer customer) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.publisher = publisher;
		this.language = language;
		this.issueDate = issueDate;
		this.returnDate = returnDate;
		this.takenDate = takenDate;
		this.customer = customer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public boolean isAvailable() {
		return customer == null;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Date getTakenDate() {
		return takenDate;
	}

	public void setTakenDate(Date takenDate) {
		this.takenDate = takenDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
