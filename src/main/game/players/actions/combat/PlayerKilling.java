package main.game.players.actions.combat;

import main.game.players.Player;

public class PlayerKilling {

	private Player c;

	public PlayerKilling(Player c) {
		this.c = c;
	}
	
	// Unique kills needed for PK Points.
	public final int NEEDED_KILLS = 3;

	// Add player to the list of killed players.
	public boolean addPlayer(String i) {
		if (!c.getInstance().killedPlayers.contains(i)) {
			c.getInstance().killedPlayers.add(i);
			return true;
		}
		return false;
	}
	
	// If the killed player is in the list, and the index is more than unique kills, remove from list.
	public void checkForPlayer(String i) {
		if (c.getInstance().killedPlayers.contains(i) && c.getInstance().killedPlayers.indexOf(i) >= NEEDED_KILLS) {
			c.getInstance().killedPlayers.remove(i);
		}
	}

}