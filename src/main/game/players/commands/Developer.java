package main.game.players.commands;

import main.game.players.Player;
import main.game.players.packets.Commands;

public class Developer extends Commands {

	/**
	 * Handles Developer commands
	 * 
	 * @param c
	 * @param playerCommand
	 */
	public static void handleCommands(Player c, String playerCommand) {
		if (c.getVariables().playerRights == 8) {
			if (playerCommand.startsWith("test")) {
				for (int i = 0; i < 25000; i++) {
					c.sendMessage("" + i);
					c.getPA().sendFrame126("" + i, i);
				}
			}
		}
	}
}
