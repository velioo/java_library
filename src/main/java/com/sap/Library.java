package com.sap;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
				String id = rs.getString(1);
				
				user = new User(id, username, email);
			} else {
				assert false;
			}
			
			System.out.println("User id: " + user.getId());
			
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
					String id = rs.getString(1);
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

	// Add book => add new element in books => add new row in table books (DB)

}
