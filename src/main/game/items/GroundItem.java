package main.game.items;

/**
 * Handles Globally Spawned Items
 * 
 * @author Sanity Revised by Shawn Notes by Shawn
 */
public class GroundItem {

	public int itemId, itemX, itemY, heightLevel, itemAmount, itemController, hideTicks, removeTicks;
	public String ownerName;
	public boolean taken, stopRemoval, remove;

	public GroundItem(int id, int x, int y, int heightLevel, int amount, int controller, int hideTicks, String name,
			boolean stopRemoval) {
		this.itemId = id;
		this.itemX = x;
		this.itemY = y;
		this.heightLevel = heightLevel;
		this.itemAmount = amount;
		this.itemController = controller;
		this.hideTicks = hideTicks;
		this.ownerName = name;
		this.taken = false;
		this.stopRemoval = stopRemoval;
	}

	public int getHeight() {
		return heightLevel;
	}

	public int getItemId() {
		return this.itemId;
	}

	public int getItemX() {
		return this.itemX;
	}

	public int getItemY() {
		return this.itemY;
	}

	public int getItemAmount() {
		return this.itemAmount;
	}

	public int getItemController() {
		return this.itemController;
	}

	public String getName() {
		return this.ownerName;
	}

}