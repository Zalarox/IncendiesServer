package main.game.players.events;

import main.event.Task;
import main.game.players.Player;

public class ConstitutionRestoreTask extends Task {

	private Player player;

	public ConstitutionRestoreTask(Player player) {
		super(10);
		this.player = player;
	}

	@Override
	protected void execute() {
		if (player.getVariables().constitution < player.getVariables().calculateMaxLifePoints(player)) {
			if (player.getVariables().constitution != 0)
				player.getVariables().constitution += 1;
		}
	}
}
