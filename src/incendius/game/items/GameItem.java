package incendius.game.items;

public class GameItem {
	public int id, amount;
	public boolean stackable = false;

	public GameItem(int id, int amount) {
		if (ItemLoader.isStackable(id)) {
			stackable = true;
		}
		this.id = id;
		this.amount = amount;
	}
}