package incendius.game.players.commands;

import incendius.game.players.Player;
import incendius.game.players.packets.Commands;

public class ExtremeDonator extends Commands {

	/**
	 * Handles Extreme Donator commands
	 * 
	 * @param c
	 * @param playerCommand
	 */
	public static void handleCommands(Player c, String playerCommand) {
		if (c.getInstance().playerRights >= 2
				&& !(c.getInstance().playerRights == 4 || c.getInstance().playerRights == 5)
				&& c.getInstance().playerRights <= 6) {

		}
	}

}
