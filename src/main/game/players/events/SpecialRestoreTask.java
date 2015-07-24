package main.game.players.events;

import main.event.Task;
import main.game.players.Player;

public class SpecialRestoreTask extends Task {

	private Player player;

	public SpecialRestoreTask(Player player) {
		super(35);
		this.player = player;
	}

	@Override
	protected void execute() {
		if (player.getVariables().specAmount < 10) {
			player.getVariables().specAmount += .5;
			if (player.getVariables().specAmount > 10)
				player.getVariables().specAmount = 10;
			player.getItems().addSpecialBar(player.getVariables().playerEquipment[player.getVariables().playerWeapon]);
		}
	}
}
