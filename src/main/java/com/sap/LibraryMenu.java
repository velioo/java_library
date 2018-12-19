package com.sap;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class LibraryMenu {
	private TextIO textIO;
	private TextTerminal terminal;

	public LibraryMenu() {
		initializeMenu();
	}

	public void initializeMenu() {
		this.textIO = TextIoFactory.getTextIO();
		this.terminal = textIO.getTextTerminal();

		this.terminal.setBookmark("EMPTY");

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
		terminal.println("| " + message + " |");
		terminal.println(
				"----------------------------------------------------------------------------------------------------------------");
	}

	public void authorizeUser() {
		printHeader("Select Option");

		getTerminal().setBookmark("SELECT OPTION");

		String option = getTextIO().newStringInputReader().withNumberedPossibleValues("Register", "Login")
				.read("Option");

		// TODO: If register => show register screen else show login screen
	}
}
