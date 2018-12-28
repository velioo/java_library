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
	private static final String BOOK_MENU_LABEL_AVAILABILITY = "Check availability";
	private static final String BOOK_MENU_LABEL_MARK_TAKEN = "Mark as taken";
	private static final String BOOK_MENU_LABEL_MARK_AVAILABLE = "Mark as available";
	private static final String BOOK_MENU_LABEL_SET_DEADLINE = "Set return deadline";
	private static final String BOOK_MENU_LABEL_REMOVE_BOOK = "Remove book";
	private static final String BOOK_MENU_LABEL_BACK = "Return to Search Menu";
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

		bookMenuPossibleValues.add(BOOK_MENU_LABEL_AVAILABILITY);
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
				showSuccessScreen("Added new book");
			} else if (option.equals(MAIN_MENU_LABEL_SEARCH_BOOK)) {
				runSearchMenu();
			} else if (option.equals(MAIN_MENU_LABEL_LIST_PEOPLE)) {
				// TODO:
				// showTakenBooksScreen();
			} else if (option.equals(MAIN_MENU_LABEL_ADD_NEW_CUSTOMER)) {
				showAddCustomerScreen();
				library.addNewCustomer();
				showSuccessScreen("Added new customer");
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
			printHeader("Select option");
			terminal.println("Selected book: '" + book.getName() + "' by " + book.getAuthor() + ", issue date - "
					+ book.getIssueDate() + ", publisher - " + book.getPublisher() + ", language - "
					+ book.getLanguage());

			terminal.println("Status: " + (book.isAvailable() ? "Available"
					: "Taken by " + book.getCustomer().getBothNames() + " on " + book.getTakenDate()));

			String option = textIO.newStringInputReader().withNumberedPossibleValues(bookMenuPossibleValues)
					.read("Option");

			if (option.equals(BOOK_MENU_LABEL_AVAILABILITY)) {

			} else if (option.equals(BOOK_MENU_LABEL_MARK_TAKEN)) {

			} else if (option.equals(BOOK_MENU_LABEL_MARK_AVAILABLE)) {

			} else if (option.equals(BOOK_MENU_LABEL_SET_DEADLINE)) {

			} else if (option.equals(BOOK_MENU_LABEL_REMOVE_BOOK)) {

				if (!book.isAvailable()) {
					showFailScreen("Cannot remove book which is taken by someone");
					continue;
				}

				library.removeBook(book);
				showSuccessScreen("Removed book '" + book.getName() + "'");
				break;
			} else if (option.equals(BOOK_MENU_LABEL_BACK)) {
				break;
			} else {
				assert false : "Invalid option";
			}
		}
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

	public void showAddBookScreen() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("Add new book");
	}

	public void showAddCustomerScreen() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("Add new customer");
	}

	public void showFindBookByTitleScreen() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("Find book by title");
	}

	public void showCombinedSearchScreen() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("Combined search. Find book by title, author, issue date, publisher and language");
	}

	public void showSuccessScreen(String msg) {
		terminal.println("You've successfully " + msg + "!");
		getTextIO().newStringInputReader().withMinLength(0).read("Press Enter to continue");
	}

	public void showFailScreen(String msg) {
		terminal.println(msg);
		getTextIO().newStringInputReader().withMinLength(0).read("Press Enter to continue");
	}

	public void showError(String msg, int code) {
		terminal.println("The program has encountered an error: " + msg + ", error code: " + code + ". Terminating...");
		getTextIO().newStringInputReader().withMinLength(0).read("Press Enter to exit...");
		System.exit(code);
	}
}
