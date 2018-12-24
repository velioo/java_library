package com.sap;

import java.util.Date;

public class Book {
	private String id;
	private String name;
	private String author;
	private String publisher;
	private String language;
	private Date issueDate;
	private boolean isAvailable;

	public Book(String id, String name, String author, String publisher, String language, Date issueDate,
			boolean isAvailable) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.publisher = publisher;
		this.language = language;
		this.issueDate = issueDate;
		this.isAvailable = isAvailable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

}
