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
		if (player.getInstance().lifePoints < player.getInstance().maxLP()) {
			if (player.getInstance().lifePoints != 0)
				player.getInstance().lifePoints += 1;
		}
	}
}
