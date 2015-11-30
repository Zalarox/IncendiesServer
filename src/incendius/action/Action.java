package incendius.action;

import incendius.event.Task;
import incendius.game.players.Player;

/**
 * Represents a tick associated with a specific player ingame.
 * 
 * @author Advocatus
 *
 */
public abstract class Action extends Task {

	private boolean walkable;
	private Player player;

	public Action(Player player, int ticks) {
		this(player, ticks, false);
	}

	public Action(Player owner, int ticks, boolean walkable) {
		super(ticks);
		this.player = owner;
		this.walkable = walkable;
	}

	public boolean isWalkable() {
		return walkable;
	}

	public Player getPlayer() {
		return player;
	}
}
