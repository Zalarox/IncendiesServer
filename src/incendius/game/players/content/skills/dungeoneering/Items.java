package incendius.game.players.content.skills.dungeoneering;

import incendius.game.players.Player;
import incendius.game.players.content.TeleportHandler;

/**
 * 
 * @author Tringan/Rafael(Binding) & TheLife/Alex
 *
 *         Dungeoneering items related stuff
 */
public class Items {

	/**
	 * Getter for Bound items
	 * 
	 * @param p
	 * @return
	 */
	public static int[][] getBoundItems(Player p) {
		return p.boundItems;
	}

	/**
	 * Binding levels
	 */
	private final static int[] bindlevels = { 1, 20, 50, 90, 120 };

	/**
	 * Returns the number of items that the player can bind based on his
	 * Dungeoneering level
	 * 
	 * @param p
	 * @return
	 */
	public static int getBindsForLevel(Player p) {
		int items = 0;
		for (int i = 0; i < bindlevels.length; i++) {
			if (p.getSkillLevel(23) >= bindlevels[i])
				items++;
		}
		return items;

	}

	/**
	 * Return the quantity of items bound to the player
	 * 
	 * @param p
	 * @return
	 */
	public static int getBoundQuantity(Player p) {
		int boundItems = 0;
		for (int i = 0; i < 5; i++) {
			if (getBoundItems(p)[i][0] != 0)
				boundItems++;
		}
		return boundItems;
	}

	/**
	 * Checks if a player is able to bind
	 * 
	 * @param p
	 * @param itemId
	 * @return
	 */
	public static boolean ableToBind(Player p, int itemId) {
		if (!p.getItems().playerHasItem(itemId)) {
			return false;
		}
		for (int i = 0; i < Constants.BOUND_ITEMS.length; i++) {
			if (itemId == Constants.BOUND_ITEMS[i][0]) {
				if (p.party == null || p.party.floor == null) {
					p.sendMessage("You may not bind an item outside of Dungeoneering.");
					return false;
				}
				if (p.party.floor.level <= 5) {
					p.sendMessage("You cannot bind an item in the first 5 levels.");
					return false;
				}
				if (getBoundQuantity(p) > getBindsForLevel(p)) {
					p.sendMessage("You already have " + getBoundQuantity(p) + " bound items.");
					return false;
				} else
					return true;
			}
		}
		return false;
	}

	/**
	 * Binds an item to the player
	 * 
	 * @param p
	 * @param itemId
	 */
	public static void bind(Player p, int itemId) {
		if (!ableToBind(p, itemId))
			return;
		int boundItemId = 0;
		for (int bound = 0; bound < Constants.BOUND_ITEMS.length; bound++) {
			if (itemId == Constants.BOUND_ITEMS[bound][0]) {
				boundItemId = Constants.BOUND_ITEMS[bound][1];
			}
		}
		if (p.getItems().isStackable(itemId)) {
			p.bindX = true;
			p.boundItemId = boundItemId;
			p.outStream.createFrame(27);
			return;
		}
		p.getItems().deleteItem(itemId, 1);
		p.getItems().addItem(boundItemId, 1);
		for (int i = 0; i < 5; i++) {
			if (getBoundItems(p)[i][0] == 0) {
				p.boundItems[i][0] = boundItemId;
				p.boundItems[i][1] = 1;
				p.sendMessage("You have bound a " + p.getItems().getItemName(itemId) + ".");
				return;
			}
		}

	}

	/**
	 * Binds a stackable item
	 * 
	 * @param p
	 * @param quantity
	 */
	public static void bindStackable(Player p, int quantity) {
		p.getPA().removeAllWindows();
		if (getBoundItems(p)[5][0] == 0 && !(getBoundItems(p)[5][1] >= 125)) {
			p.boundItems[5][0] = p.boundItemId;
			p.boundItems[5][1] = quantity;
			p.sendMessage("You have bound x" + quantity + p.getItems().getItemName(p.boundItemId) + "'s.");
			return;
		}
		p.sendMessage("You cannont bind anymore runes or ammo.");

	}

	/**
	 * Unbinds an item from the player
	 * 
	 * @param p
	 * @param itemId
	 */
	public void unBind(Player p, int itemId) {
		for (int i = 0; i < 5; i++) {
			if (p.boundItems[i][0] > 0 && p.boundItems[i][0] == itemId) {
				p.boundItems[i][0] = 0;
				p.boundItems[i][1] = 0;
				for (int i2 = i; i2 < 5; i2++) {
					if (p.boundItems[i2][1] != 0) {
						p.boundItems[i2 - 1][0] = p.boundItems[i2][0];
						p.boundItems[i2][1] = p.boundItems[i2][1];
						p.boundItems[i2][0] = 0;
						p.boundItems[i2][1] = 0;
					}
				}
				break;
			}
		}
	}

	/**
	 * Adds the bound items when a new floor starts
	 * 
	 * @param p
	 */
	public void addBoundItems(Player p) {
		if (p.boundItems[0][0] == 0)
			return;
		for (int i = 0; i < 6; i++) {
			if (p.boundItems[i][0] != 0)
				p.getItems().addItem(p.boundItems[i][0], p.boundItems[i][1]);
		}
	}

	public static void teleport(Player p, boolean firstClick) {
		// TODO Check for wilderness & more + dungeoneering area
		if (p.getItems().playerHasItem(15707)) {
			if (firstClick) {
				if (Constants.inDungeoneeringLobby(p) || (p.party != null && p.party.floor != null))
					p.setSidebarInterface(14, p.party != null ? 26224 : 27224);
				else
					p.sendMessage("You must be at Dungeoneering to do this.");
			} else
				TeleportHandler.teleport(p, 3046, 4970, 1, "dungeon");
		}
	}

	public static void handleToolKit(Player p) {
		if (p.getItems().playerHasItem(19650)) {
			if (p.getItems().freeSlots() >= 8) {
				p.getItems().deleteItem(19650, 1);
				p.getItems().addItem(17794, 1);
				p.getItems().addItem(16295, 1);
				p.getItems().addItem(16361, 1);
				p.getItems().addItem(17490, 1);
				p.getItems().addItem(17754, 1);
				p.getItems().addItem(17883, 1);
				p.getItems().addItem(17796, 50);
				p.getItems().addItem(17678, 1);
			} else {
				p.sendMessage("You must have at least 8 free slots to open the toolkit.");
			}
		}
	}

	/**
	 * 
	 * @param p
	 * @param i
	 * @param i2
	 * @param amount
	 */
	public static void addItem(Player p, int i, int i2, int amount) {
		p.getItems().addItem(getItem(i, i2), amount);
	}

	public static int getItem(int armourType, int tier) {
		int items[][] = { { 16757, 8 }, { 17019, 2 }, { 16935, 2 }, { 16383, 2 }, { 15753, 2 }, { 17063, 8 },
				{ 16405, 2 }, { 16889, 2 }, { 16691, 2 }, { 17239, 2 }, { 16713, 2 }, { 16669, 2 }, { 16647, 2 },
				{ 16273, 2 }, { 17341, 2 }, { 16339, 2 }, { 16427, 5 }, { 17885, 5 }, { 16361, 2 }, { 16295, 2 },
				{ 17630, 2 }, { 17650, 2 } };
		return items[armourType][0] + (items[armourType][1] * tier);
	}
}
