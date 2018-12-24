package com.sap;

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

	public LibraryMenu(Library library) {
		initializeMenu();
		this.library = library;
	}

	public void initializeMenu() {
		this.textIO = TextIoFactory.getTextIO();
		this.terminal = textIO.getTextTerminal();

		this.terminal.setBookmark(EMPTY_BOOKMARK);

		terminal.registerHandler("ctrl M", t -> {
			authorizeUser();
			return null;
		});

		// terminal.resetToBookmark("EMPTY");
		// String user = textIO.newStringInputReader()
		// .withDefaultValue("admin")
		// .read("Username");
		//
		// String password = textIO.newStringInputReader()
		// .withMinLength(6)
		// .withInputMasking(true)
		// .read("Password");
		//
		// int age = textIO.newIntInputReader()
		// .withMinVal(13)
		// .read("Age");
		//
		// Month month = textIO.newEnumInputReader(Month.class)
		// .read("What month were you born in?");
		//
		// TextTerminal terminal = textIO.getTextTerminal();
		// terminal.printf("\nUser %s is %d years old, was born in %s and has
		// the password %s.\n",
		// user, age, month, password);
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
		terminal.println("| " + message + " | Press Ctrl + M to go to Main Menu");
		terminal.println(
				"----------------------------------------------------------------------------------------------------------------");
	}

	public void authorizeUser() {
		terminal.resetToBookmark(EMPTY_BOOKMARK);

		printHeader("Select Option");
		terminal.setBookmark("SELECT OPTION");

		String option = getTextIO().newStringInputReader().withNumberedPossibleValues(REGISTER, LOGIN).read("Option");

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

	public void showError(String msg, int code) {
		terminal.println("The program has encountered an error: " + msg + ", error code: " + code + ". Terminating...");
		getTextIO().newStringInputReader().withMinLength(0).read("Press Enter to exit...");
		System.exit(code);
	}
}
