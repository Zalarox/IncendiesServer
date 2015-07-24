/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.game.players.content.skills.crafting;

import main.game.items.Item;
import main.game.players.Player;

public class Tanning {

	/**
	 * Initializes the tanning screen for the player. This needs to be redone,
	 * and light up when people have the correct itens in inventory.
	 */
	public static void tanningInterface(Player player) {

		player.setStatedInterface("tanning");
		player.getPA().showInterface(14670);
		player.getPA().sendString("Soft Leather", 14777);
		if (player.getInventory().getItemAmount(995) >= 1)
			player.getPA().sendString("1 coins", 14785);
		else
			player.getPA().sendString("1 coins", 14785);
		player.getPA().sendString("Hard Leather", 14778);
		if (player.getInventory().getItemAmount(995) >= 3)
			player.getPA().sendString("3 coins", 14786);
		else
			player.getPA().sendString("3 coins", 14786);
		player.getPA().sendFrame246(14769, 250, 1741);
		player.getPA().sendFrame246(14770, 250, 1743);
		player.getPA().sendFrame246(14773, 250, 1753);
		player.getPA().sendFrame246(14774, 250, 1751);
		player.getPA().sendFrame246(14771, 250, 6287);
		player.getPA().sendFrame246(14775, 250, 1749);
		player.getPA().sendFrame246(14776, 250, 1747);
		// CBA... It's 3:10am So I'll Make Strings - Ian...
		player.getPA().sendString("Snakeskin", 14779);
		if (player.getInventory().getItemAmount(995) >= 15)
			player.getPA().sendString("15 coins", 14787);
		else
			player.getPA().sendString("15 coins", 14787);
		player.getPA().sendString("", 14780);
		player.getPA().sendString("", 14788);

		int[] Line = { 14781, 14789, 14783, 14791, 14782, 14790, 14784, 14792 };
		String[] HideColor = { "Green d'hide", "Red d'hide", "Blue d'hide", "Black d'hide" };
		String[] HideCost = { "20 coins", "20 coins", "20 coins", "20 coins" };
		int HC = 0;
		for (int i = 0; i < Line.length; i++) {
			if (HC == 0) {
				player.getPA().sendString(HideColor[(i / 2)], Line[i]);
				HC = 1;
			} else {
				if (player.getInventory().getItemAmount(995) >= 1)
					player.getPA().sendString(HideCost[(i / 2)], Line[i]);
				else
					player.getPA().sendString("" + HideCost[(i / 2)], Line[i]);
				HC = 0;
			}
		}

	}

	public static void tan(Player player, int amount, int payment, int deletedItem, int addedItem) {
		Item iPayment = new Item(995, payment * amount);
		player.getPA().removeInterfaces();

		if (player.getStatedInterface() != "tanning")
			return;
		if (player.getInventory().getItemContainer().contains(deletedItem)) {
			if (player.getInventory().getItemContainer().contains(995)) {
				if (amount > player.getInventory().getItemContainer().getCount(deletedItem)) {
					amount = player.getInventory().getItemContainer().getCount(deletedItem);
				}
				player.getInventory().removeItem(iPayment);
				player.getInventory().removeItem(new Item(deletedItem, amount));
				player.getInventory().addItem(new Item(addedItem, amount));
			} else {
				player.getDialogue().sendStatement("You do not have enough coins.");
			}
		} else {
			player.getDialogue().sendStatement("You don't have enough rough hides in your inventory.");
		}
	}

	public static void handleButtonsX(Player player, int buttonId, int amount) {
		switch (buttonId) {
		// soft
		case 57209:
			tan(player, amount, 1, 1739, 1741);
			break;
		// hard
		case 57210:
			tan(player, amount, 3, 1739, 1743);
			break;
		// snakeskin
		case 57211:
			tan(player, amount, 20, 6287, 6289);
			break;
		// green
		case 57212:
			tan(player, amount, 20, 1753, 1745);
			break;
		// blue
		case 57213:
			tan(player, amount, 20, 1751, 2505);
			break;
		// Red
		case 57214:
			tan(player, amount, 20, 1749, 2507);
			break;
		// Black
		case 57215:
			tan(player, amount, 20, 1747, 2509);
			break;

		}

	}

	public static void handleButtons(Player player, int buttonId) {
		// System.out.println(buttonId);
		switch (buttonId) {
		// soft
		case 57225:
			tan(player, 1, 1, 1739, 1741);
			break;
		case 57217:
			tan(player, 5, 1, 1739, 1741);
			break;
		case 57201:
			tan(player, player.getInventory().getItemContainer().getCount(1739), 1, 1739, 1741);
			break;
		// hard
		case 57226:
			tan(player, 1, 3, 1739, 1743);
			break;
		case 57218:
			tan(player, 5, 3, 1739, 1743);
			break;
		case 57202:
			tan(player, player.getInventory().getItemContainer().getCount(1739), 3, 1739, 1743);
			break;
		// snakeskin
		case 57229:
			tan(player, 1, 20, 6287, 6289);
			break;
		case 57221:
			tan(player, 5, 20, 6287, 6289);
			break;
		case 57205:
			tan(player, player.getInventory().getItemContainer().getCount(6287), 20, 6287, 6289);
			break;
		// green
		case 57230:
			tan(player, 1, 20, 1753, 1745);
			break;
		case 57222:
			tan(player, 5, 20, 1753, 1745);
			break;
		case 57206:
			tan(player, player.getInventory().getItemContainer().getCount(1753), 20, 1753, 1745);
			break;
		// blue
		case 57231:
			tan(player, 1, 20, 1751, 2505);
			break;
		case 57223:
			tan(player, 5, 20, 1751, 2505);
			break;
		case 57207:
			tan(player, player.getInventory().getItemContainer().getCount(1751), 20, 1751, 2505);
			break;
		// Red
		case 57232:
			tan(player, 1, 20, 1749, 2507);
			break;
		case 57224:
			tan(player, 5, 20, 1749, 2507);
			break;
		case 57208:
			tan(player, player.getInventory().getItemContainer().getCount(1749), 20, 1749, 2507);
			break;
		// Black
		case 57227:
			tan(player, 1, 20, 1747, 2509);
			break;
		case 57219:
			tan(player, 5, 20, 1747, 2509);
			break;
		case 57203:
			tan(player, player.getInventory().getItemContainer().getCount(1747), 20, 1747, 2509);
			break;

		}

	}

}
