package main.game.players;

/**
 *
 * @author ArrowzFtw
 */
public class Equipment {

	Player c;

	public Equipment(Player c) {
		this.c = c;
	}

	public int getId(int slot) {
		return c.getVariables().playerEquipment[slot];
	}

	public void replaceEquipment(int id, int slot) {
		c.getItems().setEquipment(id, c.getVariables().playerEquipmentN[slot], slot);
	}
}
