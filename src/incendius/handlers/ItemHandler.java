package incendius.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import incendius.Constants;
import incendius.Data;
import incendius.game.items.GroundItem;
import incendius.game.items.ItemList;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.util.Misc;
import incendius.world.map.Region;

/**
 *
 * Handles ground items
 **/

public class ItemHandler {

	public static final int HIDE_TICKS = 120;
	public static ItemList ItemList[] = new ItemList[Constants.ITEM_LIMIT];

	public ItemHandler() {
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			ItemList[i] = null;
		}
		loadItemList(Data.ITEM_LIST);
		loadItemPrices(Data.ITEM_PRICES);
	}

	public void reloadAllItems() {
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			ItemList[i] = null;
		}
		loadItemList(Data.ITEM_LIST);
		loadItemPrices(Data.ITEM_PRICES);
	}

	/**
	 * Item amount
	 **/
	public static int itemAmount(int itemId, int itemX, int itemY) {
		for (GroundItem i : Region.getRegion(itemX, itemY).floorItems) {
			if (i.getItemId() == itemId && i.getItemX() == itemX && i.getItemY() == itemY) {
				return i.getItemAmount();
			}
		}
		return 0;
	}

	/**
	 * Item exists
	 **/
	public static boolean itemExists(int itemId, int itemX, int itemY) {
		for (GroundItem i : Region.getRegion(itemX, itemY).floorItems) {
			if (i.getItemId() == itemId && i.getItemX() == itemX && i.getItemY() == itemY) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Reloads any items if you enter a new region
	 **/
	public static void reloadItems(Player c) {
		Region r = Region.getRegion(c.absX, c.absY);
		if (r == null)
			return;
		LinkedList<GroundItem> itemsToRemove = new LinkedList<GroundItem>();
		LinkedList<GroundItem> itemsToCreate = new LinkedList<GroundItem>();
		for (GroundItem i : r.floorItems) {
			if (c.getItems().tradeable(i.getItemId()) || i.getName().equalsIgnoreCase(c.playerName)) {
				if (c.distanceToPoint(i.getItemX(), i.getItemY()) <= 60) {
					if (i.hideTicks > 0 && i.getName().equalsIgnoreCase(c.playerName)) {
						itemsToRemove.add(i);
						itemsToCreate.add(i);
					}
					if (i.hideTicks == 0) {
						itemsToRemove.add(i);
						itemsToCreate.add(i);
					}
				}
			}
		}
		for (GroundItem i : itemsToRemove) {
			removeGroundItem(c, i, r);
		}
		for (GroundItem i : itemsToCreate) {
			createGroundItem(c, i, r);
		}
	}

	/**
	 * Remove item for everyone within 60 squares
	 **/

	public static void removeGlobalItem(GroundItem i, int itemId, int itemX, int itemY, int itemHeight, int itemAmount,
			Region r) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (person != null) {
					if (person.distanceToPoint(itemX, itemY) <= 60 && itemHeight == person.heightLevel) {
						person.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount);
					}
				}
			}
		}
		removeItem(i, r);
	}

	/**
	 * Creates the ground item
	 **/
	public static int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 }, { 4712, 4872 }, { 4714, 4878 },
			{ 4716, 4884 }, { 4720, 4896 }, { 4718, 4890 }, { 4722, 4902 }, { 4732, 4932 }, { 4734, 4938 },
			{ 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 }, { 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 },
			{ 4745, 4956 }, { 4747, 4962 }, { 4749, 4968 }, { 4751, 4974 }, { 4753, 4980 }, { 4755, 4986 },
			{ 4757, 4992 }, { 4759, 4998 } };

	public static void createGroundItem(Player c, int itemId, int itemX, int itemY, int height, int itemAmount,
			int playerId) {
		if (c == null)
			return;
		if (PlayerHandler.players[playerId] == null)
			return;
		if (itemId > 0) {
			if (itemId >= 2412 && itemId <= 2414) {
				c.sendMessage("The item vanishes as it touches the ground.");
				return;
			}
			if (itemId == 10033 || itemId == 10034) {
				return;
			}
			if (itemId > 4705 && itemId < 4760) {
				for (int[] brokenBarrow : brokenBarrows) {
					if (brokenBarrow[0] == itemId) {
						itemId = brokenBarrow[1];
						break;
					}
				}
			}
			Region r = Region.getRegion(itemX, itemY);
			if (r == null)
				return;
			GroundItem item = new GroundItem(itemId, itemX, itemY, height, itemAmount, c.playerId,
					(c.party != null && c.party.floor != null) ? 0 : HIDE_TICKS,
					PlayerHandler.players[playerId].playerName,
					(c.party != null && c.party.floor != null) ? true : false);
			createGroundItem(c, item, r);
			r.startItemProcess();
		}
	}

	/**
	 * Shows items for everyone who is within 60 squares
	 **/
	public static void createGlobalItem(GroundItem i, Region r) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (person != null) {
					if (person.playerId != i.getItemController()) {
						if (!person.getItems().tradeable(i.getItemId()) && person.playerId != i.getItemController())
							continue;
						if (person.distanceToPoint(i.getItemX(),
								i.getItemY()) <= 60/*
													 * && i.getHeight() ==
													 * person.heightLevel
													 */) {
							createGroundItem(person, i, r);
						}
					}
				}
			}
		}
	}

	/**
	 * Removing the ground item
	 **/

	public static boolean removeGroundItem(Player c, int itemId, int itemX, int itemY, boolean add, Region r) {
		for (GroundItem i : r.floorItems) {
			if (i.getItemId() == itemId && i.getItemX() == itemX && i.getItemY() == itemY && !i.taken) {
				if ((i.removeTicks > 0 || i.hideTicks > 0) && i.getName().equalsIgnoreCase(c.playerName)) {
					if (add) {
						if (!c.getItems().specialCase(itemId)) {
							if (c.getInventory().add(i.getItemId(), i.getItemAmount())) {
								
								/**
								 * Log the pickup.
								 */
								c.getLogging().logDrop(itemId, i.getItemAmount(), itemX, itemY, false);
								
								i.taken = true;
								removeGroundItem(c, i, r);
								return true;
							}
						} else {
							i.taken = true;
							removeGroundItem(c, i, r);
							return true;
						}
					} else {
						if (i != null && i.removeTicks <= 0)
							i.taken = true;
						removeGroundItem(c, i, r);
						return true;
					}
				} else if (i.removeTicks <= 0 || i.hideTicks <= 0) {
					if (add) {
						if (c.getInventory().add(i.getItemId(), i.getItemAmount())) {
							i.taken = true;
							removeGlobalItem(i, r);
							return true;
						}
					} else {
						if (i.removeTicks <= 0)
							i.taken = true;
						removeGlobalItem(i, r);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Remove item for everyone within 60 squares
	 **/

	public static void removeGlobalItem(GroundItem i, Region r) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (person != null) {
					if (person.distanceToPoint(i.itemX, i.itemY) <= 60 && i.heightLevel == person.heightLevel) {
						removeGroundItem(person, i, r);
					}
				}
			}
		}
	}

	int slot;
	private static Scanner s;

	public static void newItemList(int ItemId, String ItemName, String ItemDescription, double ShopValue,
			double LowAlch, double HighAlch, int Bonuses[]) {
		if (ItemList[ItemId] != null) {
			System.out.println("Multiple item ID conflict for item " + ItemId);
		}
		ItemList newItemList = new ItemList(ItemId);
		newItemList.itemName = ItemName;
		newItemList.itemDescription = ItemDescription;
		newItemList.ShopValue = ShopValue;
		newItemList.LowAlch = LowAlch;
		newItemList.HighAlch = HighAlch;
		newItemList.Bonuses = Bonuses;
		ItemList[ItemId] = newItemList;
	}

	public static void loadItemPrices(String filename) {
		try {
			s = new Scanner(new File(filename));
			while (s.hasNextLine()) {
				String[] line = s.nextLine().split(" ");
				ItemList temp = getItemList(Integer.parseInt(line[0]));
				if (temp != null)
					temp.ShopValue = Integer.parseInt(line[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ItemList getItemList(int i) {
		for (ItemList aItemList : ItemList) {
			if (aItemList != null) {
				if (aItemList.itemId == i) {
					return aItemList;
				}
			}
		}
		return null;
	}

	/**
	 * Drop Item
	 *
	 * @param itemID
	 *            the item id
	 * @param itemX
	 *            x axis
	 * @param itemY
	 *            y axis
	 * @param itemAmount
	 *            amount
	 */

	public static void createGroundItem(Player c, GroundItem item, Region r) {
		// synchronized(c) {
		if (c != null && c.getOutStream() != null) {
			r.floorItems.add(item);
			/*
			 * if (Region.REGION_UPDATING_ENABLED) { if
			 * (Region.getRegion(item.itemX, item.itemY) != null)
			 * Region.getRegion(item.itemX, item.itemY).groundItems.add(item); }
			 */
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((item.itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((item.itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(44);
			c.getOutStream().writeWordBigEndianA(item.itemId);
			c.getOutStream().writeWord(item.itemAmount);
			c.getOutStream().writeByte(0);
			c.flushOutStream();
		}
		// }
	}

	/**
	 * Pickup Item
	 */

	public static void removeGroundItem(Player c, GroundItem item, Region r) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			r.floorItems.remove(item);
			/*
			 * if (Region.REGION_UPDATING_ENABLED) { if
			 * (Region.getRegion(item.itemX, item.itemY) != null)
			 * Region.getRegion(item.itemX,
			 * item.itemY).groundItems.remove(item); }
			 */
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((item.itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((item.itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(156);
			c.getOutStream().writeByteS(0);
			c.getOutStream().writeWord(item.itemId);
			c.flushOutStream();
		}
		// }
	}

	/**
	 * Removes item from list
	 * 
	 * @param r
	 **/
	public static void removeItem(GroundItem item, Region r) {
		r.floorItems.remove(item);
	}

	@SuppressWarnings("unused")
	public static boolean loadItemList(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		int numberOfItems = 0;
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader(FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			try {
				characterfile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		while (!EndOfFile && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("item")) {
					int[] Bonuses = new int[12];
					for (int i = 0; i < 12; i++) {
						if (token3[(6 + i)] != null) {
							Bonuses[i] = Integer.parseInt(token3[(6 + i)]);
						} else {
							break;
						}
					}
					try {
						double[] soaking = new double[3];
						for (int i = 18; i < 21; i++) {
							if (token3[i] != null) {
								soaking[i - 18] = Double.parseDouble(token3[i]);
							} else {
								break;
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {

					}
					newItemList(Integer.parseInt(token3[0]), token3[1].replaceAll("_", " "),
							token3[2].replaceAll("_", " "), Double.parseDouble(token3[4]),
							Double.parseDouble(token3[4]), Double.parseDouble(token3[6]), Bonuses);
					numberOfItems++;
				}
			} else {
				if (line.equals("[ENDOFITEMLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {

		}
		return false;
	}

}