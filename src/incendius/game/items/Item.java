package incendius.game.items;

/**
 * Represents a single item.
 * 
 * @author Graham Edgecombe
 * 
 */
public class Item {

	/**
	 * The id.
	 */
	private int id;

	/**
	 * The number of items.
	 */
	private int count;

	/* Assigning a timer to each items */

	private int timer;

	/**
	 * Creates a single item.
	 * 
	 * @param id
	 *            The id.
	 */
	public Item(int id) {
		this(id, 1);
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	/**
	 * Creates a stacked item.
	 * 
	 * @param id
	 *            The id.
	 * @param count
	 *            The number of items.
	 * @throws IllegalArgumentException
	 *             if count is negative.
	 */
	public Item(int id, int count) {
		if (count < 0) {
			throw new IllegalArgumentException("Count cannot be negative.");
		}
		this.id = id;
		this.count = count;
		this.timer = -1;

	}

	/**
	 * Creates a stacked item.
	 * 
	 * @param id
	 *            The id.
	 * @param count
	 *            The number of items.
	 * @param timer
	 *            The timer assigned.
	 * @throws IllegalArgumentException
	 *             if count is negative.
	 */
	public Item(int id, int count, int timer) {
		if (count < 0) {
			throw new IllegalArgumentException("Count cannot be negative.");
		}
		this.id = id;
		this.count = count;
		this.timer = timer;
	}

	/**
	 * Gets the item id.
	 * 
	 * @return The item id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the count.
	 * 
	 * @return The count.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Gets the timer.
	 * 
	 * @return The timer.
	 */
	public int getTimer() {
		return timer;
	}

	@Override
	public String toString() {
		return Item.class.getName() + " [id=" + id + ", count=" + count + "]";
	}

	public boolean equals(Item item) {
		return item.getId() == id && count == item.getCount();
	}

	public ItemList getDefinition() {
		return ItemDefinition.forId(id);
	}

}
