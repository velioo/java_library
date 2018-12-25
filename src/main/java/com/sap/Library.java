package com.sap;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.beryx.textio.TextIO;

import com.google.common.hash.Hashing;

public class Library {
	private LibraryMenu libraryMenu;
	private Book[] books;
	private DBInteraceImpl dbh;
	private TextIO textIO;
	private Connection conn;
	private User user;
	private static final String EMAIL_ADDRESS_REGEX = "^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{2,6}$";
	private static final String DATE_REGEX = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";

	public Library() {
		super();
		this.libraryMenu = new LibraryMenu(this);
		this.textIO = libraryMenu.getTextIO();

		try {
			this.dbh = new DBInteraceImpl();
			this.conn = dbh.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			libraryMenu.showError("Failed to connect to the database", 1001);
		}
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void registerUser() {
		String username = textIO.newStringInputReader().withMinLength(5).withMaxLength(20).withInputTrimming(true)
				.read("Username");
		String password = textIO.newStringInputReader().withMinLength(6).withInputMasking(true).read("Password");

		String email = textIO.newStringInputReader().withMinLength(5).withPattern(EMAIL_ADDRESS_REGEX)
				.withInputTrimming(true).read("Email");

		String query = "INSERT INTO users(username, password, email, salt) VALUES (?, ?, ?, ?) RETURNING id";

		try {
			PreparedStatement ps = dbh.createPreparedStatement(query);

			String salt = UUID.randomUUID().toString();
			String password_hash = Hashing.sha256().hashString(password + salt, StandardCharsets.UTF_8).toString();

			ps.setString(1, username);
			ps.setString(2, password_hash);
			ps.setString(3, email);
			ps.setString(4, salt);

			ps.execute();

			ResultSet rs = ps.getResultSet();

			if (rs.next()) {
				int id = rs.getInt(1);

				user = new User(id, username, email);
			} else {
				assert false;
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			libraryMenu.showError("Failed to register user", 1002);
		}
	}

	public boolean loginUser() {
		String inputUsername = textIO.newStringInputReader().withMinLength(5).withMaxLength(20).withInputTrimming(true)
				.read("Username");
		String inputPassword = textIO.newStringInputReader().withMinLength(6).withInputMasking(true).read("Password");

		String query = "SELECT * FROM users WHERE username = ?";

		try {
			PreparedStatement preparedStatement = dbh.createPreparedStatement(query);
			preparedStatement.setString(1, inputUsername);

			ResultSet rs = preparedStatement.executeQuery();

			if (!rs.next()) {
				return false;
			} else {
				do {
					int id = rs.getInt(1);
					String username = rs.getString(2);
					String password = rs.getString(3);
					String salt = rs.getString(4);
					String email = rs.getString(7);

					if (Hashing.sha256().hashString(inputPassword + salt, StandardCharsets.UTF_8).toString()
							.equals(password)) {

						user = new User(id, username, email);
					} else {
						return false;
					}

					break;

				} while (rs.next());
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			libraryMenu.showError("Failed to login user", 1003);
		}

		return true;
	}

	public void addNewBook() {
		String inputBookName = textIO.newStringInputReader().withMaxLength(100).withInputTrimming(true)
				.read("Book title");
		String inputBookAuthor = textIO.newStringInputReader().withMaxLength(100).withInputTrimming(true)
				.read("Book author");
		String inputBookIssueDate = textIO.newStringInputReader().withPattern(DATE_REGEX).withMaxLength(100)
				.withInputTrimming(true).read("Book issue date (yyyy-MM-dd)");
		String inputBookPublisher = textIO.newStringInputReader().withMaxLength(100).withInputTrimming(true)
				.read("Book publisher");
		String inputBookLanguage = textIO.newStringInputReader().withMaxLength(100).withInputTrimming(true)
				.read("Book language");

		String query = "INSERT INTO books(name, author, publisher, book_language, issue_date) VALUES (?, ?, ?, ?, ?)";

		try {
			PreparedStatement ps = dbh.createPreparedStatement(query);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			Date bookIssueDate = null;

			try {
				java.util.Date date = df.parse(inputBookIssueDate);
				bookIssueDate = new Date(date.getTime());
			} catch (ParseException e) {
				libraryMenu.showError("Failed to add new book", 1005);
				e.printStackTrace();
			}
			
			assert bookIssueDate != null;

			ps.setString(1, inputBookName);
			ps.setString(2, inputBookAuthor);
			ps.setString(3, inputBookPublisher);
			ps.setString(4, inputBookPublisher);
			ps.setDate(5, bookIssueDate);

			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			libraryMenu.showError("Failed to add new book", 1004);
		}
	}

	// Add book => add new element in books => add new row in table books (DB)

}
