package main.game.items;

import main.Constants;
import main.game.npcs.NPCHandler;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.content.minigames.impl.dueling.DuelPlayer;
import main.game.players.content.skills.runecrafting.Tiaras;
import main.handlers.ItemHandler;
import main.util.Misc;
import main.world.map.Region;

public class ItemAssistant {

	private Player c;

	public ItemAssistant(Player player) {
		this.c = player;
	}

	/**
	 * Adds an item to the bank without checking if the player has it in there
	 * inventory
	 * 
	 * @param itemId
	 *            the id of the item were banking
	 * @param amount
	 *            amount of items to bank
	 */
	public void addItemToBank(int itemId, int amount) {
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			if (c.getVariables().bankItems[i] <= 0 || c.getVariables().bankItems[i] == itemId + 1
					&& c.getVariables().bankItemsN[i] + amount < Integer.MAX_VALUE) {
				c.getVariables().bankItems[i] = itemId + 1;
				c.getVariables().bankItemsN[i] += amount;
				resetBank();
				return;
			}
		}
	}

	/**
	 * Items
	 **/
	public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 }, { 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 },
			{ 4720, 4896 }, { 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 }, { 4734, 4938 },
			{ 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 }, { 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 },
			{ 4745, 4956 }, { 4747, 4926 }, { 4749, 4968 }, { 4751, 4794 }, { 4753, 4980 }, { 4755, 4986 },
			{ 4757, 4992 }, { 4759, 4998 } };

	public void resetItems(int writeFrame, int[] array) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(writeFrame);
			c.getOutStream().writeWord(array.length);
			for (int i = 0; i < array.length; i++) {
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(array[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public int[][] skillcapes = { { 9747, 9748 }, // attack
			{ 9753, 9754 }, // defence
			{ 9750, 9751 }, // strength
			{ 9768, 9769 }, // hitpoints
			{ 9756, 9757 }, // range
			{ 9759, 9760 }, // prayer
			{ 9762, 9763 }, // magic
			{ 9801, 9802 }, // cooking
			{ 9807, 9808 }, // woodcutting
			{ 9783, 9784 }, // fletching
			{ 9798, 9799 }, // fishing
			{ 9804, 9805 }, // firemaking
			{ 9780, 9781 }, // crafting
			{ 9795, 9796 }, // smithing
			{ 9792, 9793 }, // mining
			{ 9774, 9775 }, // herblore
			{ 9771, 9772 }, // agility
			{ 9777, 9778 }, // thieving
			{ 9786, 9787 }, // slayer
			{ 9810, 9811 }, // farming
			{ 9765, 9766 } // runecraft
	};

	public void resetItems(int WriteFrame) {
		if (c.getAwaitingUpdate()) {
			c.setAwaitingUpdate(false);
		}
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(WriteFrame);
			c.getOutStream().writeWord(c.getVariables().playerItems.length);
			for (int i = 0; i < c.getVariables().playerItems.length; i++) {
				if (c.getVariables().playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.getVariables().playerItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.getVariables().playerItemsN[i]);
				}
				c.getOutStream().writeWordBigEndianA(c.getVariables().playerItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public String getItemName2(int i) {
		if (ItemHandler.ItemList[i] != null) {
			if (ItemHandler.ItemList[i].itemId == i) {
				return ItemHandler.ItemList[i].itemName;
			}
		}

		return "Unarmed";
	}

	public int getItemCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.getVariables().playerItems.length; j++) {
			if (c.getVariables().playerItems[j] == itemID + 1) {
				count += c.getVariables().playerItemsN[j];
			}
		}
		return count;
	}

	public void writeBonus() {
		int offset = 0;
		String send = "";
		for (int i = 0; i < c.getVariables().playerBonus.length; i++) {
			send = (c.getVariables().playerBonus[i] >= 0) ? BONUS_NAMES[i] + ": +" + c.getVariables().playerBonus[i]
					: BONUS_NAMES[i] + ": -" + java.lang.Math.abs(c.getVariables().playerBonus[i]);
			if (i == 10) {
				offset = 1;
			}
			c.getPA().sendString(send, (1675 + i + offset));
		}
		String[] soakingNames = { "Melee", "Magic", "Ranged" };
		for (int i = 0; i < c.getVariables().soakingBonus.length; i++) {
			String toSend = "Absorb " + soakingNames[i] + ": +" + (int) (c.getVariables().soakingBonus[i] * 100) + "%";
			c.getPA().sendString(toSend, 19149 + i);
		}
	}

	public int getTotalCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.getVariables().playerItems.length; j++) {
			if (ItemLoader.isNote(itemID + 1)) {
				if (itemID + 2 == c.getVariables().playerItems[j]) {
					count += c.getVariables().playerItemsN[j];
				}
			}
			if (!ItemLoader.isNote(itemID + 1)) {
				if (itemID + 1 == c.getVariables().playerItems[j]) {
					count += c.getVariables().playerItemsN[j];
				}
			}
		}
		for (int j = 0; j < c.getVariables().bankItems.length; j++) {
			if (c.getVariables().bankItems[j] == itemID + 1) {
				count += c.getVariables().bankItemsN[j];
			}
		}
		return count;
	}

	public void sendItemsKept() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6963);
			c.getOutStream().writeWord(c.getVariables().itemKeptId.length);
			for (int i = 0; i < c.getVariables().itemKeptId.length; i++) {
				if (c.getVariables().playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(1);
				} else {
					c.getOutStream().writeByte(1);
					c.getOutStream().writeWordBigEndianA(
							c.getVariables().itemKeptId[i] > 0 ? c.getVariables().itemKeptId[i] + 1 : 0);
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	/**
	 * Item kept on death
	 **/
	public void keepItem(int keepItem, boolean deleteItem) {
		int value = 0;
		int item = 0;
		int slotId = 0;
		boolean itemInInventory = false;
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			if (c.getVariables().playerItems[i] - 1 > 0) {
				int inventoryItemValue = c.getShops().getItemShopValue(c.getVariables().playerItems[i] - 1);
				if (inventoryItemValue > value && !c.getVariables().invSlot[i]) {
					value = inventoryItemValue;
					item = c.getVariables().playerItems[i] - 1;
					slotId = i;
					itemInInventory = true;
				}
			}
		}
		for (int i1 = 0; i1 < c.getVariables().playerEquipment.length; i1++) {
			if (c.getVariables().playerEquipment[i1] > 0) {
				int equipmentItemValue = c.getShops().getItemShopValue(c.getVariables().playerEquipment[i1]);
				if (equipmentItemValue > value && !c.getVariables().equipSlot[i1]) {
					value = equipmentItemValue;
					item = c.getVariables().playerEquipment[i1];
					slotId = i1;
					itemInInventory = false;
				}
			}
		}
		if (itemInInventory) {
			c.getVariables().invSlot[slotId] = true;
			if (deleteItem) {
				deleteItem(c.getVariables().playerItems[slotId] - 1,
						getItemSlot(c.getVariables().playerItems[slotId] - 1), 1);
			}
		} else {
			c.getVariables().equipSlot[slotId] = true;
			if (deleteItem) {
				deleteEquipment(item, slotId);
			}
		}
		c.getVariables().itemKeptId[keepItem] = item;
	}

	/**
	 * Reset items kept on death
	 **/
	public void resetKeepItems() {
		for (int i = 0; i < c.getVariables().itemKeptId.length; i++) {
			c.getVariables().itemKeptId[i] = -1;
		}
		for (int i1 = 0; i1 < c.getVariables().invSlot.length; i1++) {
			c.getVariables().invSlot[i1] = false;
		}
		for (int i2 = 0; i2 < c.getVariables().equipSlot.length; i2++) {
			c.getVariables().equipSlot[i2] = false;
		}
	}

	/**
	 * delete all items
	 **/
	public void deleteAllItems() {
		for (int i1 = 0; i1 < c.getVariables().playerEquipment.length; i1++) {
			if (c.getVariables().playerEquipment[i1] != 15707)
				deleteEquipment(c.getVariables().playerEquipment[i1], i1);
		}
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			if (c.getVariables().playerItems[i] - 1 != 15707)
				deleteItem(c.getVariables().playerItems[i] - 1, getItemSlot(c.getVariables().playerItems[i] - 1),
						c.getVariables().playerItemsN[i]);
		}
	}

	/**
	 * Pickup Item
	 **/

	public void removeGroundItem(int itemID, int itemX, int itemY, int Amount) {
		synchronized (c) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(156);
			c.getOutStream().writeByteS(0);
			c.getOutStream().writeWord(itemID);
			c.flushOutStream();
		}
	}

	/**
	 * Drop all items for your killer
	 **/

	public void dropAllItems() {
		// if(c.playerRights == 3 || c.playerRights == 2)
		// return;
		Player o = PlayerHandler.players[c.getVariables().killerId];
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			if (o != null) {
				if (tradeable(c.getVariables().playerItems[i] - 1)) {
					ItemHandler.createGroundItem(o, c.getVariables().playerItems[i] - 1, c.getX(), c.getY(),
							c.heightLevel, c.getVariables().playerItemsN[i], c.getVariables().killerId);
				} else {
					if (specialCase(c.getVariables().playerItems[i] - 1))
						ItemHandler.createGroundItem(o, 995, c.getX(), c.getY(), c.heightLevel,
								getUntradePrice(c.getVariables().playerItems[i] - 1), c.getVariables().killerId);
					ItemHandler.createGroundItem(c, c.getVariables().playerItems[i] - 1, c.getX(), c.getY(),
							c.heightLevel, c.getVariables().playerItemsN[i], c.playerId);
				}
			} else {
				ItemHandler.createGroundItem(c, c.getVariables().playerItems[i] - 1, c.getX(), c.getY(), c.heightLevel,
						c.getVariables().playerItemsN[i], c.playerId);
			}
		}
		for (int e = 0; e < c.getVariables().playerEquipment.length; e++) {
			if (o != null) {
				if (tradeable(c.getVariables().playerEquipment[e])) {
					ItemHandler.createGroundItem(o, c.getVariables().playerEquipment[e], c.getX(), c.getY(),
							c.heightLevel, c.getVariables().playerEquipmentN[e], c.getVariables().killerId);
				} else {
					if (specialCase(c.getVariables().playerEquipment[e]))
						ItemHandler.createGroundItem(o, 995, c.getX(), c.getY(), c.heightLevel,
								getUntradePrice(c.getVariables().playerEquipment[e]), c.getVariables().killerId);
					ItemHandler.createGroundItem(c, c.getVariables().playerEquipment[e], c.getX(), c.getY(),
							c.heightLevel, c.getVariables().playerEquipmentN[e], c.playerId);
				}
			} else {
				ItemHandler.createGroundItem(c, c.getVariables().playerEquipment[e], c.getX(), c.getY(), c.heightLevel,
						c.getVariables().playerEquipmentN[e], c.playerId);
			}
		}
		if (o != null) {
			ItemHandler.createGroundItem(o, 526, c.getX(), c.getY(), c.heightLevel, 1, c.getVariables().killerId);
		}
	}

	public int getUntradePrice(int item) {
		switch (item) {
		case 2518:
		case 2524:
		case 2526:
			return 100000;
		case 2520:
		case 2522:
			return 150000;
		}
		return 0;
	}

	public boolean specialCase(int itemId) {
		switch (itemId) {
		case 2518:
		case 2520:
		case 2522:
		case 2524:
		case 2526:
			return true;
		}
		return false;
	}

	public boolean tradeable(int itemId) {
		if (itemId == 995) {
			return true;
		}
		if (c.getShops().getItemShopValue(itemId) == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Add Item
	 **/
	public boolean addItem(int item, int amount) {
		if (amount < 1) {
			amount = 1;
		}
		if (item <= 0) {
			return false;
		}
		if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && ItemLoader.isStackable(item))
				|| ((freeSlots() > 0) && !ItemLoader.isStackable(item))) {
			for (int i = 0; i < c.getVariables().playerItems.length; i++) {
				if (c.getVariables().playerItems[i] == item + 1 && ItemLoader.isStackable(item)
						&& c.getVariables().playerItems[i] > 0) {
					c.getVariables().playerItems[i] = (item + 1);
					if (c.getVariables().playerItemsN[i] + amount < Constants.MAXITEM_AMOUNT
							&& c.getVariables().playerItemsN[i] + amount > -1) {
						c.getVariables().playerItemsN[i] += amount;
					} else {
						c.getVariables().playerItemsN[i] = Constants.MAXITEM_AMOUNT;
					}
					if (c.getOutStream() != null && c != null) {
						c.getOutStream().createFrameVarSizeWord(34);
						c.getOutStream().writeWord(3214);
						c.getOutStream().writeByte(i);
						c.getOutStream().writeWord(c.getVariables().playerItems[i]);
						if (c.getVariables().playerItemsN[i] > 254) {
							c.getOutStream().writeByte(255);
							c.getOutStream().writeDWord(c.getVariables().playerItemsN[i]);
						} else {
							c.getOutStream().writeByte(c.getVariables().playerItemsN[i]);
						}
						c.getOutStream().endFrameVarSizeWord();
						c.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < c.getVariables().playerItems.length; i++) {
				if (c.getVariables().playerItems[i] <= 0) {
					c.getVariables().playerItems[i] = item + 1;
					if (amount < Constants.MAXITEM_AMOUNT && amount > -1) {
						c.getVariables().playerItemsN[i] = 1;
						if (amount > 1) {
							c.getItems().addItem(item, amount - 1);
							return true;
						}
					} else {
						c.getVariables().playerItemsN[i] = Constants.MAXITEM_AMOUNT;
					}
					resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			resetItems(3214);
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
	}

	/**
	 * Bonuses
	 **/
	public final String[] BONUS_NAMES = { "Stab", "Slash", "Crush", "Magic", "Ranged", "Stab", "Slash", "Crush",
			"Magic", "Range", "Strength", "Prayer" };

	public void resetBonus() {
		for (int i = 0; i < c.getVariables().playerBonus.length; i++) {
			c.getVariables().playerBonus[i] = 0;
		}
		for (int i = 0; i < c.getVariables().soakingBonus.length; i++) {
			c.getVariables().soakingBonus[i] = 0;
		}
	}

	public void getBonus() {
		for (int i = 0; i < c.getVariables().playerEquipment.length; i++) {
			if (c.getVariables().playerEquipment[i] > -1) {
				int j = c.getVariables().playerEquipment[i];
				if (ItemHandler.ItemList[j] != null) {
					if (ItemHandler.ItemList[j].itemId == c.getVariables().playerEquipment[i]) {
						for (int k = 0; k < c.getVariables().playerBonus.length; k++) {
							c.getVariables().playerBonus[k] += ItemHandler.ItemList[j].Bonuses[k];
						}
					}
				}

			}
		}
		getSoakingBonus();
	}

	public void getSoakingBonus() {
		for (int i = 0; i < c.getVariables().playerEquipment.length; i++) {
			if (c.getVariables().playerEquipment[i] > -1) {
				int j = c.getVariables().playerEquipment[i];
				if (ItemHandler.ItemList[j] != null) {
					if (ItemHandler.ItemList[j].itemId == c.getVariables().playerEquipment[i]) {
						for (int k = 0; k < c.getVariables().soakingBonus.length; k++) {
							c.getVariables().soakingBonus[k] += ItemHandler.ItemList[j].soakingInfo[k];
						}
					}
				}

			}
		}
	}

	/**
	 * Wear Item
	 **/
	public void sendWeapon(int Weapon, String WeaponName) {
		c.getVariables().isFullHelm = ItemLoader
				.isFullHelm(c.getVariables().playerEquipment[c.getVariables().playerHat]);
		c.getVariables().isFullMask = ItemLoader
				.isFullMask(c.getVariables().playerEquipment[c.getVariables().playerHat]);
		c.getVariables().isFullBody = ItemLoader
				.isFullBody(c.getVariables().playerEquipment[c.getVariables().playerChest]);
		String WeaponName2 = WeaponName.replaceAll("Bronze", "");
		WeaponName2 = WeaponName2.replaceAll("Iron", "");
		WeaponName2 = WeaponName2.replaceAll("Steel", "");
		WeaponName2 = WeaponName2.replaceAll("Black", "");
		WeaponName2 = WeaponName2.replaceAll("Mithril", "");
		WeaponName2 = WeaponName2.replaceAll("Adamant", "");
		WeaponName2 = WeaponName2.replaceAll("Rune", "");
		WeaponName2 = WeaponName2.replaceAll("Granite", "");
		WeaponName2 = WeaponName2.replaceAll("Dragon", "");
		WeaponName2 = WeaponName2.replaceAll("Drag", "");
		WeaponName2 = WeaponName2.replaceAll("Crystal", "");
		WeaponName2 = WeaponName2.trim();
		// c.sendMessage(WeaponName2);
		if (WeaponName.equals("Unarmed")) {
			c.setSidebarInterface(0, 5855); // punch, kick, block
			c.getPA().sendString(WeaponName, 5857);
		} else if (WeaponName.endsWith("whip")) {
			c.setSidebarInterface(0, 12290); // flick, lash, deflect
			c.getPA().sendFrame246(12291, 200, Weapon);
			c.getPA().sendString(WeaponName, 12293);
		} else if (WeaponName.endsWith("bow") || WeaponName.endsWith("10") || WeaponName.endsWith("full")
				|| WeaponName.startsWith("seercull") || WeaponName.contains("cannon") || WeaponName.contains("javelin")
				|| WeaponName.contains("throwing")) {
			c.setSidebarInterface(0, 1764); // accurate, rapid, longrange
			c.getPA().sendFrame246(1765, 200, Weapon);
			c.getPA().sendString(WeaponName, 1767);
		} else
			if (WeaponName2.startsWith("dagger") || WeaponName2.contains("sword") && !WeaponName2.contains("godsword")
					&& !WeaponName2.contains("Vesta's") || WeaponName.endsWith("Staff of light")) {
			c.setSidebarInterface(0, 2276); // stab, lunge, slash, block
			c.getPA().sendFrame246(2277, 200, Weapon);
			c.getPA().sendString(WeaponName, 2279);
		} else if (WeaponName.endsWith("rapier")) {
			c.setSidebarInterface(0, 2276); // stab, lunge, slash, block
			c.getPA().sendFrame246(2277, 200, Weapon);
			c.getPA().sendString(WeaponName, 2279);
		} else if (WeaponName.startsWith("Staff") || WeaponName.endsWith("staff") || WeaponName.endsWith("wand")) {
			c.setSidebarInterface(0, 328); // spike, impale, smash, block
			c.getPA().sendFrame246(329, 200, Weapon);
			c.getPA().sendString(WeaponName, 331);
		} else if (WeaponName2.startsWith("dart") || WeaponName2.startsWith("knife")
				|| WeaponName2.startsWith("javelin") || WeaponName.equalsIgnoreCase("toktz-xil-ul")) {
			c.setSidebarInterface(0, 4446); // accurate, rapid, longrange
			c.getPA().sendFrame246(4447, 200, Weapon);
			c.getPA().sendString(WeaponName, 4449);
		} else if (WeaponName2.startsWith("pickaxe")) {
			c.setSidebarInterface(0, 5570); // spike, impale, smash, block
			c.getPA().sendFrame246(5571, 200, Weapon);
			c.getPA().sendString(WeaponName, 5573);
		} else if (WeaponName2.contains("axe")) {
			c.setSidebarInterface(0, 1698); // chop, hack, smash, block
			c.getPA().sendFrame246(1699, 200, Weapon);
			c.getPA().sendString(WeaponName, 1701);
		} else if (WeaponName2.contains("hatchet") || WeaponName2.endsWith("adze")) {
			c.setSidebarInterface(0, 1698); // chop, hack, smash, block
			c.getPA().sendFrame246(1699, 200, Weapon);
			c.getPA().sendString(WeaponName, 1701);
		} else if (WeaponName2.contains("claws")) {
			c.setSidebarInterface(0, 7762);
			c.getPA().sendFrame246(7763, 200, Weapon);
			c.getPA().sendString(WeaponName, 7765);
		} else if (WeaponName2.startsWith("halberd")) {
			c.setSidebarInterface(0, 8460); // jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendString(WeaponName, 8463);
		} else if (WeaponName2.startsWith("Scythe")) {
			c.setSidebarInterface(0, 8460); // jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendString(WeaponName, 8463);
		} else if (WeaponName2.contains("spear")) {
			c.setSidebarInterface(0, 4679); // lunge, swipe, pound, block
			c.getPA().sendFrame246(4680, 200, Weapon);
			c.getPA().sendString(WeaponName, 4682);
		} else if (WeaponName2.toLowerCase().contains("mace")) {
			c.setSidebarInterface(0, 3796);
			c.getPA().sendFrame246(3797, 200, Weapon);
			c.getPA().sendString(WeaponName, 3799);

		} else if (WeaponName2.contains("maul") || WeaponName2.contains("hammer")) {
			c.setSidebarInterface(0, 425); // war hamer equip.
			c.getPA().sendFrame246(426, 200, Weapon);
			c.getPA().sendString(WeaponName, 428);
		} else {
			c.setSidebarInterface(0, 2423); // chop, slash, lunge, block
			c.getPA().sendFrame246(2424, 200, Weapon);
			c.getPA().sendString(WeaponName, 2426);
		}

	}

	/**
	 * Weapon Requirements
	 **/
	public void getRequirements(String itemName, int itemId) {
		c.getVariables().attackLevelReq = c.getVariables().defenceLevelReq = c.getVariables().strengthLevelReq = c
				.getVariables().rangeLevelReq = c.getVariables().prayerLevelReq = c.getVariables().magicLevelReq = 0;
		if (itemName.contains("mystic") || itemName.contains("nchanted")) {
			if (itemName.contains("staff")) {
				c.getVariables().magicLevelReq = 20;
				c.getVariables().attackLevelReq = 40;
			} else {
				c.getVariables().magicLevelReq = 20;
				c.getVariables().defenceLevelReq = 20;
			}
		}
		if (itemName.contains("infinity")) {
			c.getVariables().magicLevelReq = 50;
			c.getVariables().defenceLevelReq = 25;
		}
		if (itemName.contains("rune c'bow")) {
			c.getVariables().rangeLevelReq = 61;
		}
		if (itemName.contains("gilded")) {
			c.getVariables().defenceLevelReq = 40;
		}
		if (itemName.contains("splitbark")) {
			c.getVariables().magicLevelReq = 40;
			c.getVariables().defenceLevelReq = 40;
		}
		if (itemName.contains("Green")) {
			if (itemName.contains("hide")) {
				c.getVariables().rangeLevelReq = 40;
				if (itemName.contains("body")) {
					c.getVariables().defenceLevelReq = 40;
				}
				return;
			}
		}
		if (itemName.contains("Blue")) {
			if (itemName.contains("hide")) {
				c.getVariables().rangeLevelReq = 50;
				if (itemName.contains("body")) {
					c.getVariables().defenceLevelReq = 40;
				}
				return;
			}
		}
		if (itemName.contains("Red")) {
			if (itemName.contains("hide")) {
				c.getVariables().rangeLevelReq = 60;
				if (itemName.contains("body")) {
					c.getVariables().defenceLevelReq = 40;
				}
				return;
			}
		}
		if (itemName.contains("Black")) {
			if (itemName.contains("hide")) {
				c.getVariables().rangeLevelReq = 70;
				if (itemName.contains("body")) {
					c.getVariables().defenceLevelReq = 40;
				}
				return;
			}
		}
		if (itemName.contains("bronze")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.getVariables().attackLevelReq = c.getVariables().defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("iron")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.getVariables().attackLevelReq = c.getVariables().defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("steel")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.getVariables().attackLevelReq = c.getVariables().defenceLevelReq = 5;
			}
			return;
		}
		/*
		 * if(itemName.contains("black")) { if(!itemName.contains("knife") &&
		 * !itemName.contains("dart") && !itemName.contains("javelin") &&
		 * !itemName.contains("thrownaxe") && !itemName.contains("vamb") &&
		 * !itemName.contains("chap")) { c.getVariables().attackLevelReq =
		 * c.getVariables().defenceLevelReq = 10; } return; }
		 */
		if (itemName.contains("mithril")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.getVariables().attackLevelReq = c.getVariables().defenceLevelReq = 20;
			}
			return;
		}
		if (itemName.contains("adamant")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.getVariables().attackLevelReq = c.getVariables().defenceLevelReq = 30;
			}
			return;
		}
		if (itemName.contains("rune")) {
			if (!itemName.contains("knife") && !itemName.contains("crossbow") && !itemName.contains("dart")
					&& !itemName.contains("javelin") && !itemName.contains("thrownaxe") && !itemName.contains("'bow")) {
				c.getVariables().attackLevelReq = c.getVariables().defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("dragon")) {
			if (!itemName.contains("nti-") && !itemName.contains("fire")) {
				c.getVariables().attackLevelReq = c.getVariables().defenceLevelReq = 60;
				return;
			}
		}
		if (itemName.contains("crystal")) {
			if (itemName.contains("shield")) {
				c.getVariables().defenceLevelReq = 70;
			} else {
				c.getVariables().rangeLevelReq = 70;
			}
			return;
		}
		if (itemName.contains("ahrim")) {
			if (itemName.contains("staff")) {
				c.getVariables().attackLevelReq = 70;
			} else {
				c.getVariables().defenceLevelReq = 70;
			}
			c.getVariables().magicLevelReq = 70;
		}
		if (itemName.contains("dagon")) {
			c.getVariables().magicLevelReq = 40;
			c.getVariables().defenceLevelReq = 20;
		}
		if (itemName.contains("stream")) {
			c.getVariables().magicLevelReq = 70;
		}
		if (itemName.contains("initiate")) {
			c.getVariables().defenceLevelReq = 20;
		}
		if (itemName.contains("chaotic")) {
			if (itemName.contains("shield")) {
				c.getVariables().defenceLevelReq = 80;
			} else {
				c.getVariables().attackLevelReq = 80;
			}
		}
		if (itemName.contains("vesta") && itemName.contains("corrupt")) {
			if (itemName.contains("longsword") || itemName.contains("spear")) {
				c.getVariables().attackLevelReq = 20;
			} else {
				c.getVariables().defenceLevelReq = 20;
			}
		}
		if (itemName.contains("vesta") && !itemName.contains("corrupt")) {
			if (itemName.contains("longsword") || itemName.contains("spear")) {
				c.getVariables().attackLevelReq = 78;
			} else {
				c.getVariables().defenceLevelReq = 78;
			}
		}
		if (itemName.contains("statius") && itemName.contains("corrupt")) {
			if (itemName.contains("warhammer")) {
				c.getVariables().attackLevelReq = 20;
			} else {
				c.getVariables().defenceLevelReq = 20;
			}
		}
		if (itemName.contains("statius") && !itemName.contains("corrupt")) {
			if (itemName.contains("warhammer")) {
				c.getVariables().attackLevelReq = 78;
			} else {
				c.getVariables().defenceLevelReq = 78;
			}
		}
		if (itemName.contains("zuriel") && itemName.contains("corrupt")) {
			if (itemName.contains("staff")) {
				c.getVariables().attackLevelReq = 20;
			} else {
				c.getVariables().defenceLevelReq = 20;
			}
			c.getVariables().magicLevelReq = 20;
		}
		if (itemName.contains("zuriel") && !itemName.contains("corrupt")) {
			if (itemName.contains("staff")) {
				c.getVariables().attackLevelReq = 78;
			} else {
				c.getVariables().defenceLevelReq = 78;
			}
			c.getVariables().magicLevelReq = 78;
		}
		if (itemName.contains("morrigan") && itemName.contains("corrupt")) {
			if (itemName.contains("javelin")) {
				c.getVariables().rangeLevelReq = 20;
			} else {
				c.getVariables().rangeLevelReq = 20;
				c.getVariables().defenceLevelReq = 20;
			}
		}
		if (itemName.contains("morrigan") && !itemName.contains("corrupt")) {
			if (itemName.contains("javelin")) {
				c.getVariables().rangeLevelReq = 78;
			} else {
				c.getVariables().rangeLevelReq = 78;
				c.getVariables().defenceLevelReq = 78;
			}
		}
		if (itemName.contains("karil")) {
			if (itemName.contains("crossbow")) {
				c.getVariables().rangeLevelReq = 70;
			} else {
				c.getVariables().rangeLevelReq = 70;
				c.getVariables().defenceLevelReq = 70;
			}
		}
		if (itemName.contains("elite")) {
			c.getVariables().defenceLevelReq = 40;
		}

		if (itemName.contains("guthix plate")) {
			c.getVariables().defenceLevelReq = 40;
		} else if (itemName.contains("guthix full")) {
			c.getVariables().defenceLevelReq = 40;
		} else if (itemName.contains("guthix kite")) {
			c.getVariables().defenceLevelReq = 40;
		}

		if (itemName.contains("zamorak plate")) {
			c.getVariables().defenceLevelReq = 40;
		} else if (itemName.contains("zamorak full")) {
			c.getVariables().defenceLevelReq = 40;
		} else if (itemName.contains("zamorak kite")) {
			c.getVariables().defenceLevelReq = 40;
		}

		if (itemName.contains("saradomin plate")) {
			c.getVariables().defenceLevelReq = 40;
		} else if (itemName.contains("saradomin full")) {
			c.getVariables().defenceLevelReq = 40;
		} else if (itemName.contains("saradomin kite")) {
			c.getVariables().defenceLevelReq = 40;
		}

		if (itemName.contains("torva")) {
			c.getVariables().defenceLevelReq = 80;
		}

		if (itemName.contains("pernix")) {
			c.getVariables().defenceLevelReq = 80;
			c.getVariables().rangeLevelReq = 80;
		}

		if (itemName.contains("virtus")) {
			c.getVariables().defenceLevelReq = 80;
			c.getVariables().magicLevelReq = 80;
		}
		if (itemName.contains("godsword")) {
			c.getVariables().attackLevelReq = 75;
		}
		if (itemName.contains("3rd age") && !itemName.contains("amulet")) {
			c.getVariables().defenceLevelReq = 60;
		}
		if (itemName.contains("Initiate")) {
			c.getVariables().defenceLevelReq = 20;
		}
		if (itemName.contains("proselyte")) {
			c.getVariables().defenceLevelReq = 30;
		}
		if (itemName.contains("rock-shell")) {
			c.getVariables().defenceLevelReq = 40;
		}
		if (itemName.contains("verac") || itemName.contains("guthan") || itemName.contains("dharok")
				|| itemName.contains("torag")) {

			if (itemName.contains("hammers")) {
				c.getVariables().attackLevelReq = 70;
				c.getVariables().strengthLevelReq = 70;
			} else if (itemName.contains("axe")) {
				c.getVariables().attackLevelReq = 70;
				c.getVariables().strengthLevelReq = 70;
			} else if (itemName.contains("warspear")) {
				c.getVariables().attackLevelReq = 70;
				c.getVariables().strengthLevelReq = 70;
			} else if (itemName.contains("flail")) {
				c.getVariables().attackLevelReq = 70;
				c.getVariables().strengthLevelReq = 70;
			} else {
				c.getVariables().defenceLevelReq = 70;
			}
		}

		switch (itemId) {
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			c.getVariables().attackLevelReq = 42;
			c.getVariables().rangeLevelReq = 42;
			c.getVariables().strengthLevelReq = 42;
			c.getVariables().magicLevelReq = 42;
			c.getVariables().defenceLevelReq = 42;
			return;
		case 6528:
			c.getVariables().strengthLevelReq = 60;
			return;
		case 5698:
			c.getVariables().attackLevelReq = 60;
			return;
		case 18335:
			c.getVariables().magicLevelReq = 70;
			return;
		case 11283:
			c.getVariables().defenceLevelReq = 75;
			return;
		case 4675:
			c.getVariables().attackLevelReq = 50;
			c.getVariables().magicLevelReq = 50;
			return;
		case 15486:
			c.getVariables().attackLevelReq = 75;
			c.getVariables().magicLevelReq = 75;
			return;
		case 11730:
		case 11716:
			c.getVariables().attackLevelReq = 70;
			return;
		case 13742:
		case 13740:
			c.getVariables().defenceLevelReq = 75;
			c.getVariables().prayerLevelReq = 75;
			return;
		case 13734:
			c.getVariables().defenceLevelReq = 40;
			c.getVariables().prayerLevelReq = 55;
			return;
		case 13736:
			c.getVariables().defenceLevelReq = 70;
			c.getVariables().prayerLevelReq = 60;
			return;
		case 13744:
		case 13738:
			c.getVariables().defenceLevelReq = 75;
			c.getVariables().magicLevelReq = 65;
			c.getVariables().prayerLevelReq = 70;
			return;
		case 10548:
		case 10551:
		case 2501:
		case 2499:
		case 1135:
			c.getVariables().defenceLevelReq = 40;
			return;
		case 11235:
		case 6522:
			c.getVariables().rangeLevelReq = 60;
			break;
		case 6524:
			c.getVariables().defenceLevelReq = 60;
			break;
		case 11284:
			c.getVariables().defenceLevelReq = 75;
			return;
		case 6889:
		case 6914:
			c.getVariables().magicLevelReq = 60;
			break;
		case 861:
			c.getVariables().rangeLevelReq = 50;
			break;
		case 10828:
		case 12681:
			c.getVariables().defenceLevelReq = 55;
			break;
		case 11724:
		case 11726:
		case 11728:
			c.getVariables().defenceLevelReq = 65;
			break;
		case 3751:
		case 3749:
		case 3755:
		case 3753:
		case 12677:
		case 12679:
		case 12673:
		case 12675:
			c.getVariables().defenceLevelReq = 45;
			break;
		case 2497:
			c.getVariables().rangeLevelReq = 70;
			break;
		case 2412:
		case 2413:
		case 2414:
			c.getVariables().magicLevelReq = 60;
			break;
		case 9185:
			c.getVariables().rangeLevelReq = 61;
			break;
		case 2503:
			c.getVariables().defenceLevelReq = 40;
			c.getVariables().rangeLevelReq = 70;
			break;

		case 7462:
		case 7461:
			c.getVariables().defenceLevelReq = 40;
			break;
		case 8846:
			c.getVariables().defenceLevelReq = 5;
			break;
		case 8847:
			c.getVariables().defenceLevelReq = 10;
			break;
		case 8848:
			c.getVariables().defenceLevelReq = 20;
			break;
		case 8849:
			c.getVariables().defenceLevelReq = 30;
			break;
		case 8850:
			c.getVariables().defenceLevelReq = 40;
			break;
		case 11200:
			c.getVariables().defenceLevelReq = 50;
			break;
		case 7460:
			c.getVariables().defenceLevelReq = 40;
			break;

		case 837:
			c.getVariables().rangeLevelReq = 61;
			break;

		case 4151:
		case 15445:
		case 15444:
		case 15443:
		case 15442:
		case 15441:
			c.getVariables().attackLevelReq = 70;
			return;

		case 6724: // seercull
			c.getVariables().rangeLevelReq = 60; // idk if that is correct
			return;
		case 4153:
			c.getVariables().attackLevelReq = 50;
			c.getVariables().strengthLevelReq = 50;
			return;
		}
	}

	/**
	 * two handed weapon check
	 **/
	public boolean is2handed(String itemName, int itemId) {
		if (itemName.contains("ahrim") || itemName.contains("karil") || itemName.contains("verac")
				|| itemName.contains("guthan") || itemName.contains("dharok") || itemName.contains("torag")) {
			return true;
		}
		if (itemName.contains("longbow") || itemName.contains("shortbow") || itemName.contains("ark bow")) {
			return true;
		}
		if (itemName.contains("crystal")) {
			return true;
		}
		if (itemName.contains("godsword") || itemName.contains("aradomin sword") || itemName.contains("2h")
				|| itemName.contains("spear") || itemName.contains("maul")) {
			return true;
		}
		switch (itemId) {
		case 6724: // seercull
		case 11730:
		case 4153:
		case 6528:
		case 14484:
		case 15241:
			return true;
		}
		return false;
	}

	/**
	 * Weapons special bar, adds the spec bars to weapons that require them and
	 * removes the spec bars from weapons which don't require them
	 **/
	public void addSpecialBar(int weapon) {
		switch (weapon) {
		case 14484: // Dragon claws
			c.getPA().sendFrame171(0, 7800);
			specialAmount(weapon, c.getVariables().specAmount, 7812);
			break;
		case 15441: // whip
		case 15442: // whip
		case 15443: // whip
		case 15444: // whip
		case 4151: // whip
			c.getPA().sendFrame171(0, 12323);
			specialAmount(weapon, c.getVariables().specAmount, 12335);
			break;

		case 859: // Magic bows
		case 861:
		case 11235: // Dark bow
		case 15241: // Hand cannon
		case 13883: // morrigan throwing axe
		case 13879: // Morrigan Javeline
			c.getPA().sendFrame171(0, 7549);
			specialAmount(weapon, c.getVariables().specAmount, 7561);
			break;

		case 4587: // dscimmy
		case 10887:
		case 11694:
		case 11698:
		case 11700:
		case 11730:
		case 11696:
		case 13899:
			c.getPA().sendFrame171(0, 7599);
			specialAmount(weapon, c.getVariables().specAmount, 7611);
			break;

		case 3204: // d hally
			c.getPA().sendFrame171(0, 8493);
			specialAmount(weapon, c.getVariables().specAmount, 8505);
			break;

		case 1377: // d battleaxe
			c.getPA().sendFrame171(0, 7499);
			specialAmount(weapon, c.getVariables().specAmount, 7511);
			break;

		case 4153: // gmaul
		case 13902:
			c.getPA().sendFrame171(0, 7474);
			specialAmount(weapon, c.getVariables().specAmount, 7486);
			break;

		case 1249: // dspear
		case 13905:
			c.getPA().sendFrame171(0, 7674);
			specialAmount(weapon, c.getVariables().specAmount, 7686);
			break;

		case 1215:// dragon dagger
		case 1231:
		case 5680:
		case 5698:
		case 15486:
		case 19784:
		case 19780: // korasi
		case 1305: // dragon long
			c.getPA().sendFrame171(0, 7574);
			specialAmount(weapon, c.getVariables().specAmount, 7586);
			break;

		case 1434: // dragon mace
			c.getPA().sendFrame171(0, 7624);
			specialAmount(weapon, c.getVariables().specAmount, 7636);
			break;

		default:
			c.getPA().sendFrame171(1, 7624); // mace interface
			c.getPA().sendFrame171(1, 7474); // hammer, gmaul
			c.getPA().sendFrame171(1, 7499); // axe
			c.getPA().sendFrame171(1, 7549); // bow interface
			c.getPA().sendFrame171(1, 7574); // sword interface
			c.getPA().sendFrame171(1, 7599); // scimmy sword interface, for most
												// swords
			c.getPA().sendFrame171(1, 8493);
			c.getPA().sendFrame171(1, 12323); // whip interface
			break;
		}
	}

	/**
	 * Specials bar filling amount
	 **/
	public void specialAmount(int weapon, double specAmount, int barId) {
		c.getVariables().specBarId = barId;
		c.getPA().sendFrame70(specAmount >= 10 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 9 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 8 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 7 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 6 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 5 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 4 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 3 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 2 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 1 ? 500 : 0, 0, (--barId));
		updateSpecialBar();
		sendWeapon(weapon, getItemName(weapon));
	}

	/**
	 * Special attack text and what to highlight or blackout
	 **/
	public void updateSpecialBar() {
		if (c.getVariables().usingSpecial && c.getVariables().playerEquipment[c.getVariables().playerWeapon] != 15050) {
			c.getPA().sendString("@yel@ Special Attack (" + (int) c.getVariables().specAmount * 10 + "%)",
					c.getVariables().specBarId);
		} else {
			c.getPA().sendString("@bla@ Special Attack (" + (int) c.getVariables().specAmount * 10 + "%)",
					c.getVariables().specBarId);
		}
	}

	/**
	 * Wear Item
	 **/
	public int targetSlot(String item) {
		int targetSlot = -1;
		for (int i = 0; i < HATS.length; i++) {
			if (item.contains(HATS[i])) {
				targetSlot = 0;
			}
		}
		for (int i = 0; i < CAPES.length; i++) {
			if (item.contains(CAPES[i])) {
				targetSlot = 1;
			}
		}
		for (int i = 0; i < AMULETS.length; i++) {
			if (item.contains(AMULETS[i])) {
				targetSlot = 2;
			}
		}
		for (int i = 0; i < WEAPONS.length; i++) {
			if (item.contains(WEAPONS[i])) {
				targetSlot = 3;
			}
		}
		for (int i = 0; i < BODY.length; i++) {
			if (item.contains(BODY[i])) {
				targetSlot = 4;
			}
		}
		for (int i = 0; i < SHIELDS.length; i++) {
			if (item.contains(SHIELDS[i])) {
				targetSlot = 5;
			}
		}
		for (int i = 0; i < LEGS.length; i++) {
			if (item.contains(LEGS[i])) {
				targetSlot = 7;
			}
		}
		for (int i = 0; i < GLOVES.length; i++) {
			if (item.contains(GLOVES[i])) {
				targetSlot = 9;
			}
		}
		for (int i = 0; i < BOOTS.length; i++) {
			if (item.contains(BOOTS[i])) {
				targetSlot = 10;
			}
		}
		for (int i = 0; i < RINGS.length; i++) {
			if (item.contains(RINGS[i])) {
				targetSlot = 12;
			}
		}
		for (int i = 0; i < ARROWS.length; i++) {
			if (item.contains(ARROWS[i])) {
				targetSlot = 13;
			}
		}
		return targetSlot;
	}

	public static String[] HATS = { "sallet", "hat", "helm", "helmet", "full slayer helmet", "visor", "mask", "coif",
			"hood", "headdress", "cowl", "bandana" };
	public static String[] CAPES = { "cape", "tokhaar", "accumulator", "attractor", "cloak", "alerter", "kal" };
	public static String[] AMULETS = { "amulet", "necklace" };
	public static String[] WEAPONS = { "hand", "mace", "dart", "knife", "javelin", "scythe", "claws", "bow", "crossbow",
			"c' bow", "hatchet", "adze", "axe", "sword", "rapier", "scimitar", "spear", "dagger", "staff", "wand",
			"blade", "whip", "silverlight", "darklight", "maul", "halberd", "anchor", "tzhaar-ket-om", "hammer",
			"hand cannon" };
	public static String[] BODY = { "hauberk", "torso", "body", "poncho", "top", "platebody", "chainbody", "shirt",
			"chestplate", "guthix dragonhide", "zamorak dragonhide", "saradomin dragonhide", "brassard" };
	public static String[] SHIELDS = { "shield", "book", "defender", "toktz-ket-xil" };
	public static String[] LEGS = { "cuisse", "chaps", "platelegs", "skirt", "tassets", "bottoms", "bottom", "legs",
			"cuise", "void knight robe" };
	public static String[] GLOVES = { "gloves", "gauntlets", "vambraces", "vamb" };
	public static String[] BOOTS = { "boots", "shoes" };
	public static String[] RINGS = { "ring" };
	public static String[] ARROWS = { "bolts", "arrow", "bolt rack", "handcannon shot" };

	@SuppressWarnings("unused")
	public boolean wearItem(int wearID, int slot) {
		synchronized (c) {
			if (wearID == 4155) {
				c.sendMessage(c.getSlayer().getTask() != null
						? "I currently have " + c.getSlayer().getTask().leftToKill() + " "
								+ c.getSlayer().getTask().toFormattedString() + "s left to kill."
						: "You currently have none.");
				return false;
			}
			int targetSlot = 0;
			boolean canWearItem = true;
			if (c.getVariables().playerItems[slot] == (wearID + 1)) {
				getRequirements(getItemName(wearID).toLowerCase(), wearID);
				targetSlot = targetSlot(getItemName(wearID).toLowerCase());

				String itemName = getItemName(wearID).toLowerCase();

				if (itemName.contains("cloak") || itemName.contains("attractor") || itemName.contains("accumulator")
						|| itemName.contains("cape") || itemName.contains("Cape") || itemName.contains("cloak")
						|| itemName.contains("Cloak")) {
					targetSlot = 1;
				} else if (itemName.contains("cap") && !itemName.contains("cape") || itemName.contains("coif")
						|| itemName.contains("tiara") || itemName.contains("halo") || itemName.contains("crown")
						|| itemName.contains("hexcrest") || itemName.contains("mitre") || itemName.contains("headdress")
						|| itemName.contains("cavalier") || itemName.contains("Cavalier") || itemName.contains("sallet")
						|| itemName.contains("Sallet") || itemName.contains("Hat") || itemName.contains("boater")
						|| itemName.contains("pagri") && !itemName.contains("Hatchet") || itemName.contains("mask")
						|| itemName.contains("Mask") || itemName.contains("hat") && !itemName.contains("hatchet")
						|| itemName.contains("helm") || itemName.contains("ringlet") || itemName.contains("Helm")
						|| itemName.contains("nemes") || itemName.contains("bandana") || itemName.contains("hood")
						|| itemName.contains("bun") || itemName.contains("Hood") || itemName.contains("berret")
						|| itemName.contains("afro")) {
					targetSlot = 0;
				} else if (itemName.contains("Amulet") || itemName.contains("amulet") || itemName.contains("scarf")
						|| itemName.contains("necklace") || itemName.contains("ammy") || itemName.contains("stole")
						|| itemName.contains("Necklace")) {
					targetSlot = 2;
				} else if (itemName.contains("shot") || itemName.contains("bolt") || itemName.contains("Bolt")
						|| itemName.contains("arrow") || itemName.contains("Arrow")) {
					targetSlot = 13;
				} else if (itemName.contains("platemail") || itemName.contains("jacket")
						|| itemName.contains("chest") && !itemName.contains("anchor") || itemName.contains("Torso")
						|| itemName.contains("platebody") || itemName.contains("Platebody")
						|| itemName.contains("chain") && !itemName.contains("skirt") || itemName.contains("Chain")
						|| itemName.contains("Top") || itemName.contains("Hauberk") || itemName.contains("hauberk")
						|| itemName.contains("top") || itemName.contains("blouse") || itemName.contains("torso")
						|| itemName.contains("shirt") || itemName.contains("Shirt") || itemName.contains("body")
						|| itemName.contains("Body") || itemName.contains("shirt") || itemName.contains("tunic")
						|| itemName.contains("ankh") || itemName.contains("shendyt")) {
					targetSlot = 4;
				} else if (itemName.contains("Book") || itemName.contains("defender") || itemName.contains("deflect")
						|| itemName.contains("toktz-xil-ul") || itemName.contains("book") || itemName.contains("Shield")
						|| itemName.contains("surgebox") || itemName.contains("deflect") || itemName.contains("shield")
						|| itemName.contains("defender") || itemName.contains("Defender")) {
					targetSlot = 5;
				} else if (itemName.contains("Cuisse") || itemName.contains("trousers") || itemName.contains("cuisse")
						|| itemName.contains("chaps") || itemName.contains("Chaps") || itemName.contains("legs")
						|| itemName.contains("Legs") || itemName.contains("Bottom") || itemName.contains("bottom")
						|| itemName.contains("skirt") || itemName.contains("Skirt") || itemName.contains("shorts")
						|| itemName.contains("Shorts") || itemName.contains("Blouse") || itemName.contains("tasset")
						|| itemName.contains("trouser") || itemName.contains("Tasset") || itemName.contains("leg")
						|| itemName.contains("Leg") || itemName.equalsIgnoreCase("Void_knight_robe")
						|| itemName.equalsIgnoreCase("Void knight robe")) {
					targetSlot = 7;
				} else if (itemName.contains("Gloves") || itemName.contains("gauntlet") || itemName.contains("gloves")
						|| itemName.contains("vamb") || itemName.contains("Vamb")) {
					targetSlot = 9;
				} else if (itemName.contains("Boots") || itemName.contains("boots") || itemName.contains("shoe")
						|| itemName.contains("flipper") || itemName.contains("sandle")) {
					targetSlot = 10;
				} else if (itemName.contains("ring") || itemName.contains("Ring") || itemName.contains("bracelet")) {
					targetSlot = 12;
				} else {

					targetSlot = ItemDefinition.forId(wearID).targetSlot;
					if (targetSlot <= 0) {
						targetSlot = 3;
					}
				}

				switch (wearID) {
				case 7918:
				case 19111: // Capes
					targetSlot = 1;
					break;
				case 11118: // Gloves
				case 11133:
				case 7462:
					targetSlot = 9;
					break;
				case 15018: // Rings
				case 15019:
				case 15020:
				case 15220:
					targetSlot = 12;
					break;
				case 922: // Hats/helmets
				case 4168:
				case 4166:
				case 6326:
				case 4502:
					targetSlot = 0;
					break;
				case 920: // Bodies
				case 14936:
				case 6129:
					targetSlot = 4;
					break;
				case 919: // Legs
				case 14938:
					targetSlot = 7;
					break;
				case 20171: // Weapons
				case 15259:
					targetSlot = 3;
					break;
				case 19335: // Amulets
					targetSlot = 2;
					break;
				case 9419: // Ammo
					targetSlot = 13;
					break;
				}
				if ((c.getVariables().duelRule[16] && is2handed(getItemName(wearID).toLowerCase(), wearID))) {
					c.sendMessage("Wearing 2h has been disabled in this duel!");
					return false;
				}
				if (c.getVariables().duelRule[11] && targetSlot == 0) {
					c.sendMessage("Wearing hats has been disabled in this duel!");
					return false;
				}
				if (c.getVariables().duelRule[12] && targetSlot == 1) {
					c.sendMessage("Wearing capes has been disabled in this duel!");
					return false;
				}
				if (c.getVariables().duelRule[13] && targetSlot == 2) {
					c.sendMessage("Wearing amulets has been disabled in this duel!");
					return false;
				}
				if (c.getVariables().duelRule[14] && targetSlot == 3) {
					c.sendMessage("Wielding weapons has been disabled in this duel!");
					return false;
				}
				if (c.getVariables().duelRule[15] && targetSlot == 4) {
					c.sendMessage("Wearing bodies has been disabled in this duel!");
					return false;
				}
				if ((c.getVariables().duelRule[16] && targetSlot == 5)) {
					c.sendMessage("Wearing shield has been disabled in this duel!");
					return false;
				}
				if (c.getVariables().duelRule[17] && targetSlot == 7) {
					c.sendMessage("Wearing legs has been disabled in this duel!");
					return false;
				}
				if (c.getVariables().duelRule[18] && targetSlot == 9) {
					c.sendMessage("Wearing gloves has been disabled in this duel!");
					return false;
				}
				if (c.getVariables().duelRule[19] && targetSlot == 10) {
					c.sendMessage("Wearing boots has been disabled in this duel!");
					return false;
				}
				if (c.getVariables().duelRule[20] && targetSlot == 12) {
					c.sendMessage("Wearing rings has been disabled in this duel!");
					return false;
				}
				if (c.getVariables().duelRule[21] && targetSlot == 13) {
					c.sendMessage("Wearing arrows has been disabled in this duel!");
					return false;
				}

				if (Constants.itemRequirements) {
					if (targetSlot == 10 || targetSlot == 7 || targetSlot == 5 || targetSlot == 4 || targetSlot == 0
							|| targetSlot == 9 || targetSlot == 10) {
						if (c.getVariables().defenceLevelReq > 0) {
							if (c.getPA()
									.getLevelForXP(c.getVariables().playerXP[1]) < c.getVariables().defenceLevelReq) {
								c.sendMessage("You need a defence level of " + c.getVariables().defenceLevelReq
										+ " to wear this item.");
								canWearItem = false;
							}
						}
						if (c.getVariables().rangeLevelReq > 0) {
							if (c.getPA()
									.getLevelForXP(c.getVariables().playerXP[4]) < c.getVariables().rangeLevelReq) {
								c.sendMessage("You need a range level of " + c.getVariables().rangeLevelReq
										+ " to wear this item.");
								canWearItem = false;
							}
						}
						if (c.getVariables().magicLevelReq > 0) {
							if (c.getPA()
									.getLevelForXP(c.getVariables().playerXP[6]) < c.getVariables().magicLevelReq) {
								c.sendMessage("You need a magic level of " + c.getVariables().magicLevelReq
										+ " to wear this item.");
								canWearItem = false;
							}
						}
					}
					if (targetSlot == 3) {
						if (c.getVariables().attackLevelReq > 0) {
							if (c.getPA()
									.getLevelForXP(c.getVariables().playerXP[0]) < c.getVariables().attackLevelReq) {
								c.sendMessage("You need an attack level of " + c.getVariables().attackLevelReq
										+ " to wield this weapon.");
								canWearItem = false;
							}
						}
						if (c.getVariables().strengthLevelReq > 0) {
							if (c.getPA()
									.getLevelForXP(c.getVariables().playerXP[0]) < c.getVariables().strengthLevelReq) {
								c.sendMessage("You need an strength level of " + c.getVariables().strengthLevelReq
										+ " to wield this weapon.");
								canWearItem = false;
							}
						}
						if (c.getVariables().rangeLevelReq > 0) {
							if (c.getPA()
									.getLevelForXP(c.getVariables().playerXP[4]) < c.getVariables().rangeLevelReq) {
								c.sendMessage("You need a range level of " + c.getVariables().rangeLevelReq
										+ " to wield this weapon.");
								canWearItem = false;
							}
						}
						if (c.getVariables().prayerLevelReq > 0) {
							if (c.getPA()
									.getLevelForXP(c.getVariables().playerXP[5]) < c.getVariables().prayerLevelReq) {
								c.sendMessage("You need a prayer level of " + c.getVariables().prayerLevelReq
										+ " to wield this weapon.");
								canWearItem = false;
							}
						}
						if (c.getVariables().magicLevelReq > 0) {
							if (c.getPA()
									.getLevelForXP(c.getVariables().playerXP[6]) < c.getVariables().magicLevelReq) {
								c.sendMessage("You need a magic level of " + c.getVariables().magicLevelReq
										+ " to wield this weapon.");
								canWearItem = false;
							}
						}
					}
				}

				if (!canWearItem) {
					return false;
				}
				if (targetSlot == c.getVariables().playerWeapon) {
					c.getPA().resetAutocast();
				}

				int wearAmount = c.getVariables().playerItemsN[slot];
				if (wearAmount < 1) {
					return false;
				}

				if (targetSlot == -1) {
					c.sendMessage("No possible slot found.");
					return false;
				}

				if (slot >= 0 && wearID >= 0) {
					int toEquip = c.getVariables().playerItems[slot];
					int toEquipN = c.getVariables().playerItemsN[slot];

					int toRemove = c.getVariables().playerEquipment[targetSlot];
					int toRemoveN = c.getVariables().playerEquipmentN[targetSlot];
					if (toEquip == toRemove + 1 && ItemLoader.isStackable(toRemove)) {
						deleteItem(toRemove, getItemSlot(toRemove), toEquipN);
						c.getVariables().playerEquipmentN[targetSlot] += toEquipN;
					} else if (targetSlot != 5 && targetSlot != 3) {
						c.getVariables().playerItems[slot] = toRemove + 1;
						c.getVariables().playerItemsN[slot] = toRemoveN;
						c.getVariables().playerEquipment[targetSlot] = toEquip - 1;
						c.getVariables().playerEquipmentN[targetSlot] = toEquipN;
					} else if (targetSlot == 5) {
						boolean wearing2h = is2handed(
								getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon])
										.toLowerCase(),
								c.getVariables().playerEquipment[c.getVariables().playerWeapon]);
						boolean wearingShield = c.getVariables().playerEquipment[c.getVariables().playerShield] > 0;
						if (wearing2h) {
							toRemove = c.getVariables().playerEquipment[c.getVariables().playerWeapon];
							toRemoveN = c.getVariables().playerEquipmentN[c.getVariables().playerWeapon];
							c.getVariables().playerEquipment[c.getVariables().playerWeapon] = -1;
							c.getVariables().playerEquipmentN[c.getVariables().playerWeapon] = 0;
							updateSlot(c.getVariables().playerWeapon);
						}
						c.getVariables().playerItems[slot] = toRemove + 1;
						c.getVariables().playerItemsN[slot] = toRemoveN;
						c.getVariables().playerEquipment[targetSlot] = toEquip - 1;
						c.getVariables().playerEquipmentN[targetSlot] = toEquipN;
					} else if (targetSlot == 3) {
						boolean is2h = is2handed(getItemName(wearID).toLowerCase(), wearID);
						boolean wearingShield = c.getVariables().playerEquipment[c.getVariables().playerShield] > 0;
						boolean wearingWeapon = c.getVariables().playerEquipment[c.getVariables().playerWeapon] > 0;
						if (is2h) {
							if (wearingShield && wearingWeapon) {
								if (freeSlots() > 0) {
									c.getVariables().playerItems[slot] = toRemove + 1;
									c.getVariables().playerItemsN[slot] = toRemoveN;
									c.getVariables().playerEquipment[targetSlot] = toEquip - 1;
									c.getVariables().playerEquipmentN[targetSlot] = toEquipN;
									removeItem(c.getVariables().playerEquipment[c.getVariables().playerShield],
											c.getVariables().playerShield);
								} else {
									c.sendMessage("You do not have enough inventory space to do this.");
									return false;
								}
							} else if (wearingShield && !wearingWeapon) {
								c.getVariables().playerItems[slot] = c.getVariables().playerEquipment[c
										.getVariables().playerShield] + 1;
								c.getVariables().playerItemsN[slot] = c.getVariables().playerEquipmentN[c
										.getVariables().playerShield];
								c.getVariables().playerEquipment[targetSlot] = toEquip - 1;
								c.getVariables().playerEquipmentN[targetSlot] = toEquipN;
								c.getVariables().playerEquipment[c.getVariables().playerShield] = -1;
								c.getVariables().playerEquipmentN[c.getVariables().playerShield] = 0;
								updateSlot(c.getVariables().playerShield);
							} else {
								c.getVariables().playerItems[slot] = toRemove + 1;
								c.getVariables().playerItemsN[slot] = toRemoveN;
								c.getVariables().playerEquipment[targetSlot] = toEquip - 1;
								c.getVariables().playerEquipmentN[targetSlot] = toEquipN;
							}
						} else {
							c.getVariables().playerItems[slot] = toRemove + 1;
							c.getVariables().playerItemsN[slot] = toRemoveN;
							c.getVariables().playerEquipment[targetSlot] = toEquip - 1;
							c.getVariables().playerEquipmentN[targetSlot] = toEquipN;
						}
					}
					c.setAwaitingUpdate(true);
				}
				if (targetSlot == 3) {
					c.getVariables().usingSpecial = false;
					addSpecialBar(wearID);
				}
				if (c.getOutStream() != null && c != null) {
					c.getOutStream().createFrameVarSizeWord(34);
					c.getOutStream().writeWord(1688);
					c.getOutStream().writeByte(targetSlot);
					c.getOutStream().writeWord(wearID + 1);

					if (c.getVariables().playerEquipmentN[targetSlot] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord(c.getVariables().playerEquipmentN[targetSlot]);
					} else {
						c.getOutStream().writeByte(c.getVariables().playerEquipmentN[targetSlot]);
					}

					c.getOutStream().endFrameVarSizeWord();
					c.flushOutStream();
				}
				sendWeapon(c.getVariables().playerEquipment[c.getVariables().playerWeapon],
						getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon]));
				resetBonus();
				getBonus();
				writeBonus();
				c.getCombat().getPlayerAnimIndex(c.getItems()
						.getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon]).toLowerCase());
				c.getPA().requestUpdates();
				Tiaras.handleTiara(c, wearID);
				return true;
			} else {
				return false;
			}
		}
	}

	public void wearItem(int wearID, int wearAmount, int targetSlot) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(targetSlot);
			c.getOutStream().writeWord(wearID + 1);
			if (wearAmount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(wearAmount);
			} else {
				c.getOutStream().writeByte(wearAmount);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.getVariables().playerEquipment[targetSlot] = wearID;
			c.getVariables().playerEquipmentN[targetSlot] = wearAmount;
			c.getItems();
			c.getItems().sendWeapon(c.getVariables().playerEquipment[c.getVariables().playerWeapon],
					c.getItems().getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon]));
			c.getItems().resetBonus();
			c.getItems().getBonus();
			c.getItems().writeBonus();
			c.getItems();
			c.getCombat().getPlayerAnimIndex(c.getItems()
					.getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon]).toLowerCase());
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
			c.getVariables().isFullHelm = ItemLoader
					.isFullHelm(c.getVariables().playerEquipment[c.getVariables().playerHat]);
			c.getVariables().isFullMask = ItemLoader
					.isFullMask(c.getVariables().playerEquipment[c.getVariables().playerHat]);
			c.getVariables().isFullBody = ItemLoader
					.isFullBody(c.getVariables().playerEquipment[c.getVariables().playerChest]);
		}
	}

	public void updateSlot(int slot) {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(slot);
				c.getOutStream().writeWord(c.getVariables().playerEquipment[slot] + 1);
				if (c.getVariables().playerEquipmentN[slot] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.getVariables().playerEquipmentN[slot]);
				} else {
					c.getOutStream().writeByte(c.getVariables().playerEquipmentN[slot]);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
		}

	}

	/**
	 * Remove Item
	 **/
	public void removeItem(int wearID, int slot) {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				if (c.getVariables().playerEquipment[slot] > -1) {
					if ((wearID == 4041 || wearID == 4042 || wearID == 4513
							|| wearID == 4515)/*
												 * && (c.inCwGame() ||
												 * c.inZammyWait() ||
												 * c.inSaraWait())
												 */) {
						c.sendMessage("Can't unequip this during Castle Wars.");
						return;
					}
					if (addItem(c.getVariables().playerEquipment[slot], c.getVariables().playerEquipmentN[slot])) {
						c.getVariables().constitution -= c.getVariables().calculateRemoved(slot, c);
						c.getPA().refreshSkill(3);
						c.getVariables().playerEquipment[slot] = -1;
						c.getVariables().playerEquipmentN[slot] = 0;
						sendWeapon(c.getVariables().playerEquipment[c.getVariables().playerWeapon],
								getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon]));
						resetBonus();
						getBonus();
						writeBonus();
						c.getItems();
						c.getCombat()
								.getPlayerAnimIndex(c.getItems()
										.getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon])
										.toLowerCase());
						c.getOutStream().createFrame(34);
						c.getOutStream().writeWord(6);
						c.getOutStream().writeWord(1688);
						c.getOutStream().writeByte(slot);
						c.getOutStream().writeWord(0);
						c.getOutStream().writeByte(0);
						c.flushOutStream();
						c.updateRequired = true;
						c.setAppearanceUpdateRequired(true);
					}
				}
			}
		}
	}

	/**
	 * BANK
	 */
	public void rearrangeBank() {
		int totalItems = 0;
		int highestSlot = 0;
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			if (c.getVariables().bankItems[i] != 0) {
				totalItems++;
				if (highestSlot <= i) {
					highestSlot = i;
				}
			}
		}
		for (int i = 0; i <= highestSlot; i++) {
			if (c.getVariables().bankItems[i] == 0) {
				boolean stop = false;

				for (int k = i; k <= highestSlot; k++) {
					if (c.getVariables().bankItems[k] != 0 && !stop) {
						int spots = k - i;
						for (int j = k; j <= highestSlot; j++) {
							c.getVariables().bankItems[j - spots] = c.getVariables().bankItems[j];
							c.getVariables().bankItemsN[j - spots] = c.getVariables().bankItemsN[j];
							stop = true;
							c.getVariables().bankItems[j] = 0;
							c.getVariables().bankItemsN[j] = 0;
						}
					}
				}
			}
		}

		int totalItemsAfter = 0;
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			if (c.getVariables().bankItems[i] != 0) {
				totalItemsAfter++;
			}
		}

		if (totalItems != totalItemsAfter) {
			c.disconnected = true;
		}
	}

	public void itemOnInterface(int id, int amount, int child) {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(child);
			c.getOutStream().writeWord(amount);
			if (amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(amount);
			} else {
				c.getOutStream().writeByte(amount);
			}
			c.getOutStream().writeWordBigEndianA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void sendFrame34(int item, int amount, int slot, int frame) {
		c.getOutStream().createFrameVarSizeWord(34);
		c.getOutStream().writeWord(frame);
		c.getOutStream().writeByte(slot);
		c.getOutStream().writeWord(item + 1);
		c.getOutStream().writeByte(255);
		c.getOutStream().writeDWord(amount);
		c.getOutStream().endFrameVarSizeWord();
	}

	public void resetBank() {

		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(5382); // bank
			c.getOutStream().writeWord(Constants.BANK_SIZE);
			for (int i = 0; i < Constants.BANK_SIZE; i++) {
				if (c.getVariables().bankItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.getVariables().bankItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.getVariables().bankItemsN[i]);
				}
				if (c.getVariables().bankItemsN[i] < 1) {
					c.getVariables().bankItems[i] = 0;
				}
				if (c.getVariables().bankItems[i] > Constants.ITEM_LIMIT || c.getVariables().bankItems[i] < 0) {
					c.getVariables().bankItems[i] = Constants.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(c.getVariables().bankItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
		if (c.getVariables().isSearching) {
			BankSearch.inputText(c, c.getVariables().bankText);
			return;
		}
	}

	public void resetTempItems() {
		synchronized (c) {
			int itemCount = 0;
			for (int i = 0; i < c.getVariables().playerItems.length; i++) {
				if (c.getVariables().playerItems[i] > -1) {
					itemCount = i;
				}
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(5064);
			c.getOutStream().writeWord(itemCount + 1);
			for (int i = 0; i < itemCount + 1; i++) {
				if (c.getVariables().playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.getVariables().playerItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.getVariables().playerItemsN[i]);
				}
				if (c.getVariables().playerItems[i] > Constants.ITEM_LIMIT || c.getVariables().playerItems[i] < 0) {
					c.getVariables().playerItems[i] = Constants.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(c.getVariables().playerItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}
	@SuppressWarnings("all")
	public boolean addToBank(int itemID, int amount) {
		if (!c.getVariables().isBanking) {
			c.sendMessage("You may not add items to a bank that isn't yours.");
			return false;
		}
		if (!ItemLoader.isStackable(itemID)) {
			if (itemID <= 0) {
				return false;
			}
			if (ItemLoader.isStackable(itemID) || amount > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (c.getVariables().bankItems[i] == itemID) {
						if (amount < amount) {
							amount = amount;
						}
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (c.getVariables().bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					c.getVariables().bankItems[toBankSlot] = itemID;
					if (amount < amount) {
						amount = amount;
					}
					if ((c.getVariables().bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (c.getVariables().bankItemsN[toBankSlot] + amount) > -1) {
						c.getVariables().bankItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((c.getVariables().bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (c.getVariables().bankItemsN[toBankSlot] + amount) > -1) {
						c.getVariables().bankItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}

					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (c.getVariables().bankItems[i] == itemID) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (c.getVariables().bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.getVariables().playerItems.length; i++) {
							if ((c.getVariables().playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.getVariables().bankItems[toBankSlot] = c
									.getVariables().playerItems[firstPossibleSlot];
							c.getVariables().bankItemsN[toBankSlot] += 1;
							deleteItem(
									(c.getVariables().playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.getVariables().playerItems.length; i++) {
							if ((c.getVariables().playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.getVariables().bankItemsN[toBankSlot] += 1;
							deleteItem(
									(c.getVariables().playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (ItemLoader.isNote(itemID) && !ItemLoader.isNote(itemID - 2)) {
			if (itemID <= 0) {
				return false;
			}
			if (ItemLoader.isStackable(itemID) || amount > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (c.getVariables().bankItems[i] == (itemID)) {
						if (amount < amount) {
							amount = amount;
						}
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (c.getVariables().bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					c.getVariables().bankItems[toBankSlot] = (itemID);
					if (amount < amount) {
						amount = amount;
					}
					if ((c.getVariables().bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (c.getVariables().bankItemsN[toBankSlot] + amount) > -1) {
						c.getVariables().bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}

					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((c.getVariables().bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (c.getVariables().bankItemsN[toBankSlot] + amount) > -1) {
						c.getVariables().bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}

					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (c.getVariables().bankItems[i] == (itemID)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (c.getVariables().bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.getVariables().playerItems.length; i++) {
							if ((c.getVariables().playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.getVariables().bankItems[toBankSlot] = (c
									.getVariables().playerItems[firstPossibleSlot] - 1);
							c.getVariables().bankItemsN[toBankSlot] += 1;
							deleteItem(
									(c.getVariables().playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.getVariables().playerItems.length; i++) {
							if ((c.getVariables().playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.getVariables().bankItemsN[toBankSlot] += 1;
							deleteItem(
									(c.getVariables().playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			c.sendMessage("Item not supported " + (itemID));
			return false;
		}
	}

	public boolean bankItem(int itemID, int fromSlot, int amount) {
		if (!c.getVariables().isBanking) {
			c.sendMessage("You may not add items to a bank that isn't yours.");
			return false;
		}
		if (c.getVariables().playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!ItemLoader.isNote(c.getVariables().playerItems[fromSlot] - 1)) {
			if (c.getVariables().playerItems[fromSlot] <= 0) {
				return false;
			}
			if (ItemLoader.isStackable(c.getVariables().playerItems[fromSlot] - 1)
					|| c.getVariables().playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (c.getVariables().bankItems[i] == c.getVariables().playerItems[fromSlot]) {
						if (c.getVariables().playerItemsN[fromSlot] < amount) {
							amount = c.getVariables().playerItemsN[fromSlot];
						}
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (c.getVariables().bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					c.getVariables().bankItems[toBankSlot] = c.getVariables().playerItems[fromSlot];
					if (c.getVariables().playerItemsN[fromSlot] < amount) {
						amount = c.getVariables().playerItemsN[fromSlot];
					}
					if ((c.getVariables().bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (c.getVariables().bankItemsN[toBankSlot] + amount) > -1) {
						c.getVariables().bankItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItem((c.getVariables().playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((c.getVariables().bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (c.getVariables().bankItemsN[toBankSlot] + amount) > -1) {
						c.getVariables().bankItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItem((c.getVariables().playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.getVariables().playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (c.getVariables().bankItems[i] == c.getVariables().playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (c.getVariables().bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.getVariables().playerItems.length; i++) {
							if ((c.getVariables().playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.getVariables().bankItems[toBankSlot] = c.getVariables().playerItems[firstPossibleSlot];
							c.getVariables().bankItemsN[toBankSlot] += 1;
							deleteItem((c.getVariables().playerItems[firstPossibleSlot] - 1), firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.getVariables().playerItems.length; i++) {
							if ((c.getVariables().playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.getVariables().bankItemsN[toBankSlot] += 1;
							deleteItem((c.getVariables().playerItems[firstPossibleSlot] - 1), firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (ItemLoader.isNote(c.getVariables().playerItems[fromSlot] - 1)
				&& !ItemLoader.isNote(c.getVariables().playerItems[fromSlot] - 2)) {
			if (c.getVariables().playerItems[fromSlot] <= 0) {
				return false;
			}
			if (ItemLoader.isStackable(c.getVariables().playerItems[fromSlot] - 1)
					|| c.getVariables().playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (c.getVariables().bankItems[i] == (c.getVariables().playerItems[fromSlot] - 1)) {
						if (c.getVariables().playerItemsN[fromSlot] < amount) {
							amount = c.getVariables().playerItemsN[fromSlot];
						}
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (c.getVariables().bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					c.getVariables().bankItems[toBankSlot] = (c.getVariables().playerItems[fromSlot] - 1);
					if (c.getVariables().playerItemsN[fromSlot] < amount) {
						amount = c.getVariables().playerItemsN[fromSlot];
					}
					if ((c.getVariables().bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (c.getVariables().bankItemsN[toBankSlot] + amount) > -1) {
						c.getVariables().bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.getVariables().playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((c.getVariables().bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (c.getVariables().bankItemsN[toBankSlot] + amount) > -1) {
						c.getVariables().bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.getVariables().playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.getVariables().playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (c.getVariables().bankItems[i] == (c.getVariables().playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (c.getVariables().bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.getVariables().playerItems.length; i++) {
							if ((c.getVariables().playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.getVariables().bankItems[toBankSlot] = (c.getVariables().playerItems[firstPossibleSlot]
									- 1);
							c.getVariables().bankItemsN[toBankSlot] += 1;
							deleteItem((c.getVariables().playerItems[firstPossibleSlot] - 1), firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.getVariables().playerItems.length; i++) {
							if ((c.getVariables().playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.getVariables().bankItemsN[toBankSlot] += 1;
							deleteItem((c.getVariables().playerItems[firstPossibleSlot] - 1), firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			c.sendMessage("Item not supported " + (c.getVariables().playerItems[fromSlot] - 1));
			return false;
		}
	}

	public int freeBankSlots() {
		int freeS = 0;
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			if (c.getVariables().bankItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public int getBankSlot(int itemId) {
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			if (c.getVariables().bankItems[i] - 1 == itemId) {
				return i;
			}
		}
		return -1;
	}

	public void fromBank(int fromSlot, int amount) {
		if (c.getVariables().bankItems[fromSlot] - 1 < 0)
			return;
		if (!c.getVariables().isBanking) {
			c.sendMessage("You may not withdraw items from a bank that isn't yours.");
			return;
		}
		if (c.getVariables().inTrade || DuelPlayer.contains(c) || DuelPlayer.isInFirstScreen(c)
				|| DuelPlayer.isInSecondScreen(c)) {
			c.getPA().closeAllWindows();
			return;
		}
		if (c.getVariables().takeAsNote && ItemLoader.isNote(c.getVariables().bankItems[fromSlot])) {
			if (ItemLoader.isStackable(c.getVariables().bankItems[fromSlot])) {
				long l = c.getItems().getItemAmount(c.getVariables().bankItems[fromSlot]);
				long k = (l + amount);
				if (k > 2147483647) {
					c.sendMessage("Inventory full!");
					return;
				}
			}
		}
		if (!c.getVariables().takeAsNote) {
			if (ItemLoader.isStackable(c.getVariables().bankItems[fromSlot] - 1)) {
				long l = c.getItems().getItemAmount(c.getVariables().bankItems[fromSlot] - 1);
				long k = (l + amount);
				if (k > 2147483647) {
					c.sendMessage("Inventory full!");
					return;
				}
			}
		}
		if (amount > 0) {
			if (c.getVariables().bankItems[fromSlot] > 0) {
				if (!c.getVariables().takeAsNote) {
					if (ItemLoader.isStackable(c.getVariables().bankItems[fromSlot] - 1)) {
						if (c.getVariables().bankItemsN[fromSlot] > amount) {
							if (addItem((c.getVariables().bankItems[fromSlot] - 1), amount)) {
								c.getVariables().bankItemsN[fromSlot] -= amount;
								resetBank();
								c.getItems().resetItems(5064);
							}
						} else {
							if (addItem((c.getVariables().bankItems[fromSlot] - 1),
									c.getVariables().bankItemsN[fromSlot])) {
								c.getVariables().bankItems[fromSlot] = 0;
								c.getVariables().bankItemsN[fromSlot] = 0;
								resetBank();
								c.getItems().resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (c.getVariables().bankItemsN[fromSlot] > 0) {
								if (addItem((c.getVariables().bankItems[fromSlot] - 1), 1)) {
									c.getVariables().bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						c.getItems().resetItems(5064);
					}
				} else if (c.getVariables().takeAsNote && ItemLoader.isNote(c.getVariables().bankItems[fromSlot])
						&& c.getVariables().bankItems[fromSlot] != 7407
						&& c.getVariables().bankItems[fromSlot] != 6723) {
					if (c.getVariables().bankItemsN[fromSlot] > amount) {
						if (addItem(c.getVariables().bankItems[fromSlot], amount)) {
							c.getVariables().bankItemsN[fromSlot] -= amount;
							resetBank();
							c.getItems().resetItems(5064);
						}
					} else {
						if (addItem(c.getVariables().bankItems[fromSlot], c.getVariables().bankItemsN[fromSlot])) {
							c.getVariables().bankItems[fromSlot] = 0;
							c.getVariables().bankItemsN[fromSlot] = 0;
							resetBank();
							c.getItems().resetItems(5064);
						}
					}
				} else {
					c.sendMessage("This item can't be withdrawn as a note.");
					if (ItemLoader.isStackable(c.getVariables().bankItems[fromSlot] - 1)) {
						if (c.getVariables().bankItemsN[fromSlot] > amount) {
							if (addItem((c.getVariables().bankItems[fromSlot] - 1), amount)) {
								c.getVariables().bankItemsN[fromSlot] -= amount;
								resetBank();
								c.getItems().resetItems(5064);
							}
						} else {
							if (addItem((c.getVariables().bankItems[fromSlot] - 1),
									c.getVariables().bankItemsN[fromSlot])) {
								c.getVariables().bankItems[fromSlot] = 0;
								c.getVariables().bankItemsN[fromSlot] = 0;
								resetBank();
								c.getItems().resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (c.getVariables().bankItemsN[fromSlot] > 0) {
								if (addItem((c.getVariables().bankItems[fromSlot] - 1), 1)) {
									c.getVariables().bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						c.getItems().resetItems(5064);
					}
				}
			}
		}
	}

	public int itemAmount(int itemID) {
		int tempAmount = 0;
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			if (c.getVariables().playerItems[i] == itemID) {
				tempAmount += c.getVariables().playerItemsN[i];
			}
		}
		return tempAmount;
	}

	public boolean isStackable(int itemID) {
		return ItemLoader.isStackable(itemID);
	}

	/**
	 * Update Equip tab
	 **/
	public void setEquipment(int wearID, int amount, int targetSlot) {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(targetSlot);
			c.getOutStream().writeWord(wearID + 1);
			if (amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(amount);
			} else {
				c.getOutStream().writeByte(amount);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.getVariables().playerEquipment[targetSlot] = wearID;
			c.getVariables().playerEquipmentN[targetSlot] = amount;
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}

	/**
	 * Move Items
	 **/
	public void moveItems(int from, int to, int moveWindow) {
		if (moveWindow == 3724) {
			int tempI;
			int tempN;
			tempI = c.getVariables().playerItems[from];
			tempN = c.getVariables().playerItemsN[from];

			c.getVariables().playerItems[from] = c.getVariables().playerItems[to];
			c.getVariables().playerItemsN[from] = c.getVariables().playerItemsN[to];
			c.getVariables().playerItems[to] = tempI;
			c.getVariables().playerItemsN[to] = tempN;
		}

		if (moveWindow == 34453 && from >= 0 && to >= 0 && from < Constants.BANK_SIZE && to < Constants.BANK_SIZE
				&& to < Constants.BANK_SIZE) {
			int tempI;
			int tempN;
			tempI = c.getVariables().bankItems[from];
			tempN = c.getVariables().bankItemsN[from];

			c.getVariables().bankItems[from] = c.getVariables().bankItems[to];
			c.getVariables().bankItemsN[from] = c.getVariables().bankItemsN[to];
			c.getVariables().bankItems[to] = tempI;
			c.getVariables().bankItemsN[to] = tempN;
		}

		if (moveWindow == 34453) {
			resetBank();
		}
		if (moveWindow == 18579) {
			int tempI;
			int tempN;
			tempI = c.getVariables().playerItems[from];
			tempN = c.getVariables().playerItemsN[from];

			c.getVariables().playerItems[from] = c.getVariables().playerItems[to];
			c.getVariables().playerItemsN[from] = c.getVariables().playerItemsN[to];
			c.getVariables().playerItems[to] = tempI;
			c.getVariables().playerItemsN[to] = tempN;
			resetItems(3214);
		}
		resetTempItems();
		if (moveWindow == 3724) {
			resetItems(3214);
		}

	}

	/**
	 * delete Item
	 **/
	public void deleteEquipment(int i, int j) {
		synchronized (c) {
			if (PlayerHandler.players[c.playerId] == null) {
				return;
			}
			if (i < 0) {
				return;
			}

			c.getVariables().playerEquipment[j] = -1;
			c.getVariables().playerEquipmentN[j] = c.getVariables().playerEquipmentN[j] - 1;
			c.getOutStream().createFrame(34);
			c.getOutStream().writeWord(6);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(j);
			c.getOutStream().writeWord(0);
			c.getOutStream().writeByte(0);
			getBonus();
			if (j == c.getVariables().playerWeapon) {
				sendWeapon(-1, "Unarmed");
			}
			resetBonus();
			getBonus();
			writeBonus();
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}

	public void deleteItem(int id, int amount) {
		deleteItem(id, getItemSlot(id), amount);
	}

	public void deleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.getVariables().playerItems[slot] == (id + 1)) {
			if (c.getVariables().playerItemsN[slot] > amount) {
				c.getVariables().playerItemsN[slot] -= amount;
			} else {
				c.getVariables().playerItemsN[slot] = 0;
				c.getVariables().playerItems[slot] = 0;
			}
			resetItems(3214);
		}
	}

	public void deleteItem2(int id, int amount) {
		int am = amount;
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			if (am == 0) {
				break;
			}
			if (c.getVariables().playerItems[i] == (id + 1)) {
				if (c.getVariables().playerItemsN[i] > amount) {
					c.getVariables().playerItemsN[i] -= amount;
					break;
				} else {
					c.getVariables().playerItems[i] = 0;
					c.getVariables().playerItemsN[i] = 0;
					am--;
				}
			}
		}
		resetItems(3214);
	}

	/**
	 * Delete Arrows
	 **/
	public void deleteArrow() {
		synchronized (c) {
			if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 10499 && Misc.random(5) != 1
					&& c.getVariables().playerEquipment[c.getVariables().playerArrows] != 4740) {
				return;
			}
			if (c.getVariables().playerEquipmentN[c.getVariables().playerArrows] == 1) {
				c.getItems().deleteEquipment(c.getVariables().playerEquipment[c.getVariables().playerArrows],
						c.getVariables().playerArrows);
			}
			if (c.getVariables().playerEquipmentN[c.getVariables().playerArrows] != 0) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(c.getVariables().playerArrows);
				c.getOutStream().writeWord(c.getVariables().playerEquipment[c.getVariables().playerArrows] + 1);
				if (c.getVariables().playerEquipmentN[c.getVariables().playerArrows] - 1 > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.getVariables().playerEquipmentN[c.getVariables().playerArrows] - 1);
				} else {
					c.getOutStream().writeByte(c.getVariables().playerEquipmentN[c.getVariables().playerArrows] - 1);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
				c.getVariables().playerEquipmentN[c.getVariables().playerArrows] -= 1;
			}
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}

	public void deleteEquipment() {
		synchronized (c) {
			if (c.getVariables().playerEquipmentN[c.getVariables().playerWeapon] == 1) {
				c.getItems().deleteEquipment(c.getVariables().playerEquipment[c.getVariables().playerWeapon],
						c.getVariables().playerWeapon);
			}
			if (c.getVariables().playerEquipmentN[c.getVariables().playerWeapon] != 0) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(c.getVariables().playerWeapon);
				c.getOutStream().writeWord(c.getVariables().playerEquipment[c.getVariables().playerWeapon] + 1);
				if (c.getVariables().playerEquipmentN[c.getVariables().playerWeapon] - 1 > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.getVariables().playerEquipmentN[c.getVariables().playerWeapon] - 1);
				} else {
					c.getOutStream().writeByte(c.getVariables().playerEquipmentN[c.getVariables().playerWeapon] - 1);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
				c.getVariables().playerEquipmentN[c.getVariables().playerWeapon] -= 1;
			}
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}

	/**
	 * Dropping Arrows
	 **/
	public void dropArrowNpc() {
		if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 10499) {
			return;
		}
		int enemyX = NPCHandler.npcs[c.getVariables().oldNpcIndex].getX();
		int enemyY = NPCHandler.npcs[c.getVariables().oldNpcIndex].getY();
		int enemyHeight = NPCHandler.npcs[c.getVariables().oldNpcIndex].heightLevel;
		if (Misc.random(10) >= 4) {
			if (ItemHandler.itemAmount(c.getVariables().rangeItemUsed, enemyX, enemyY) == 0) {
				ItemHandler.createGroundItem(c, c.getVariables().rangeItemUsed, enemyX, enemyY, enemyHeight, 1,
						c.getId());
			} else if (ItemHandler.itemAmount(c.getVariables().rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = ItemHandler.itemAmount(c.getVariables().rangeItemUsed, enemyX, enemyY);
				ItemHandler.removeGroundItem(c, c.getVariables().rangeItemUsed, enemyX, enemyY, false,
						Region.getRegion(enemyX, enemyY));
				ItemHandler.createGroundItem(c, c.getVariables().rangeItemUsed, enemyX, enemyY, enemyHeight, amount + 1,
						c.getId());
			}
		}
	}

	public void dropArrowPlayer() {
		int enemyX = PlayerHandler.players[c.getVariables().oldPlayerIndex].getX();
		int enemyY = PlayerHandler.players[c.getVariables().oldPlayerIndex].getY();
		int enemyHeight = PlayerHandler.players[c.getVariables().oldPlayerIndex].heightLevel;
		if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 10499) {
			return;
		}
		if (Misc.random(10) >= 4) {
			if (ItemHandler.itemAmount(c.getVariables().rangeItemUsed, enemyX, enemyY) == 0) {
				ItemHandler.createGroundItem(c, c.getVariables().rangeItemUsed, enemyX, enemyY, enemyHeight, 1,
						c.getId());
			} else if (ItemHandler.itemAmount(c.getVariables().rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = ItemHandler.itemAmount(c.getVariables().rangeItemUsed, enemyX, enemyY);
				ItemHandler.removeGroundItem(c, c.getVariables().rangeItemUsed, enemyX, enemyY, false,
						Region.getRegion(enemyX, enemyY));
				ItemHandler.createGroundItem(c, c.getVariables().rangeItemUsed, enemyX, enemyY, enemyHeight, amount + 1,
						c.getId());
			}
		}
	}

	public void removeAllItems() {
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			c.getVariables().playerItems[i] = 0;
		}
		for (int i = 0; i < c.getVariables().playerItemsN.length; i++) {
			c.getVariables().playerItemsN[i] = 0;
		}
		resetItems(3214);
	}

	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			if (c.getVariables().playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public int findItem(int id, int[] items, int[] amounts) {
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			if (((items[i] - 1) == id) && (amounts[i] > 0)) {
				return i;
			}
		}
		return -1;
	}

	public String getItemName(int i) {
		if (i == -1)
			return "Unarmed";
		if (ItemHandler.ItemList[i] != null) {
			if (ItemHandler.ItemList[i].itemId == i) {
				return ItemHandler.ItemList[i].itemName;
			}
		}
		return "Unarmed";
	}

	public int getItemId(String itemName) {
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (ItemHandler.ItemList[i] != null) {
				if (ItemHandler.ItemList[i].itemName.equalsIgnoreCase(itemName)) {
					return ItemHandler.ItemList[i].itemId;
				}
			}
		}
		return -1;
	}

	public int getItemSlot(int ItemID) {
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			if ((c.getVariables().playerItems[i] - 1) == ItemID) {
				return i;
			}
		}
		return -1;
	}

	public int getItemAmount(int ItemID) {
		int itemCount = 0;
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			if ((c.getVariables().playerItems[i] - 1) == ItemID) {
				itemCount += c.getVariables().playerItemsN[i];
			}
		}
		return itemCount;
	}

	public boolean playerHasItem(int itemID, int amt, int slot) {
		itemID++;
		int found = 0;
		if (c.getVariables().playerItems[slot] == (itemID)) {
			for (int i = 0; i < c.getVariables().playerItems.length; i++) {
				if (c.getVariables().playerItems[i] == itemID) {
					if (c.getVariables().playerItemsN[i] >= amt) {
						return true;
					} else {
						found++;
					}
				}
			}
			if (found >= amt) {
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean playerHasItem(int itemID) {
		itemID++;
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			if (c.getVariables().playerItems[i] == itemID) {
				return true;
			}
		}
		return false;
	}

	public boolean playerHasItem(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			if (c.getVariables().playerItems[i] == itemID) {
				if (c.getVariables().playerItemsN[i] >= amt) {
					return true;
				} else {
					found++;
				}
			}
		}
		if (found >= amt) {
			return true;
		}
		return false;
	}

	public int getUnnotedItem(int ItemID) {
		int NewID = ItemID - 1;
		String NotedName = "";
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (ItemHandler.ItemList[i] != null) {
				if (ItemHandler.ItemList[i].itemId == ItemID) {
					NotedName = ItemHandler.ItemList[i].itemName;
				}
			}
		}
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (ItemHandler.ItemList[i] != null) {
				if (ItemHandler.ItemList[i].itemName == NotedName) {
					if (ItemHandler.ItemList[i].itemDescription
							.startsWith("Swap this note at any bank for a") == false) {
						NewID = ItemHandler.ItemList[i].itemId;
						break;
					}
				}
			}
		}
		return NewID;
	}

	public boolean ownsCape() {
		if (c.getItems().playerHasItem(2412, 1) || c.getItems().playerHasItem(2413, 1)
				|| c.getItems().playerHasItem(2414, 1)) {
			return true;
		}
		for (int j = 0; j < Constants.BANK_SIZE; j++) {
			if (c.getVariables().bankItems[j] == 2412 || c.getVariables().bankItems[j] == 2413
					|| c.getVariables().bankItems[j] == 2414) {
				return true;
			}
		}
		if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 2413
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 2414
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 2415) {
			return true;
		}
		return false;
	}

	public boolean contains(int i) {
		return playerHasItem(i, 1);
	}

	public boolean contains(int i, int i2) {
		return playerHasItem(i, i2);
	}

	public boolean hasAllShards() {
		return playerHasItem(11712, 1) && playerHasItem(11712, 1) && playerHasItem(11714, 1);
	}

	public void makeBlade() {
		deleteItem(11710, 1);
		deleteItem(11712, 1);
		deleteItem(11714, 1);
		addItem(11690, 1);
		c.sendMessage("You combine the shards to make a blade.");
	}

	@SuppressWarnings("unused")
	public void makeGodsword(int i) {
		int godsword = i - 8;
		if (playerHasItem(11690) && playerHasItem(i)) {
			deleteItem(11690, 1);
			deleteItem(i, 1);
			addItem(i - 8, 1);
			c.sendMessage("You combine the hilt and the blade to make a godsword.");
		}
	}

	public boolean isHilt(int i) {
		return i >= 11702 && i <= 11708 && i % 2 == 0;
	}
}