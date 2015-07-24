package main.game.players.content;

import main.game.players.Player;

/**
 * 
 * @author Alex
 *
 */
public class Pouch {

	/**
	 * Integers
	 */
	public long pouchAmount = 0;

	/**
	 * Strings
	 */
	public static final String addMessage = "been added to your money pouch.";
	public static final String removeMessage = "been removed from your money pouch.";

	/**
	 * Initializing c
	 */
	private Player c;

	public Pouch(Player c) {
		this.c = c;
	}

	/**
	 * Withdraws & Refreshes
	 */
	public void withdraw(int amount) {
		if (takeFromPouch(amount)) {
			// TODO Refresh
		} else {
			c.sendMessage("Returns false.");
		}
	}

	/**
	 * Adds & Refreshes
	 */
	public void add(int amount) {
		if (addToPouch(amount)) {
			// TODO Refresh
		} else {
			c.sendMessage("Returns false.");
		}
	}

	/**
	 * Take from the pouch
	 */
	public boolean takeFromPouch(int amount) {
		if (amount > 0 && pouchAmount > 0) {
			int coinAmount = c.getItems().getItemCount(995);
			if (pouchAmount >= amount && pouchAmount + amount <= Integer.MAX_VALUE
					&& pouchAmount + coinAmount <= Integer.MAX_VALUE) {
				c.sendMessage(message("Withdraw", amount));
				c.getItems().addItem(995, amount);
				pouchAmount -= amount;
				return true;
			} else if (pouchAmount + amount <= Integer.MAX_VALUE && pouchAmount + coinAmount <= Integer.MAX_VALUE) {
				c.sendMessage(message("Withdraw", (int) pouchAmount));
				c.getItems().addItem(995, (int) pouchAmount);
				pouchAmount = 0;
				return true;
			}
			if (pouchAmount + coinAmount > Integer.MAX_VALUE) {
				c.sendMessage(message("Withdraw", Integer.MAX_VALUE - (int) pouchAmount - coinAmount));
				c.getItems().deleteItem(995, coinAmount);
				c.getItems().addItem(995, Integer.MAX_VALUE);
				pouchAmount = (int) pouchAmount + coinAmount - Integer.MAX_VALUE;
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * Add to the pouch
	 */
	public boolean addToPouch(int amount) {
		if (amount > 0) {
			int coinAmount = c.getItems().getItemCount(995);
			if (c.getItems().playerHasItem(995, amount) && pouchAmount + amount <= Integer.MAX_VALUE) {
				c.sendMessage(message("Add", amount));
				c.getItems().deleteItem(995, amount);
				pouchAmount += amount;
				return true;
			} else if (c.getItems().playerHasItem(995, amount) && pouchAmount + amount > Integer.MAX_VALUE) {
				c.sendMessage(message("Add", (int) pouchAmount + amount - Integer.MAX_VALUE));
				c.getItems().deleteItem(995, coinAmount);
				c.getItems().addItem(995, Integer.MAX_VALUE - coinAmount);
				pouchAmount = Integer.MAX_VALUE;
				return true;
			}
		}
		return false;
	}

	private String message(String type, int amount) {
		if (amount == 0 && type == "Withdraw") {
			return "One coin has " + removeMessage;
		}
		if (amount == 0 && type == "Add") {
			return "One coin has " + addMessage;
		}
		if (type == "Withdraw") {
			return amount + " coins have " + removeMessage;
		}
		if (type == "Add") {
			return amount + " coins have " + addMessage;
		}
		return "";
	}

}
