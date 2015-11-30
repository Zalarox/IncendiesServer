package main.game.players.events;

import main.event.Task;
import main.game.players.Player;

public class ConstitutionRestoreTask extends Task {

	private Player c;

	public ConstitutionRestoreTask(Player player) {
		super(10);
		this.c = player;
	}

	@Override
	protected void execute() {
		if ((c.getLP() < c.getMaxLP()) && (c.getLP() != 0)) {
			c.lifePoints += 1;
		}
	}
}
