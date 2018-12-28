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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.beryx.textio.InputReader.ValueChecker;
import org.beryx.textio.TextIO;

import com.google.common.hash.Hashing;

public class Library {
	private LibraryMenu libraryMenu;
	private ArrayList<Book> books;
	private ArrayList<Customer> customers;
	private DBInteraceImpl dbh;
	private TextIO textIO;
	private Connection conn;
	private User user;
	private static final String EMAIL_ADDRESS_REGEX = "^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{2,6}$";
	private static final String DATE_REGEX = "^$|(^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$)";
	private String lastSearch;

	public Library() {
		super();
		this.libraryMenu = new LibraryMenu(this);
		this.textIO = libraryMenu.getTextIO();
		this.books = new ArrayList<Book>();
		this.customers = new ArrayList<Customer>();

		try {
			this.dbh = new DBInteraceImpl();
			this.conn = dbh.getConnection();

			initBooks();
		} catch (SQLException e) {
			e.printStackTrace();
			libraryMenu.showError("Failed to initialize library", 1001);
		}
	}

	public void initBooks() throws SQLException {
		String query = "SELECT b.id as book_id, b.name as book_name, b.author, b.publisher, b.book_language, b.issue_date,"
				+ "tb.return_date, tb.created_at, c.id as customer_id, c.first_name, c .last_name, c.email FROM books b "
				+ "LEFT JOIN taken_books tb ON tb.book_id = b.id LEFT JOIN customers c ON c.id = tb.customer_id ORDER BY book_name ASC, author ASC";

		books.clear();

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {
			Integer bookId = rs.getInt(1);
			String bookName = rs.getString(2);
			String bookAuthor = rs.getString(3);
			String bookPublisher = rs.getString(4);
			String bookLanguage = rs.getString(5);
			Date bookIssueDate = rs.getDate(6);
			Date bookReturnDate = rs.getDate(7);
			Date bookTakenDate = rs.getDate(8);
			Integer customerId = rs.getInt(9);
			String customerFirstName = rs.getString(10);
			String customerLastName = rs.getString(11);
			String customerEmail = rs.getString(12);

			Customer customer = null;

			if (customerId != 0) {
				customer = new Customer(customerId, customerFirstName, customerLastName, customerEmail);
			}

			books.add(new Book(bookId, bookName, bookAuthor, bookPublisher, bookLanguage, bookIssueDate, bookReturnDate,
					bookTakenDate, customer));
		}

		rs.close();
	}

	public LibraryMenu getLibraryMenu() {
		return libraryMenu;
	}

	public void setLibraryMenu(LibraryMenu libraryMenu) {
		this.libraryMenu = libraryMenu;
	}

	public ArrayList<Book> getBooks() {
		return books;
	}

	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLastSearch() {
		return lastSearch;
	}

	public void setLastSearch(String lastSearch) {
		this.lastSearch = lastSearch;
	}

	public void registerUser() {
		String username = textIO.newStringInputReader().withMinLength(5).withMaxLength(20)
				.withValueChecker(new UserUsernameValueChecker(libraryMenu, dbh)).withInputTrimming(true)
				.read("Username");
		String password = textIO.newStringInputReader().withMinLength(6).withInputMasking(true).read("Password");
		String email = textIO.newStringInputReader().withMinLength(5).withPattern(EMAIL_ADDRESS_REGEX)
				.withValueChecker(new UserEmailValueChecker(libraryMenu, dbh)).withInputTrimming(true).read("Email");

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

		String query = "INSERT INTO books(name, author, publisher, book_language, issue_date) VALUES (?, ?, ?, ?, ?) RETURNING id, name, author, publisher, book_language, issue_date";

		try {
			PreparedStatement ps = dbh.createPreparedStatement(query);
			Date bookIssueDate = null;

			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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
			ps.setString(4, inputBookLanguage);
			ps.setDate(5, bookIssueDate);

			ps.execute();

			ResultSet rs = ps.getResultSet();

			if (rs.next()) {
				int bookId = rs.getInt(1);
				String bookName = rs.getString(2);
				String bookAuthor = rs.getString(3);
				String bookPublisher = rs.getString(4);
				String bookLanguage = rs.getString(5);
				Date issueDate = rs.getDate(6);

				books.add(new Book(bookId, bookName, bookAuthor, bookPublisher, bookLanguage, issueDate));
			} else {
				assert false;
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			libraryMenu.showError("Failed to add new book", 1004);
		}
	}

	public void removeBook(Book book) {

		assert books.contains(book) : "Cannot remove a book that doesn't exist in the library";

		String query = "DELETE FROM books WHERE id = ?";

		try {
			PreparedStatement ps = dbh.createPreparedStatement(query);

			ps.setInt(1, book.getId());

			ps.executeUpdate();

			books.remove(book);

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			libraryMenu.showError("Failed to remove book", 1007);
		}
	}

	public void addNewCustomer() {
		String firstName = textIO.newStringInputReader().withMaxLength(100).withInputTrimming(true).read("First name");
		String lastName = textIO.newStringInputReader().withMaxLength(100).withInputTrimming(true).read("Last name");
		String email = textIO.newStringInputReader().withMinLength(5).withPattern(EMAIL_ADDRESS_REGEX)
				.withValueChecker(new CustomerEmailValueChecker(libraryMenu, dbh)).withInputTrimming(true)
				.read("Email");

		String query = "INSERT INTO customers(first_name, last_name, email) VALUES (?, ?, ?)";

		try {
			PreparedStatement ps = dbh.createPreparedStatement(query);

			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, email);

			ps.execute();

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			libraryMenu.showError("Failed to register user", 1002);
		}
	}

	public ArrayList<Book> searchBookByTitle() {
		String bookTitle = textIO.newStringInputReader().withMaxLength(100).withInputTrimming(true).read("Title");

		lastSearch = bookTitle;

		ArrayList<Book> matchedBooks = new ArrayList<Book>();

		for (Book book : books) {
			if (book.getName().toLowerCase().contains(bookTitle.toLowerCase())) {
				matchedBooks.add(book);
			}
		}

		return matchedBooks;
	}

	public ArrayList<Book> searchBookCombined() {
		String bookTitle = textIO.newStringInputReader().withMinLength(0).withMaxLength(100).withInputTrimming(true)
				.read("Title");
		String bookAuthor = textIO.newStringInputReader().withMinLength(0).withMaxLength(100).withInputTrimming(true)
				.read("Author");
		String bookPublisher = textIO.newStringInputReader().withMinLength(0).withMaxLength(100).withInputTrimming(true)
				.read("Publisher");
		String bookIssueDate = textIO.newStringInputReader().withMinLength(0).withPattern(DATE_REGEX)
				.withInputTrimming(true).read("Issue date (yyyy-MM-dd)");
		String bookLanguage = textIO.newStringInputReader().withMinLength(0).withMaxLength(100).withInputTrimming(true)
				.read("Language");

		lastSearch = "Title - '" + bookTitle + "', author - '" + bookAuthor + "', publisher - '" + bookPublisher
				+ "', issue year - '" + bookIssueDate + "', language - '" + bookLanguage + "'";

		ArrayList<Book> matchedBooks = new ArrayList<Book>();
		Date issueDate = null;

		if (!bookIssueDate.isEmpty()) {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date = df.parse(bookIssueDate);
				issueDate = new Date(date.getTime());
			} catch (ParseException e) {
				libraryMenu.showError("Failed to parse issue date", 1006);
				e.printStackTrace();
			}
		}

		for (Book book : books) {
			if ((bookTitle.isEmpty()
					|| !bookTitle.isEmpty() && book.getName().toLowerCase().contains(bookTitle.toLowerCase()))
					&& (bookAuthor.isEmpty() || !bookAuthor.isEmpty()
							&& book.getAuthor().toLowerCase().contains(bookAuthor.toLowerCase()))
					&& (bookPublisher.isEmpty() || !bookPublisher.isEmpty()
							&& book.getPublisher().toLowerCase().contains(bookPublisher.toLowerCase()))
					&& (bookIssueDate.isEmpty()
							|| !bookIssueDate.isEmpty() && book.getIssueDate().compareTo(issueDate) == 0)
					&& (bookLanguage.isEmpty() || !bookLanguage.isEmpty()
							&& book.getLanguage().toLowerCase().contains(bookLanguage.toLowerCase()))) {
				matchedBooks.add(book);
			}
		}

		return matchedBooks;
	}

	// Add book => add new element in books => add new row in table books (DB)

}
