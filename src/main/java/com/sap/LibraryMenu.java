package com.sap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class LibraryMenu {
	private TextIO textIO;
	private TextTerminal terminal;
	private Library library;
	private static final String REGISTER = "Register";
	private static final String LOGIN = "Login";
	private static final String EMPTY_BOOKMARK = "EMPTY";
	private static final String MAIN_MENU_LABEL_ADD_NEW_BOOK = "Add new book";
	private static final String MAIN_MENU_LABEL_SEARCH_BOOK = "Search books";
	private static final String MAIN_MENU_LABEL_LIST_PEOPLE = "List people who haven't returned books";
	private static final String MAIN_MENU_LABEL_ADD_NEW_CUSTOMER = "Add new customer";
	private static final String MAIN_MENU_LABEL_EXIT = "Exit";
	private static final String SEARCH_MENU_LABEL_SEARCH_BY_TITLE = "Search by title";
	private static final String SEARCH_MENU_LABEL_COMBINED_SEARCH = "Combined search by title, author, issue date, publisher, language";
	private static final String SEARCH_MENU_LABEL_BACK = "Return to Main Menu";
	private static final String BOOK_MENU_LABEL_MARK_TAKEN = "Mark as taken";
	private static final String BOOK_MENU_LABEL_MARK_AVAILABLE = "Mark as available";
	private static final String BOOK_MENU_LABEL_SET_DEADLINE = "Set deadline";
	private static final String BOOK_MENU_LABEL_REMOVE_BOOK = "Remove book";
	private static final String BOOK_MENU_LABEL_BACK = "Return to Search Menu";
	private static final String CUSTOMERS_MENU_LABEL_SEARCH_AGAIN = "Search again";
	private static final String CUSTOMERS_MENU_LABEL_BACK = "Return to book menu";
	private static final ArrayList<String> mainMenuPossibleValues = new ArrayList<String>();
	private static final ArrayList<String> searchMenuPossibleValues = new ArrayList<String>();
	private static final ArrayList<String> bookMenuPossibleValues = new ArrayList<String>();

	public LibraryMenu(Library library) {
		initializeMenu();
		this.library = library;
	}

	public void initializeMenu() {
		this.textIO = TextIoFactory.getTextIO();
		this.terminal = textIO.getTextTerminal();
		this.terminal.setBookmark(EMPTY_BOOKMARK);

		mainMenuPossibleValues.add(MAIN_MENU_LABEL_ADD_NEW_BOOK);
		mainMenuPossibleValues.add(MAIN_MENU_LABEL_SEARCH_BOOK);
		mainMenuPossibleValues.add(MAIN_MENU_LABEL_LIST_PEOPLE);
		mainMenuPossibleValues.add(MAIN_MENU_LABEL_ADD_NEW_CUSTOMER);
		mainMenuPossibleValues.add(MAIN_MENU_LABEL_EXIT);

		searchMenuPossibleValues.add(SEARCH_MENU_LABEL_SEARCH_BY_TITLE);
		searchMenuPossibleValues.add(SEARCH_MENU_LABEL_COMBINED_SEARCH);
		searchMenuPossibleValues.add(SEARCH_MENU_LABEL_BACK);

		bookMenuPossibleValues.add(BOOK_MENU_LABEL_MARK_TAKEN);
		bookMenuPossibleValues.add(BOOK_MENU_LABEL_MARK_AVAILABLE);
		bookMenuPossibleValues.add(BOOK_MENU_LABEL_SET_DEADLINE);
		bookMenuPossibleValues.add(BOOK_MENU_LABEL_REMOVE_BOOK);
		bookMenuPossibleValues.add(BOOK_MENU_LABEL_BACK);
	}

	public TextIO getTextIO() {
		return textIO;
	}

	public void setTextIO(TextIO textIO) {
		this.textIO = textIO;
	}

	public TextTerminal getTerminal() {
		return terminal;
	}

	public void setTerminal(TextTerminal terminal) {
		this.terminal = terminal;
	}

	public void printHeader(String message) {
		terminal.println(
				"----------------------------------------------------------------------------------------------------------------");
		terminal.println("| " + message + " | Ctrl + Q to exit");
		terminal.println(
				"----------------------------------------------------------------------------------------------------------------");
	}

	public void authorizeUser() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("Select Option");
		terminal.setBookmark("SELECT OPTION");

		String option = textIO.newStringInputReader().withNumberedPossibleValues(REGISTER, LOGIN).read("Option");

		if (option.equals(REGISTER)) {
			showRegisterScreen();
			library.registerUser();
		} else if (option.equals(LOGIN)) {
			showLoginScreen();

			boolean isLoginSuccessful = library.loginUser();

			while (!isLoginSuccessful) {
				terminal.println("Username or password is invalid, try again");
				isLoginSuccessful = library.loginUser();
			}
		} else {
			assert false;
		}
	}

	public void showRegisterScreen() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("Registration");
	}

	public void showLoginScreen() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("Login");
	}

	public void runMainMenu() {
		while (true) {
			terminal.resetToBookmark(EMPTY_BOOKMARK);
			printHeader("Main Menu");

			String option = textIO.newStringInputReader().withNumberedPossibleValues(mainMenuPossibleValues)
					.read("Option");

			if (option.equals(MAIN_MENU_LABEL_ADD_NEW_BOOK)) {
				showAddBookScreen();
				library.addNewBook();
				showSuccessScreen("added new book");
			} else if (option.equals(MAIN_MENU_LABEL_SEARCH_BOOK)) {
				runSearchMenu();
			} else if (option.equals(MAIN_MENU_LABEL_LIST_PEOPLE)) {
				showTakenBooksScreen();
				showTakenBooks();
			} else if (option.equals(MAIN_MENU_LABEL_ADD_NEW_CUSTOMER)) {
				showAddCustomerScreen();
				library.addNewCustomer();
				showSuccessScreen("added new customer");
			} else if (option.equals(MAIN_MENU_LABEL_EXIT)) {
				terminal.dispose();
				break;
			} else {
				assert false : "Option not implemented";
			}
		}
	}

	public void runSearchMenu() {
		while (true) {
			terminal.resetToBookmark(EMPTY_BOOKMARK);
			printHeader("Search books");

			String option = textIO.newStringInputReader().withNumberedPossibleValues(searchMenuPossibleValues)
					.read("Option");

			if (option.equals(SEARCH_MENU_LABEL_SEARCH_BY_TITLE)) {
				showFindBookByTitleScreen();
				ArrayList<Book> books = library.searchBookByTitle();
				showMatchedBooks(books);
			} else if (option.equals(SEARCH_MENU_LABEL_COMBINED_SEARCH)) {
				showCombinedSearchScreen();
				ArrayList<Book> books = library.searchBookCombined();
				showMatchedBooks(books);
			} else if (option.equals(SEARCH_MENU_LABEL_BACK)) {
				break;
			} else {
				assert false : "Option not implemented";
			}
		}
	}

	public void runBookMenu(Book book) {
		while (true) {
			terminal.resetToBookmark(EMPTY_BOOKMARK);
			printHeader("Book information. Select option");

			terminal.println("Title      : " + book.getName());
			terminal.println("Author     : " + book.getAuthor());
			terminal.println("Issue date : " + book.getIssueDate());
			terminal.println("Publisher  : " + book.getPublisher());
			terminal.println("Language   : " + book.getLanguage());
			terminal.println("\nStatus     : " + (book.isAvailable() ? "Available\n"
					: "Taken by " + book.getCustomer().getBothNames() + " on " + book.getTakenDate()));

			if (!book.isAvailable()) {
				terminal.println("Return date: " + book.getReturnDate() + "\n");
			}

			String option = textIO.newStringInputReader().withNumberedPossibleValues(bookMenuPossibleValues)
					.read("Option");

			if (option.equals(BOOK_MENU_LABEL_MARK_TAKEN)) {
				String question = "mark the book as taken?";

				if (!book.isAvailable()) {
					question += " The book is already taken by " + book.getCustomer().getBothNames()
							+ ", you will overwrite its customer!!!";
				}

				if (isConfirmed(question)) {
					Customer customer = runSelectCustomerMenu();

					if (customer == null) {
						continue;
					}

					library.markBookAsTaken(book, customer);
					showSuccessScreen(
							"marked book '" + book.getName() + "' as taken by " + book.getCustomer().getBothNames());
				}
			} else if (option.equals(BOOK_MENU_LABEL_MARK_AVAILABLE)) {

				if (isConfirmed("mark the book as available?")) {
					if (!book.isAvailable()) {
						library.markBookAsAvailable(book);
					}
				}
			} else if (option.equals(BOOK_MENU_LABEL_SET_DEADLINE)) {
				if (book.isAvailable()) {
					showContinueScreen("Cannot set a deadline to a book which is not taken by anyone");
					continue;
				}

				if (isConfirmed("change the deadline?")) {
					library.changeBookDeadline(book);
					showSuccessScreen("changed the book's deadline");
				}
			} else if (option.equals(BOOK_MENU_LABEL_REMOVE_BOOK)) {

				if (!book.isAvailable()) {
					showContinueScreen("Cannot remove a book which is still taken by someone");
					continue;
				}

				if (isConfirmed("remove the book?")) {
					library.removeBook(book);
					showSuccessScreen("removed book '" + book.getName() + "'");
					break;
				}
			} else if (option.equals(BOOK_MENU_LABEL_BACK)) {
				break;
			} else {
				assert false : "Invalid option";
			}
		}
	}

	private Customer runSelectCustomerMenu() {
		ArrayList<Customer> customers = library.getCustomers();
		ArrayList<String> formatedCustomers = new ArrayList<String>();

		for (Customer customer : customers) {
			String formatedCustomer = customer.getBothNames() + " <" + customer.getEmail() + ">";

			formatedCustomers.add(formatedCustomer);
		}

		while (true) {
			terminal.resetToBookmark(EMPTY_BOOKMARK);
			printHeader("Search customers by name or email. To list all customers, don't enter anything.");

			String searchCustomer = textIO.newStringInputReader().withMinLength(0).withMaxLength(100).read("Customer");

			ArrayList<String> filteredCustomers = new ArrayList<String>();

			if (!searchCustomer.isEmpty()) {
				for (String customer : formatedCustomers) {
					if (customer.contains(searchCustomer)) {
						filteredCustomers.add(customer);
					}
				}
			} else {
				filteredCustomers.addAll(formatedCustomers);
			}

			filteredCustomers.add(CUSTOMERS_MENU_LABEL_SEARCH_AGAIN);
			filteredCustomers.add(CUSTOMERS_MENU_LABEL_BACK);

			terminal.resetToBookmark(EMPTY_BOOKMARK);
			printHeader("Select customer who will take the book");

			String inputCustomer = textIO.newStringInputReader().withNumberedPossibleValues(filteredCustomers)
					.read("Customer");

			if (inputCustomer.equals(CUSTOMERS_MENU_LABEL_SEARCH_AGAIN)) {
				continue;
			} else if (inputCustomer.equals(CUSTOMERS_MENU_LABEL_BACK)) {
				break;
			} else {
				Customer selectedCustomer = null;

				for (Customer customer : customers) {
					if ((customer.getBothNames() + " <" + customer.getEmail() + ">").equals(inputCustomer)) {
						selectedCustomer = customer;
					}
				}

				if (selectedCustomer != null) {
					return selectedCustomer;
				} else {
					assert false : "Invalid selected customer";
				}
			}
		}

		return null;
	}

	public void showMatchedBooks(ArrayList<Book> books) {
		terminal.resetToBookmark(EMPTY_BOOKMARK);
		printHeader("Search results for \"" + library.getLastSearch() + "\"");

		ArrayList<String> formatedBooks = new ArrayList<String>();

		for (Book book : books) {
			String formatedBook = book.getName();

			formatedBooks.add(formatedBook);
		}

		formatedBooks.add(BOOK_MENU_LABEL_BACK);

		String bookTitle = textIO.newStringInputReader().withNumberedPossibleValues(formatedBooks)
				.read("Select book to process");

		if (!bookTitle.equals(BOOK_MENU_LABEL_BACK)) {

			Book selectedBook = null;

			for (Book book : books) {
				if (book.getName().equals(bookTitle)) {
					selectedBook = book;
				}
			}

			if (selectedBook != null) {
				runBookMenu(selectedBook);
			} else {
				assert false : "Invalid selected book";
			}

		}
	}

	public void showTakenBooks() {
		ArrayList<Book> books = library.getBooks();
		ArrayList<Book> takenBooks = new ArrayList<Book>();

		for (Book book : books) {
			if (!book.isAvailable()) {
				takenBooks.add(book);
			}
		}

		for (Book book : takenBooks) {
			terminal.println("'" + book.getName() + "'" + " taken by " + book.getCustomer().getBothNames() + " <"
					+ book.getCustomer().getEmail() + "> on " + book.getTakenDate() + ". Deadline - "
					+ book.getReturnDate());
		}

		showContinueScreen();
	}

	public void showAddBookScreen() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("Add new book");
	}

	public void showAddCustomerScreen() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("Add new customer");
	}

	public void showTakenBooksScreen() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("People who haven't returned books");
	}

	public void showFindBookByTitleScreen() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("Find book by title. To list all books don't enter anything.");
	}

	public void showCombinedSearchScreen() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader(
				"Combined search. Find book by title, author, issue date, publisher and language. To skip a filter don't enter anything.");
	}

	public void showSuccessScreen(String msg) {
		terminal.println("You've successfully " + msg + "!");
		getTextIO().newStringInputReader().withMinLength(0).read("Press Enter to continue");
	}

	public void showContinueScreen(String msg) {
		terminal.println(msg);
		getTextIO().newStringInputReader().withMinLength(0).read("Press Enter to continue");
	}

	public void showContinueScreen() {
		getTextIO().newStringInputReader().withMinLength(0).read("Press Enter to continue");
	}

	public boolean isConfirmed(String msg) {
		terminal.println("Are you sure you want to " + msg);
		String option = getTextIO().newStringInputReader().withNumberedPossibleValues("Yes", "No").read("Option");

		return option.equals("Yes");
	}

	public void showError(String msg, int code) {
		terminal.println("The program has encountered an error: " + msg + ", error code: " + code + ". Terminating...");
		getTextIO().newStringInputReader().withMinLength(0).read("Press Enter to exit...");
		System.exit(code);
	}
}
