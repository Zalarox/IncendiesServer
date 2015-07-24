package main.game.players.content;

import main.game.players.Player;

public class ItemsKeptOnDeath {

	public static void ResetKeepItems(Player c) {
		c.WillKeepAmt1 = -1;
		c.WillKeepItem1 = -1;
		c.WillKeepAmt2 = -1;
		c.WillKeepItem2 = -1;
		c.WillKeepAmt3 = -1;
		c.WillKeepItem3 = -1;
		c.WillKeepAmt4 = -1;
		c.WillKeepItem4 = -1;
	}

	public static void StartBestItemScan(Player p) {
		boolean protectOn = p.prayerActive[10] || p.curseActive[0];
		if (p.isSkulled && !protectOn) {
			ItemKeptInfo(p, 0);
			return;
		}
		FindItemKeptInfo(p);
		ResetKeepItems(p);
		BestItem1(p);
	}

	public static void FindItemKeptInfo(Player c) {
		boolean protectOn = c.prayerActive[10] || c.curseActive[0];
		if (c.isSkulled && protectOn) {
			ItemKeptInfo(c, 1);
		} else if (!c.isSkulled && !protectOn) {
			ItemKeptInfo(c, 3);
		} else if (!c.isSkulled && protectOn) {
			ItemKeptInfo(c, 4);
		}
	}

	public static void ItemKeptInfo(Player c, int Lose) {
		for (int i = 17109; i < 17131; i++) {
			c.getPA().sendString("", i);
		}
		c.getPA().sendString("Items you will keep on death:", 17104);
		c.getPA().sendString("Items you will lose on death:", 17105);
		c.getPA().sendString("Player Information", 17106);
		c.getPA().sendString("Max items kept on death:", 17107);
		c.getPA().sendString("~ " + Lose + " ~", 17108);
		c.getPA().sendString("The normal amount of", 17111);
		c.getPA().sendString("items kept is three.", 17112);
		switch (Lose) {
		case 0:
		default:
			c.getPA().sendString("Items you will keep on death:", 17104);
			c.getPA().sendString("Items you will lose on death:", 17105);
			c.getPA().sendString("You're marked with a", 17111);
			c.getPA().sendString("@red@skull. @lre@This reduces the", 17112);
			c.getPA().sendString("items you keep from", 17113);
			c.getPA().sendString("three to zero!", 17114);
			break;
		case 1:
			c.getPA().sendString("Items you will keep on death:", 17104);
			c.getPA().sendString("Items you will lose on death:", 17105);
			c.getPA().sendString("You're marked with a", 17111);
			c.getPA().sendString("@red@skull. @lre@This reduces the", 17112);
			c.getPA().sendString("items you keep from", 17113);
			c.getPA().sendString("three to zero!", 17114);
			c.getPA().sendString("However, you also have", 17115);
			c.getPA().sendString("the @red@Protect @lre@Items prayer", 17116);
			c.getPA().sendString("active, which saves you", 17117);
			c.getPA().sendString("one extra item!", 17118);
			break;
		case 3:
			c.getPA().sendString("Items you will keep on death(if not skulled):", 17104);
			c.getPA().sendString("Items you will lose on death(if not skulled):", 17105);
			c.getPA().sendString("You have no factors", 17111);
			c.getPA().sendString("affecting the items you", 17112);
			c.getPA().sendString("keep.", 17113);
			break;
		case 4:
			c.getPA().sendString("Items you will keep on death(if not skulled):", 17104);
			c.getPA().sendString("Items you will lose on death(if not skulled):", 17105);
			c.getPA().sendString("You have the @red@Protect", 17111);
			c.getPA().sendString("@red@Item @lre@prayer active,", 17112);
			c.getPA().sendString("which saves you one", 17113);
			c.getPA().sendString("extra item!", 17114);
			break;
		}
	}

	public static void BestItem1(Player c) {
		boolean protectOn = c.prayerActive[10] || c.curseActive[0];
		int BestValue = 0;
		int NextValue = 0;
		int ItemsContained = 0;
		c.WillKeepItem1 = 0;
		c.WillKeepItem1Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (c.playerItems[ITEM] > 0) {
				ItemsContained += 1;
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(c.playerItems[ITEM] - 1));
				if (NextValue > BestValue) {
					BestValue = NextValue;
					c.WillKeepItem1 = c.playerItems[ITEM] - 1;
					c.WillKeepItem1Slot = ITEM;
					if (c.playerItemsN[ITEM] > 2 && !protectOn) {
						c.WillKeepAmt1 = 3;
					} else if (c.playerItemsN[ITEM] > 3 && protectOn) {
						c.WillKeepAmt1 = 4;
					} else {
						c.WillKeepAmt1 = c.playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (c.playerEquipment[EQUIP] > 0) {
				ItemsContained += 1;
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(c.playerEquipment[EQUIP]));
				if (NextValue > BestValue) {
					BestValue = NextValue;
					c.WillKeepItem1 = c.playerEquipment[EQUIP];
					c.WillKeepItem1Slot = EQUIP + 28;
					if (c.playerEquipmentN[EQUIP] > 2 && !protectOn) {
						c.WillKeepAmt1 = 3;
					} else if (c.playerEquipmentN[EQUIP] > 3 && protectOn) {
						c.WillKeepAmt1 = 4;
					} else {
						c.WillKeepAmt1 = c.playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!c.isSkulled && ItemsContained > 1 && (c.WillKeepAmt1 < 3 || (protectOn && c.WillKeepAmt1 < 4))) {
			BestItem2(c, ItemsContained);
		}
	}

	public static void BestItem2(Player c, int ItemsContained) {
		boolean protectOn = c.prayerActive[10] || c.curseActive[0];
		int BestValue = 0;
		int NextValue = 0;
		c.WillKeepItem2 = 0;
		c.WillKeepItem2Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (c.playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(c.playerItems[ITEM] - 1));
				if (NextValue > BestValue
						&& !(ITEM == c.WillKeepItem1Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem1)) {
					BestValue = NextValue;
					c.WillKeepItem2 = c.playerItems[ITEM] - 1;
					c.WillKeepItem2Slot = ITEM;
					if (c.playerItemsN[ITEM] > 2 - c.WillKeepAmt1 && !protectOn) {
						c.WillKeepAmt2 = 3 - c.WillKeepAmt1;
					} else if (c.playerItemsN[ITEM] > 3 - c.WillKeepAmt1 && protectOn) {
						c.WillKeepAmt2 = 4 - c.WillKeepAmt1;
					} else {
						c.WillKeepAmt2 = c.playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (c.playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(c.playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == c.WillKeepItem1Slot && c.playerEquipment[EQUIP] == c.WillKeepItem1)) {
					BestValue = NextValue;
					c.WillKeepItem2 = c.playerEquipment[EQUIP];
					c.WillKeepItem2Slot = EQUIP + 28;
					if (c.playerEquipmentN[EQUIP] > 2 - c.WillKeepAmt1 && !protectOn) {
						c.WillKeepAmt2 = 3 - c.WillKeepAmt1;
					} else if (c.playerEquipmentN[EQUIP] > 3 - c.WillKeepAmt1 && protectOn) {
						c.WillKeepAmt2 = 4 - c.WillKeepAmt1;
					} else {
						c.WillKeepAmt2 = c.playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!c.isSkulled && ItemsContained > 2
				&& (c.WillKeepAmt1 + c.WillKeepAmt2 < 3 || (protectOn && c.WillKeepAmt1 + c.WillKeepAmt2 < 4))) {
			BestItem3(c, ItemsContained);
		}
	}

	public static void BestItem3(Player c, int ItemsContained) {
		boolean protectOn = c.prayerActive[10] || c.curseActive[0];
		int BestValue = 0;
		int NextValue = 0;
		c.WillKeepItem3 = 0;
		c.WillKeepItem3Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (c.playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(c.playerItems[ITEM] - 1));
				if (NextValue > BestValue
						&& !(ITEM == c.WillKeepItem1Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem1)
						&& !(ITEM == c.WillKeepItem2Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem2)) {
					BestValue = NextValue;
					c.WillKeepItem3 = c.playerItems[ITEM] - 1;
					c.WillKeepItem3Slot = ITEM;
					if (c.playerItemsN[ITEM] > 2 - (c.WillKeepAmt1 + c.WillKeepAmt2) && !protectOn) {
						c.WillKeepAmt3 = 3 - (c.WillKeepAmt1 + c.WillKeepAmt2);
					} else if (c.playerItemsN[ITEM] > 3 - (c.WillKeepAmt1 + c.WillKeepAmt2) && protectOn) {
						c.WillKeepAmt3 = 4 - (c.WillKeepAmt1 + c.WillKeepAmt2);
					} else {
						c.WillKeepAmt3 = c.playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (c.playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(c.playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == c.WillKeepItem1Slot && c.playerEquipment[EQUIP] == c.WillKeepItem1)
						&& !(EQUIP + 28 == c.WillKeepItem2Slot && c.playerEquipment[EQUIP] == c.WillKeepItem2)) {
					BestValue = NextValue;
					c.WillKeepItem3 = c.playerEquipment[EQUIP];
					c.WillKeepItem3Slot = EQUIP + 28;
					if (c.playerEquipmentN[EQUIP] > 2 - (c.WillKeepAmt1 + c.WillKeepAmt2) && !protectOn) {
						c.WillKeepAmt3 = 3 - (c.WillKeepAmt1 + c.WillKeepAmt2);
					} else if (c.playerEquipmentN[EQUIP] > 3 - c.WillKeepAmt1 && protectOn) {
						c.WillKeepAmt3 = 4 - (c.WillKeepAmt1 + c.WillKeepAmt2);
					} else {
						c.WillKeepAmt3 = c.playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!c.isSkulled && ItemsContained > 3 && protectOn
				&& ((c.WillKeepAmt1 + c.WillKeepAmt2 + c.WillKeepAmt3) < 4)) {
			BestItem4(c);
		}
	}

	public static void BestItem4(Player c) {
		int BestValue = 0;
		int NextValue = 0;
		c.WillKeepItem4 = 0;
		c.WillKeepItem4Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (c.playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(c.playerItems[ITEM] - 1));
				if (NextValue > BestValue
						&& !(ITEM == c.WillKeepItem1Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem1)
						&& !(ITEM == c.WillKeepItem2Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem2)
						&& !(ITEM == c.WillKeepItem3Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem3)) {
					BestValue = NextValue;
					c.WillKeepItem4 = c.playerItems[ITEM] - 1;
					c.WillKeepItem4Slot = ITEM;
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (c.playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(c.playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == c.WillKeepItem1Slot && c.playerEquipment[EQUIP] == c.WillKeepItem1)
						&& !(EQUIP + 28 == c.WillKeepItem2Slot && c.playerEquipment[EQUIP] == c.WillKeepItem2)
						&& !(EQUIP + 28 == c.WillKeepItem3Slot && c.playerEquipment[EQUIP] == c.WillKeepItem3)) {
					BestValue = NextValue;
					c.WillKeepItem4 = c.playerEquipment[EQUIP];
					c.WillKeepItem4Slot = EQUIP + 28;
				}
			}
		}
	}
}
