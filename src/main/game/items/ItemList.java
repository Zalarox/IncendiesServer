package main.game.items;

public class ItemList {

	public int itemId;
	public String itemName;
	public String itemDescription;
	public double ShopValue;
	public double LowAlch;
	public double HighAlch;
	public int[] Bonuses = new int[15];
	public double[] soakingInfo = new double[3];
	public int targetSlot;
	public boolean itemIsNote;
	public boolean itemStackable;

	public ItemList(int _itemId) {
		itemId = _itemId;
	}

	public String getName() {
		return itemName;
	}

	public boolean isNoted() {
		return itemIsNote;
	}

	public boolean isStackable() {
		return itemStackable;
	}

	public String getDescription() {
		return itemDescription;
	}
}
