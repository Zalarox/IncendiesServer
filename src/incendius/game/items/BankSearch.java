package incendius.game.items;

import incendius.Constants;
import incendius.game.players.Player;

/**
 *
 * @author ArrowzFtw
 */
public class BankSearch {

	public static int orderItems(Player c) {
		for (int i = 0; i < c.getInstance().bankArray.length; i++) {
			if (c.getInstance().bankArray[i] == null) {
				return i;
			}
		}
		return -1;
	}

	public static void clearItems(Player c) {
		for (int i = 0; i < c.getInstance().bankArray.length; i++) {
			if (c.getInstance().bankArray[i] != null) {
				c.getInstance().bankArray[i] = null;
			}
		}
	}

	/*
	 * Handles text input
	 * 
	 */

	public static void inputText(Player c, String text) {
		int matches = 0;
		clearItems(c);
		c.getInstance().bankText = text;
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			/*
			 * New item variable is opened due to loop
			 */
			GameItem bank = new GameItem(c.getInstance().bankItems[i], c.getInstance().bankItemsN[i]);

			if (bank.id == 0 || bank.id == -1) {
				continue;
			}

			if (orderItems(c) == -1) {
				continue;
			}
			/*
			 * Creates a Item bankArray of items
			 * 
			 */
			if (text == null) {
				System.out.println("Text is null: " + text);
				return;
			}
			c.getItems();
			String name = c.getItems().getItemName(bank.id).toLowerCase();
			text = text.toLowerCase();

			if (name.contains(text)) {
				c.getInstance().bankArray[orderItems(c)] = bank;
				matches += 1;
			}
		}
		c.sendMessage("Item matches found @whi@" + matches);
		c.getPA().sendItemOnInterface(5382, c.getInstance().bankArray);

	}
}
