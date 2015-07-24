package main.game.players.actions;

import main.Constants;
import main.game.items.Item;
import main.game.players.Player;

/**
 *
 * @author ArrowzFtw
 */
public class Bank {

	private Player c;

	public Bank(Player c) {
		this.c = c;
	}

	public int capacity() {
		return Constants.BANK_SIZE;
	}

	public int get(int i) {
		return c.getVariables().bankItems[i] - 1;
	}

	public int getFreeSlots() {
		int amount = 0;
		for (int i = 0; i < capacity(); i++) {
			if (c.getVariables().bankItems[i] > 0) {
				amount++;
			}
		}
		return capacity() - amount;
	}

	public int getSlot(int itemId) {
		for (int i = 0; i < capacity(); i++) {
			if (get(i) == itemId) {
				return i;
			}
		}
		return -1;
	}

	public Item getItem(int itemId) {
		for (int i = 0; i < capacity(); i++) {
			if (get(i) == itemId) {
				return new Item(get(i), c.getVariables().bankItemsN[i]);
			}
		}
		return null;
	}

	public void add(Item item) {
		c.getItems().addToBank(item.getId(), item.getCount());
	}

	public void remove(Item item) {

	}
}
