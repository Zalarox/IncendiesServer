package main.game.players;

import main.game.items.Item;
import main.game.items.ItemLoader;
import main.handlers.ItemHandler;

/**
 *
 * @author ArrowzFtw
 * @note itemId+1 is the playerItems
 * @note playerItems-1 = normalItemId
 */
public class Inventory {

	Player c;

	public Inventory(Player c) {
		this.c = c;
	}

	public void removeItem(Item i) {
		c.getItems().deleteItem(i.getId(), i.getCount());
	}

	public void addItemToSlot(Item i, int slot) {
		if (get(slot) != i.getId() + 1) {
			c.getVariables().playerItems[slot] = i.getId() + 1;
			c.getVariables().playerItemsN[slot] = i.getCount();
		} else {
			c.getVariables().playerItemsN[slot] += i.getCount();
		}
		update();
	}

	public int get(int slot) {
		return c.getVariables().playerItems[slot];
	}

	public void update() {
		c.getItems().resetItems(3214);
	}

	public boolean contains(int id) {
		return c.getItems().playerHasItem(id);
	}

	public boolean contains(Item item) {
		return c.getItems().playerHasItem(item.getId(), item.getCount());
	}

	public boolean contains(int id, int amount) {
		return c.getItems().playerHasItem(id, amount);
	}

	public Inventory getItemContainer() {
		return this;
	}

	public void addItem(Item item) {
		c.getItems().addItem(item.getId(), item.getCount());
	}

	public int getItemAmount(int id) {
		return c.getItems().getItemAmount(id);
	}

	public void replace(int item, int newItem) {
		c.getItems().deleteItem(item, 1);
		c.getItems().addItem(newItem, 1);
	}

	public int getCount(int i) {
		return c.getItems().getItemAmount(i);
	}

	public void set(int slot, Item item) {
		c.getVariables().playerItems[slot] = item.getId() + 1;
		c.getVariables().playerItemsN[slot] = item.getCount();
		update();
	}

	public int freeSlots() {
		return c.getItems().freeSlots();
	}

	public void add(int id) {
		c.getItems().addItem(id, 1);
	}

	public boolean add(int id, int amount) {
		if (ItemLoader.isStackable(id)) {
			int allAmount = getStackedGroundAmount(id, amount, getCount(id));
			if (allAmount > 0) {
				ItemHandler.createGroundItem(c, id, c.getX(), c.getY(), c.heightLevel, allAmount, c.getId());
				c.getItems().deleteItem(id, Integer.MAX_VALUE);
				c.getItems().addItem(id, Integer.MAX_VALUE);
				return true;
			}
		}
		return c.getItems().addItem(id, amount);
	}

	public int getStackedGroundAmount(int itemId, int addItemAmount, int itemAmount) {
		long x = 0;
		x += itemAmount;
		x += addItemAmount;
		if (x > 2147483647) {
			x -= 2147483647;
			return (int) x;
		}
		return -1;
	}

	public boolean canAddItem(Item item) {
		return item.getCount() <= freeSlots();
	}

	public void addItem(Item item, boolean drop) {
		/*
		 * for(int i = 0; i < item.getCount(); i++) {
		 * if(!ItemLoader.isStackable(item.getId())) {
		 * //getStackedGroundAmount(int itemId, int addItemAmount)
		 * if(freeSlots() >= 1) { add(item.getId()); } else {
		 * Server.itemHandler.createGroundItem(c, item.getId(), c.getX(),
		 * c.getY(), 1, c.getId()); } } else {
		 * if(getStackedGroundAmount(item.getId(), item.getCount()) > 0) { long
		 * l = item.getCount(); removeItem(item); add(item.getId(),
		 * Integer.MAX_VALUE); Server.itemHandler.createGroundItem(c,
		 * item.getId(), c.getX(), c.getY(),
		 * getStackedGroundAmount(item.getId(), item.getCount()), c.getId()); }
		 * } }
		 */
	}

	public boolean playerHasItem(int item) {
		return c.getItems().playerHasItem(item);
	}

	public void removeItemSlot(Item item, int slot) {
		c.getItems().deleteItem(item.getId(), slot, item.getCount());
	}

}
