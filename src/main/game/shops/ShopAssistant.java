package main.game.shops;

import main.Constants;
import main.game.items.ItemLoader;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.handlers.ItemHandler;
import main.world.ShopHandler;

public class ShopAssistant {

	private Player c;

	public ShopAssistant(Player player) {
		this.c = player;
	}

	/**
	 * Shops
	 **/

	public void openShop(int ShopID) {
		c.setBusy(true);
		c.getItems().resetItems(3823);
		resetShop(ShopID);
		c.getInstance().isShopping = true;
		c.getInstance().myShopId = ShopID;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendString(ShopHandler.ShopName[ShopID], 3901);
	}

	public void updatePlayerShop() {
		for (int i = 1; i < Constants.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].getInstance().isShopping == true
						&& PlayerHandler.players[i].getInstance().myShopId == c.getInstance().myShopId
						&& i != c.playerId) {
					PlayerHandler.players[i].getInstance().updateShop = true;
				}
			}
		}
	}

	public boolean shopSellsItem(int itemID) {
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (itemID == (ShopHandler.ShopItems[c.getInstance().myShopId][i] - 1)) {
				return true;
			}
		}
		return false;
	}

	public void updateshop(int i) {
		resetShop(i);
	}

	public void resetShop(int ShopID) {
		// synchronized (c) {
		int TotalItems = 0;
		for (int i = 0; i < ShopHandler.MaxShopItems; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0) {
				TotalItems++;
			}
		}
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(3900);
		c.getOutStream().writeWord(TotalItems);
		int TotalCount = 0;
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0 || i <= ShopHandler.ShopItemsStandard[ShopID]) {
				if (ShopHandler.ShopItemsN[ShopID][i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(ShopHandler.ShopItemsN[ShopID][i]);
				} else {
					c.getOutStream().writeByte(ShopHandler.ShopItemsN[ShopID][i]);
				}
				if (ShopHandler.ShopItems[ShopID][i] > Constants.ITEM_LIMIT || ShopHandler.ShopItems[ShopID][i] < 0) {
					ShopHandler.ShopItems[ShopID][i] = Constants.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(ShopHandler.ShopItems[ShopID][i]);
				TotalCount++;
			}
			if (TotalCount > TotalItems) {
				break;
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	@SuppressWarnings("unused")
	public double getItemShopValue(int ItemID, int Type, int fromSlot) {
		double ShopValue = 1;
		double Overstock = 0;
		double TotPrice = 0;
		int i = ItemID;
		if (ItemHandler.ItemList[i] != null) {
			if (ItemHandler.ItemList[i].itemId == ItemID) {
				ShopValue = ItemHandler.ItemList[i].ShopValue;
			}
		}

		TotPrice = ShopValue;

		if (ShopHandler.ShopBModifier[c.getInstance().myShopId] == 1) {
			TotPrice *= 1;
			TotPrice *= 1;
			if (Type == 1) {
				TotPrice *= 1;
			}
		} else if (Type == 1) {
			TotPrice *= 1;
		}
		return TotPrice;
	}

	public int getItemShopValue(int itemId) {
		int i = itemId;
		if (i > 0) {
			if (ItemHandler.ItemList[i] != null) {
				if (ItemHandler.ItemList[i].itemId == itemId) {
					return (int) ItemHandler.ItemList[i].ShopValue;
				}
			}
		}
		return 0;
	}

	/**
	 * buy item from shop (Shop Price)
	 **/

	public void buyFromShopPrice(int removeId, int removeSlot) {
		int ShopValue = (int) Math.floor(getItemShopValue(removeId, 0, removeSlot));
		ShopValue *= 1.15;
		String ShopAdd = "";
		if (c.getInstance().myShopId == 3) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " points.");
			return;
		}
		if (c.getInstance().myShopId == 13) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " points.");
			return;
		}
		if (c.getInstance().myShopId == 69) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " points.");
			return;
		}
		if (c.getInstance().myShopId == 70) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " points.");
			return;
		}
		if (c.getInstance().myShopId == 71) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " points.");
			return;
		}
		if (c.getInstance().myShopId == 72) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " points.");
			return;
		}
		if (ShopValue >= 1000 && ShopValue < 1000000) {
			ShopAdd = " (" + (ShopValue / 1000) + "K)";
		} else if (ShopValue >= 1000000) {
			ShopAdd = " (" + (ShopValue / 1000000) + " million)";
		}
		c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + String.format("%, d", ShopValue)
				+ " coins" + ShopAdd);
	}

	public int getSpecialItemValue(int id) {
		switch (id) {
		case 6914: // masters wand
			return 20;
		case 6889: // mages book
			return 15;
		case 6570: // fire cape
			return 10;
		case 10551: // torso
			return 25;
		case 10548: // fighter hat
			return 15;
		case 15349:
			return 20; // ardy cloak 3
		case 15013:
			return 15;
		case 15014:
			return 25;
		case 15017:
		case 15018:
		case 15220:
		case 15020:
		case 15019:
			return 40; // imbued rings
		case 2581:
		case 2577:
			return 400; // Robin hood & Ranger boots
		case 13263:
			return 250; // Slayer helmet
		case 15492:
			return 325; // Full slayer helmet
		case 14642:
		case 14641:
			return 100; // Soul war capes
		case 1038:
		case 1040:
		case 1042:
		case 1044:
		case 1046:
		case 1048:
			return 50;// Party hats
		case 1053:
		case 1055:
		case 1057:
			return 30;// H'ween masks
		case 1050:
			return 40;
		case 15098:
			return 20;// Dice bag
		case 20135:
		case 20139:
		case 20143:
			return 20;// Torva
		case 20159:
		case 20163:
		case 20167:
			return 15;// Virtus
		case 20147:
		case 20151:
		case 20155:
			return 15;// Pernix
		case 20000:
		case 20001:
		case 20002:
			return 12;// Glacor boots
		case 19111:
			return 15;// TokHar-Kal
		case 11694:
			return 20;// Ags
		case 11696:
		case 11698:
		case 11700:
			return 10;// Zgs
		case 14484:
			return 20;// D claws
		case 19784:
			return 30;// Korasi
		case 15486:
			return 30;// Sol
		case 13744:
			return 10;// Spectral
		case 13738:
			return 18;// Arcane
		case 13742:
			return 25;// Elysian
		case 13740:
			return 30;// Divine
		case 13896:
			return 11;// Stat helm
		case 13884:
			return 12;// Stat body
		case 13890:
			return 12;// Stat legs
		case 13902:
			return 15;// Stat hammer
		case 13887:
			return 18;// Vesta body
		case 13893:
			return 17;// Vesta legs
		case 13905:
			return 10;// Vesta spear
		case 13899:
			return 25;// Vesta longsword
		case 13876:
			return 5;// Morrigans coif
		case 13870:
			return 5;// Morrigans body
		case 13873:
			return 5;// Morrigans chaps
		case 13864:
			return 5;// Zuriels hood
		case 13858:
			return 5;// Zuriels robe top
		case 13861:
			return 5;// Zuriels robe bottom
		case 13867:
			return 5;// Zuriels staff
		case 11283:// Dragonfire Shield
			return 8;
		case 11718:// Arma helm
			return 6;
		case 11720:// Arma chest
			return 7;
		case 11722:// Arma legs
			return 7;// Armadyl
		case 11724:
		case 11726:
			return 12;// Bandos tassets and chestplate
		case 11728:
			return 5;// Bandos boots

		case 8844:
			return 2;
		case 8845:
			return 5;
		case 8846:
			return 7;
		case 8847:
			return 9;
		case 8848:
			return 10;
		case 8849:
			return 15;
		case 8850:
			return 20;
		case 20072:
			return 30;
		case 18335:// Arcane stream neckalace
			return 15;
		case 19669:// Ring of Vigour
			return 50;
		case 18349:// Chaotic Rapier
		case 18351:// Chaotic Longsword
		case 18353:// Chaotic Maul
		case 18355:// Chaotic Staff
		case 18357:// Chaotic crossbow
		case 18359:// Chaotic KiteShield
		case 18361:// Eagle-eye shield
		case 18363:// Farseer kiteshield
			return 200;
		case 4151:
			return 7; // Whip
		case 15441:
		case 15442:
		case 15443:
		case 15444:
			return 20; // Coloured whips
		case 14936:
		case 14939:
			return 4; // Agile armour
		case 13661:
			return 8; // Inferno adze
		case 6665:
		case 6666:
			return 10; // Flippers and mudskipper hat
		case 11926:
		case 11928:
		case 11930:
			return 8; // God armours box sets
		case 11938:
			return 12; // Gilded box set
		case 8839:
		case 8840:
			return 15; // Void robes top and bottom
		case 8841:
			return 5; // Void mace
		case 8842:
			return 8; // void gloves
		case 11663:
		case 11664:
		case 11665:
			return 6; // Void helms

		}
		return 100;
	}

	/**
	 * Sell item to shop (Shop Price)
	 **/
	public void sellToShopPrice(int removeId, int removeSlot) {
		for (int i : Constants.ITEM_SELLABLE) {
			if (i == removeId) {
				c.sendMessage("You can't sell " + c.getItems().getItemName(removeId).toLowerCase() + ".");
				return;
			}
		}
		boolean IsIn = false;
		if (ShopHandler.ShopSModifier[c.getInstance().myShopId] > 1) {
			for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.getInstance().myShopId]; j++) {
				if (removeId == (ShopHandler.ShopItems[c.getInstance().myShopId][j] - 1)) {
					IsIn = true;
					break;
				}
			}
		} else {
			IsIn = true;
		}
		if (IsIn == false) {
			c.sendMessage("You can't sell " + c.getItems().getItemName(removeId).toLowerCase() + " to this store.");
		} else {
			int ShopValue = (int) Math.floor(getItemShopValue(removeId, 1, removeSlot));
			String ShopAdd = "";
			int coinsShop = c.getInstance().myShopId != 1 ? ShopValue : (ShopValue - (ShopValue / 3));
			if (coinsShop >= 1000 && coinsShop < 1000000) {
				ShopAdd = " (" + ((coinsShop) / 1000) + "K)";
			} else if (coinsShop >= 1000000) {
				ShopAdd = " (" + ((coinsShop) / 1000000) + " million)";
			}
			c.sendMessage(c.getItems().getItemName(removeId) + ": shop will buy for " + coinsShop + " coins" + ShopAdd);
		}
	}

	public boolean sellItem(int itemID, int fromSlot, int amount) {
		if (c.getInstance().inTrade)
			return false;
		if (!(c.getInstance().myShopId == 1 || (c.getInstance().myShopId == 73
				&& (itemID == 1897 || itemID == 6814 || itemID == 1796 || itemID == 1613))))
			return false;
		for (int i : Constants.ITEM_SELLABLE) {
			if (i == itemID) {
				c.sendMessage("You can't sell " + c.getItems().getItemName(itemID).toLowerCase() + ".");
				return false;
			}
		}

		if (amount > 0 && itemID == (c.getInstance().playerItems[fromSlot] - 1)) {
			if (ShopHandler.ShopSModifier[c.getInstance().myShopId] > 1) {
				boolean IsIn = false;
				for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.getInstance().myShopId]; i++) {
					if (itemID == (ShopHandler.ShopItems[c.getInstance().myShopId][i] - 1)) {
						IsIn = true;
						break;
					}
				}
				if (IsIn == false) {
					c.sendMessage(
							"You can't sell " + c.getItems().getItemName(itemID).toLowerCase() + " to this store.");
					return false;
				}
			}

			if (amount > c.getInstance().playerItemsN[fromSlot]
					&& (ItemLoader.isNote((c.getInstance().playerItems[fromSlot] - 1)) == true
							|| ItemLoader.isNote((c.getInstance().playerItems[fromSlot] - 1)) == true)) {
				amount = c.getInstance().playerItemsN[fromSlot];
			} else if (amount > c.getItems().getItemAmount(itemID)
					&& ItemLoader.isNote((c.getInstance().playerItems[fromSlot] - 1)) == false
					&& ItemLoader.isStackable((c.getInstance().playerItems[fromSlot] - 1)) == false) {
				amount = c.getItems().getItemAmount(itemID);
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 1, fromSlot));
				int coinsShop = c.getInstance().myShopId != 1 ? TotPrice2 : (TotPrice2 - (TotPrice2 / 3));
				if (c.getItems().freeSlots() > 0 || c.getItems().playerHasItem(995)) {
					if (ItemLoader.isNote(itemID) == false) {
						c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
					} else {
						c.getItems().deleteItem(itemID, fromSlot, 1);
					}
					c.getItems().addItem(995, coinsShop);
					addShopItem(itemID, 1);
				} else {
					c.sendMessage("You don't have enough space in your inventory.");
					break;
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.getInstance().myShopId);
			updatePlayerShop();
			return true;
		}
		return true;
	}

	public boolean addShopItem(int itemID, int amount) {
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}
		if (ItemLoader.isNote(itemID) == true) {
			itemID = c.getItems().getUnnotedItem(itemID);
		}
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if ((ShopHandler.ShopItems[c.getInstance().myShopId][i] - 1) == itemID) {
				ShopHandler.ShopItemsN[c.getInstance().myShopId][i] += amount;
				Added = true;
			}
		}
		if (Added == false) {
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[c.getInstance().myShopId][i] == 0) {
					ShopHandler.ShopItems[c.getInstance().myShopId][i] = (itemID + 1);
					ShopHandler.ShopItemsN[c.getInstance().myShopId][i] = amount;
					ShopHandler.ShopItemsDelay[c.getInstance().myShopId][i] = 0;
					break;
				}
			}
		}
		return true;
	}

	@SuppressWarnings("unused")
	public boolean buyItem(int itemID, int fromSlot, int amount) {

		if (ShopHandler.ShopItems[c.getInstance().myShopId][fromSlot] - 1 != itemID
				&& c.getInstance().myShopId != 14) {
			return false;
		}
		if (c.getInstance().myShopId == 14) {
			skillBuy(itemID);
			return false;
		} else if (c.getInstance().myShopId == 3) {
			handleOtherShop(itemID);
			return false;
		} else if (c.getInstance().myShopId == 13) {
			handleOtherShop(itemID);
			return false;
		} else if (c.getInstance().myShopId == 72) {
			handleOtherShop(itemID);
			return false;
		} else if (c.getInstance().myShopId == 71) {
			handleOtherShop(itemID);
			return false;
		} else if (c.getInstance().myShopId == 69) {
			handleOtherShop(itemID);
			return false;
		} else if (c.getInstance().myShopId == 70) {
			handleOtherShop(itemID);
			return false;
		}
		if (amount > 0) {
			int currency = (c.myShopId >= 77 && c.myShopId <= 79)
					? main.game.players.content.skills.dungeoneering.Constants.CURRENCY : 995;
			if (!shopSellsItem(itemID))
				return false;
			if (amount > ShopHandler.ShopItemsN[c.getInstance().myShopId][fromSlot]) {
				amount = ShopHandler.ShopItemsN[c.getInstance().myShopId][fromSlot];
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			int Slot = 0;
			int Slot1 = 0;// Tokkul
			int Slot2 = 0;// Pking Points
			if (c.getInstance().myShopId == 17 || c.getInstance().myShopId == 2 && c.getInstance().myShopId == 4) {
				handleOtherShop(itemID);
				return false;
			}
			if (ItemLoader.isStackable(itemID) && c.getItems().playerHasItem(currency, TotPrice2 * amount)) {
				c.getItems().deleteItem2(currency, TotPrice2 * amount);
				c.getItems().addItem(itemID, amount);
				ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= amount;
				ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
				c.getItems().resetItems(3823);
				resetShop(c.myShopId);
				updatePlayerShop();
				return false;
			}
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0, fromSlot));
				Slot = c.getItems().getItemSlot(currency);
				Slot1 = c.getItems().getItemSlot(6529);
				if (Slot == -1 && c.getInstance().myShopId != 29 && c.getInstance().myShopId != 30
						&& c.getInstance().myShopId != 31 && c.getInstance().myShopId != 47) {
					c.sendMessage("You don't have enough coins.");
					break;
				}
				if (Slot1 == -1 && c.getInstance().myShopId == 29 || c.getInstance().myShopId == 30
						|| c.getInstance().myShopId == 31) {
					c.sendMessage("You don't have enough tokkul.");
					break;
				}
				if (TotPrice2 <= 1) {
					TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0, fromSlot));
					TotPrice2 *= 1.66;
				}
				if (c.getInstance().myShopId != 29 || c.getInstance().myShopId != 30
						|| c.getInstance().myShopId != 31 || c.getInstance().myShopId != 47) {
					if (c.getInstance().playerItemsN[Slot] >= TotPrice2) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(currency, c.getItems().getItemSlot(currency), TotPrice2);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.getInstance().myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.getInstance().myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.getInstance().myShopId]) {
								ShopHandler.ShopItems[c.getInstance().myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough coins.");
						break;
					}
				}
				if (c.getInstance().myShopId == 29 || c.getInstance().myShopId == 30
						|| c.getInstance().myShopId == 31) {
					if (c.getInstance().playerItemsN[Slot1] >= TotPrice2) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(6529, c.getItems().getItemSlot(6529), TotPrice2);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.getInstance().myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.getInstance().myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.getInstance().myShopId]) {
								ShopHandler.ShopItems[c.getInstance().myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough tokkul.");
						break;
					}
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.getInstance().myShopId);
			updatePlayerShop();
			return true;
		}
		return false;
	}

	public void handleOtherShop(int itemID) {
		if (c.getInstance().myShopId == 17) {
			if (c.getInstance().magePoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getInstance().magePoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough points to buy this item.2");
			}
		} else if (c.getInstance().myShopId == 18) {
			if (c.getInstance().pcPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getInstance().pcPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough points to buy this item.");
			}
		} else if (c.getInstance().myShopId == 69) { // Donator Points shop 1
			if (c.getInstance().DonatorPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getInstance().DonatorPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough Donator points.");
			}
		} else if (c.getInstance().myShopId == 70) { // Donator Points shop 2
			if (c.getInstance().DonatorPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getInstance().DonatorPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough Donator points.");
			}
		} else if (c.getInstance().myShopId == 71) { // Slayer points shop
			if (c.getInstance().SlayerPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getInstance().SlayerPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough Slayer points.");
			}
		} else if (c.getInstance().myShopId == 72) { // Pest control shop
			if (c.getInstance().pcPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getInstance().pcPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough Pest Control points.");
			}
		} else if (c.getInstance().myShopId == 13) { // Voting point shop
			if (c.getInstance().votingPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getInstance().votingPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough Voting points.");
			}
		} else if (c.getInstance().myShopId == 3) { // Pk points shop
			if (c.getInstance().pkp >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getInstance().pkp -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough PK points.");
			}
		}
	}

	public void openSkillCape() {
		int capes = get99Count();
		if (capes > 1)
			capes = 1;
		else
			capes = 0;
		c.getInstance().myShopId = 14;
		setupSkillCapes(capes, get99Count());
	}

	public int[] skillCapes = { 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795,
			9792, 9774, 9771, 9777, 9786, 9810, 9765, 9948, 12169, 18508, 0 };
	// TODO ADD THE REAL SKILLCAPE ABOVE WHERE THE 0 IS!

	public int get99Count() {
		int count = 0;
		for (int j = 0; j < c.getInstance().playerLevel.length; j++) {
			if (c.getLevelForXP(c.getInstance().playerXP[j]) >= 99) {
				count++;
			}
		}
		return count;
	}

	public void setupSkillCapes(int capes, int capes2) {
		// synchronized (c) {
		c.getItems().resetItems(3823);
		c.getInstance().isShopping = true;
		c.getInstance().myShopId = 14;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendString("Skillcape Shop", 3901);

		int TotalItems = 0;
		TotalItems = capes2;
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(3900);
		c.getOutStream().writeWord(TotalItems);
		@SuppressWarnings("unused")
		int TotalCount = 0;
		for (int i = 0; i < 25; i++) {
			if (c.getLevelForXP(c.getInstance().playerXP[i]) < 99)
				continue;
			c.getOutStream().writeByte(1);
			c.getOutStream().writeWordBigEndianA(skillCapes[i] + 2);
			TotalCount++;
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	public void skillBuy(int item) {
		int nn = get99Count();
		if (nn > 1)
			nn = 1;
		else
			nn = 0;
		for (int j = 0; j < skillCapes.length; j++) {
			if (skillCapes[j] == item || skillCapes[j] + 1 == item) {
				if (c.getItems().freeSlots() > 1) {
					if (c.getItems().playerHasItem(995, 99000)) {
						if (c.getLevelForXP(c.getInstance().playerXP[j]) >= 99) {
							c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 99000);
							c.getItems().addItem(skillCapes[j] + nn, 1);
							c.getItems().addItem(skillCapes[j] + 2, 1);
						} else {
							c.sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
						}
					} else {
						c.sendMessage("You need 99k to buy this item.");
					}
				} else {
					c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
				}
			}
		}
	}

}
