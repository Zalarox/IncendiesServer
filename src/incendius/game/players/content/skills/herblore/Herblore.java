package incendius.game.players.content.skills.herblore;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.players.Player;
import incendius.handlers.SkillHandler;

/**
 * @author Ochroid | Scott
 */

public class Herblore extends SkillHandler {

	private static final int[][] CLEANING_HERB = { { 199, 249, 1, 100 }, { 201, 251, 5, 110 }, { 203, 253, 11, 120 },
			{ 205, 255, 20, 150 }, { 207, 257, 25, 200 }, { 209, 259, 30, 300 }, { 211, 261, 40, 400 },
			{ 213, 263, 54, 450 }, { 215, 265, 65, 600 }, { 217, 267, 70, 700 }, { 219, 269, 75, 800 },
			{ 3051, 3000, 59, 500 }, { 3049, 2998, 30, 310 }, { 2485, 2481, 67, 650 }, };

	private static final int[][] ITEM_ON_VIAL = { { 249, 91, 1 }, { 251, 93, 5 }, { 253, 95, 12 }, { 255, 97, 22 },
			{ 257, 99, 30 }, { 259, 101, 34 }, { 261, 103, 45 }, { 263, 105, 55 }, { 265, 107, 66 }, { 267, 109, 72 },
			{ 269, 111, 75 }, { 2481, 2483, 67 }, { 3000, 3004, 59 }, { 2998, 3002, 30 }, };

	private static final int[][] ITEM_ON_ITEM = { { 91, 221, 121, 1, 25 }, { 93, 235, 175, 5, 38 },
			{ 95, 225, 115, 12, 50 }, { 97, 223, 127, 22, 63 }, { 99, 239, 133, 30, 75 }, { 97, 9736, 9741, 36, 84 },
			{ 99, 231, 139, 38, 88 }, { 101, 221, 145, 45, 100 }, { 101, 235, 181, 48, 106 },
			{ 103, 231, 151, 50, 112 }, { 105, 225, 157, 55, 125 }, { 105, 241, 5940, 60, 137 },
			{ 3004, 223, 3026, 63, 142 }, { 107, 239, 163, 66, 150 }, { 2483, 241, 2454, 69, 158 },
			{ 109, 245, 169, 72, 163 }, { 2483, 3138, 3042, 76, 173 }, { 111, 247, 189, 78, 175 },
			{ 3002, 6693, 6687, 81, 180 }, { 157, 9594, 15313, 89, 230 }, { 145, 9594, 15309, 88, 220 },
			{ 163, 9594, 15317, 90, 240 }, { 3042, 9594, 15321, 91, 250 }, { 169, 9594, 15325, 92, 260 },
			{ 139, 9594, 15329, 94, 270 }, };

	public static boolean isHerb(int item) {
		for (int i = 0; i < CLEANING_HERB.length; i++) {
			if (item == CLEANING_HERB[i][0]) {
				return true;
			}
		}
		return false;
	}

	public static boolean mixPotion(int item, int item1) {
		for (int i = 0; i < ITEM_ON_VIAL.length; i++) {
			if (item == ITEM_ON_VIAL[i][0] || item1 == ITEM_ON_VIAL[i][0]) {
				return true;
			}
		}
		return false;
	}

	public static boolean mixPotionNew(int item, int item1) {
		for (int i = 0; i < ITEM_ON_ITEM.length; i++) {
			if (item == ITEM_ON_ITEM[i][0] || item1 == ITEM_ON_ITEM[i][0]) {
				return true;
			}
		}
		return false;
	}

	public static void cleanTheHerb(Player c, int itemId) {
		for (int i = 0; i < CLEANING_HERB.length; i++) {
			if (itemId == CLEANING_HERB[i][0]) {
				if (c.getInstance().playerLevel[c.playerHerblore] < CLEANING_HERB[i][2]) {
					c.sendMessage("You haven't got high enough Herblore level to clean this herb!");
					c.sendMessage("You need the Herblore level of " + CLEANING_HERB[i][2] + " to clean this herb.");
					c.getDH().sendStatement(
							"You need the herblore level of " + CLEANING_HERB[i][2] + " to clean this herb.");
					resetHerblore(c);
					return;
				}
				c.getPA().addSkillXP(CLEANING_HERB[i][3], c.playerHerblore);
				c.getItems().deleteItem(CLEANING_HERB[i][0], c.getItems().getItemSlot(CLEANING_HERB[i][0]), 1);
				c.getItems().addItem(CLEANING_HERB[i][1], 1);
				c.sendMessage("You clean the herb.");
			}
		}
	}

	public static void setUpUnfinished(Player c, int useItem, int otherItem) {
		c.getInstance().secondHerb = false;
		for (int i = 0; i < ITEM_ON_VIAL.length; i++) {
			if ((useItem == 227 && otherItem == ITEM_ON_VIAL[i][0])
					|| (useItem == ITEM_ON_VIAL[i][0] && otherItem == 227)) {
				if (c.getInstance().playerLevel[c.playerHerblore] < ITEM_ON_VIAL[i][2]) {
					c.sendMessage("You haven't got high enough Herblore level to make this potion!");
					c.sendMessage("You need the Herblore level of " + ITEM_ON_VIAL[i][2] + " to make this potion.");
					c.getDH().sendStatement(
							"You need the herblore level of " + ITEM_ON_VIAL[i][2] + " to make this potion.");
					resetHerblore(c);
					return;
				}
				c.getPA().sendFrame164(4429);
				c.getPA().sendFrame246(1746, 190, ITEM_ON_VIAL[i][1]);
				c.getPA().sendFrame126(getLine() + "" + c.getItems().getItemName(ITEM_ON_VIAL[i][1]) + "", 2799);
				c.getInstance().doingHerb = ITEM_ON_VIAL[i][0];
				c.getInstance().newHerb = ITEM_ON_VIAL[i][1];
				c.getInstance().newXp = 0;
			}
		}
	}

	public static void setUpPotion(Player c, int useItem, int otherItem) {
		c.getInstance().secondHerb = true;
		for (int i = 0; i < ITEM_ON_ITEM.length; i++) {
			if ((useItem == ITEM_ON_ITEM[i][1] && otherItem == ITEM_ON_ITEM[i][0])
					|| (useItem == ITEM_ON_ITEM[i][0] && otherItem == ITEM_ON_ITEM[i][1])) {
				if (c.getInstance().playerLevel[c.playerHerblore] < ITEM_ON_ITEM[i][3]) {
					c.sendMessage("You haven't got high enough Herblore level to make this potion!");
					c.sendMessage("You need the Herblore level of " + ITEM_ON_ITEM[i][3] + " to make this potion.");
					c.getDH().sendStatement(
							"You need the herblore level of " + ITEM_ON_ITEM[i][3] + " to make this potion.");
					resetHerblore(c);
					return;
				}
				c.getPA().sendFrame164(4429);
				c.getPA().sendFrame246(1746, 190, ITEM_ON_ITEM[i][2]);
				c.getPA().sendFrame126(getLine() + "" + c.getItems().getItemName(ITEM_ON_ITEM[i][2]) + "", 2799);
				c.getInstance().doingHerb = ITEM_ON_ITEM[i][0];
				c.getInstance().newHerb = ITEM_ON_ITEM[i][2];
				c.getInstance().newItem = ITEM_ON_ITEM[i][1];
				c.getInstance().newXp = ITEM_ON_ITEM[i][4];
			}
		}
	}

	public static void finishOverload(Player c) {
		if (c.getInstance().playerLevel[c.playerHerblore] < 96) {
			c.sendMessage("You haven't got high enough Herblore level to make this potion!");
			c.sendMessage("You need the Herblore level of 96 to make this potion.");
			c.getDH().sendStatement("You need a herblore level of 96 to make this potion.");
			resetHerblore(c);
			return;
		}
		c.getItems().addItem(15333, 1);
		c.getPA().addSkillXP(15000, c.playerHerblore);
		c.sendMessage("You combine all the extreme potions and manage to make a overload potion.");

	}

	public static void finishUnfinished(Player c, int amount) {
		if (c.getInstance().newHerb == -1 || c.getInstance().doingHerb == -1) {
			resetHerblore(c);
			return;
		}
		int item1 = c.getItems().getItemAmount(227);
		int item2 = c.getItems().getItemAmount(c.getInstance().doingHerb);
		if (amount > item2) {
			amount = item2;
		} else if (amount > item1) {
			amount = item1;
		}
		if (!c.getItems().playerHasItem(227, amount)) {
			c.sendMessage("You don't have enough vials of water to make that many!");
			resetHerblore(c);
			return;
		}
		if (!c.getItems().playerHasItem(c.getInstance().doingHerb, amount)) {
			c.sendMessage("You don't have enough herbs to make that many!");
			resetHerblore(c);
			return;
		}
		c.getInstance().doAmount = amount;
		makePotion(c, amount, 227, "un");
	}

	public static void finishPotion(Player c, int amount) {
		if (c.getInstance().newHerb == -1 || c.getInstance().doingHerb == -1) {
			resetHerblore(c);
			return;
		}
		int item1 = c.getItems().getItemAmount(c.getInstance().newItem);
		int item2 = c.getItems().getItemAmount(c.getInstance().doingHerb);
		if (amount > item2) {
			amount = item2;
		} else if (amount > item1) {
			amount = item1;
		}
		if (!c.getItems().playerHasItem(c.getInstance().newItem, amount)) {
			c.sendMessage("You don't have enough " + c.getItems().getItemName(c.getInstance().newItem)
					+ " to make that many!");
			resetHerblore(c);
			return;
		}
		if (!c.getItems().playerHasItem(c.getInstance().doingHerb, amount)) {
			c.sendMessage("You don't have enough unfinished potions to make that many!");
			resetHerblore(c);
			return;
		}
		c.getInstance().doAmount = amount;
		makePotion(c, amount, c.getInstance().newItem, "");
	}

	private static void makePotion(final Player c, final int amount, final int otherItem, final String s) {
		final int xp = (c.getInstance().newXp * SkillHandler.XPRates.HERBLORE.getXPRate());
		final int item1 = c.getInstance().doingHerb;
		final int item2 = otherItem;
		final int newItem1 = c.getInstance().newHerb;
		c.getInstance().stopPlayerSkill = true;
		c.startAnimation(363);
		c.getPA().removeAllWindows();
		if (c.getInstance().herbloreI) {
			return;
		}
		c.getInstance().herbloreI = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!c.getItems().playerHasItem(item1, 1) || !c.getItems().playerHasItem(item2, 1)
						|| c.getInstance().doAmount <= 0) {
					resetHerblore(c);
					container.stop();
					return;
				}
				if (!c.getInstance().stopPlayerSkill) {
					resetHerblore(c);
					container.stop();
					return;
				}
				c.getPA().addSkillXP(xp, c.playerHerblore);
				c.getItems().deleteItem(item1, c.getItems().getItemSlot(item1), 1);
				c.getItems().deleteItem(item2, c.getItems().getItemSlot(item2), 1);
				c.getItems().addItem(newItem1, 1);
				c.sendMessage("You make a " + c.getItems().getItemName(newItem1).toLowerCase() + ".");
				c.startAnimation(363);
				addTime(c);
			}

			@Override
			public void stop() {
			}
		}, 2);
	}

	public static void addTime(Player c) {
		c.getInstance().doAmount--;
	}

	public static void resetHerblore(Player c) {
		c.getPA().removeAllWindows();
		c.getInstance().newHerb = -1;
		c.getInstance().doingHerb = -1;
		c.getInstance().newXp = 0;
		c.getInstance().herbloreI = false;
		c.getInstance().herbAmount = -1;
		c.getInstance().newItem = -1;
		c.getInstance().secondHerb = false;
	}
}