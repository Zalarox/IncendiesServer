package incendius.game.players.events;

import incendius.event.Task;
import incendius.game.players.Player;

public class SpecialRestoreTask extends Task {

	private Player player;

	public SpecialRestoreTask(Player player) {
		super(35);
		this.player = player;
	}

	@Override
	protected void execute() {
		if (player.getInstance().specAmount < 10) {
			player.getInstance().specAmount += .5;
			if (player.getInstance().specAmount > 10)
				player.getInstance().specAmount = 10;
			player.getItems().addSpecialBar(player.getInstance().playerEquipment[player.getInstance().playerWeapon]);
		}
	}
}
