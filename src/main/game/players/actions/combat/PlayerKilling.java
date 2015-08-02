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
		if (!c.getVariables().killedPlayers.contains(i)) {
			c.getVariables().killedPlayers.add(i);
			return true;
		}
		return false;
	}
	
	// If the killed player is in the list, and the index is more than unique kills, remove from list.
	public void checkForPlayer(String i) {
		if (c.getVariables().killedPlayers.contains(i) && c.getVariables().killedPlayers.indexOf(i) >= NEEDED_KILLS) {
			c.getVariables().killedPlayers.remove(i);
		}
	}

}