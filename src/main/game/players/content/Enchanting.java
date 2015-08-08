package main.game.players.content;

import main.game.players.Player;

public class Enchanting {

	public static final int[][] ENCHANT = {
			// Unenchanted ring, unenchanted amulet, unenchanted necklace,
			// enchanted ring, enchanted amulet, enchanted necklace, rune1,
			// rune1 amount, rune2, rune2 amount, level required, xp, anim, gfx
			{ 1637, 1694, 1656, 2550, 1727, 3853, 555, 1, 0, 0, 7, 18, 719, 114 }, // sapphire
																					// enchant
			{ 1639, 1696, 1658, 2552, 1729, 5521, 556, 3, 0, 0, 27, 37, 719, 114 }, // emerald
																					// enchant
			{ 1641, 1698, 1660, 2568, 1725, 11194, 554, 5, 0, 0, 49, 59, 720, 115 }, // ruby
																						// enchant
			{ 1643, 1700, 1662, 2570, 1731, 11090, 557, 10, 0, 0, 57, 67, 720, 115 }, // diamond
																						// enchant
			{ 1645, 1702, 1664, 2572, 1712, 11105, 557, 15, 555, 15, 68, 78, 721, 116 }, // dragonstone
																							// enchant
			{ 6575, 6581, 6577, 6583, 6585, 11128, 557, 20, 554, 20, 87, 97, 721, 452 } // onyx
																						// enchant
	};

	public static String[] gems = { "Sapphire", "Emerald", "Ruby", "Diamond", "Dragonstone", "Onyx" };

	public static void enchant(Player c, int itemId) {
		boolean ring = false, amulet = false, necklace = false;
		int index = -1;
		for (int i = 0; i < ENCHANT.length; i++) {
			if (itemId == ENCHANT[i][0]) {
				index = i;
				ring = true;
				break;
			}
			if (itemId == ENCHANT[i][1]) {
				index = i;
				amulet = true;
				break;
			}
			if (itemId == ENCHANT[i][2]) {
				index = i;
				necklace = true;
				break;
			}
		}
		if (index == -1) {
			c.sendMessage("You cannot enchant this item.");
			return;
		}
		if (c.getInstance().playerLevel[c.getInstance().playerMagic] >= ENCHANT[index][10]) {
			if (c.getItems().playerHasItem(ENCHANT[index][6], ENCHANT[index][7])
					&& c.getItems().playerHasItem(ENCHANT[index][8], ENCHANT[index][9])
					&& c.getItems().playerHasItem(564, 1)) {
				c.startAnimation(ENCHANT[index][12]);
				c.gfx100(ENCHANT[index][13]);
				c.getItems().deleteItem(564, c.getItems().getItemSlot(564), 1);
				c.getItems().deleteItem(ENCHANT[index][8], c.getItems().getItemSlot(ENCHANT[index][8]),
						ENCHANT[index][9]);
				c.getItems().deleteItem(ENCHANT[index][6], c.getItems().getItemSlot(ENCHANT[index][6]),
						ENCHANT[index][7]);
				if (ring) {
					c.getItems().deleteItem(ENCHANT[index][0], 1);
					c.getItems().addItem(ENCHANT[index][3], 1);
				} else if (amulet) {
					c.getItems().deleteItem(ENCHANT[index][1], 1);
					c.getItems().addItem(ENCHANT[index][4], 1);
				} else if (necklace) {
					c.getItems().deleteItem(ENCHANT[index][2], 1);
					c.getItems().addItem(ENCHANT[index][5], 1);
				}
				c.getPA().addSkillXP(ENCHANT[index][11], c.getInstance().playerMagic);
				c.getPA().refreshSkill(c.getInstance().playerMagic);
				c.sendMessage("You enchant the " + gems[index] + " jewellery.");
			} else {
				c.sendMessage("You don't have the required runes to cast this spell.");
			}
		} else {
			c.sendMessage("You need a Magic level of " + ENCHANT[index][10] + " to use this enchantment.");
		}
	}

}
