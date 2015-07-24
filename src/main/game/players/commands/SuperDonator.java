package main.game.players.commands;

import main.game.players.Player;

public class SuperDonator {

	/**
	 * Handles Super Donator commands
	 * 
	 * @param c
	 * @param playerCommand
	 */
	public static void handleCommands(Player c, String playerCommand) {
		if (c.getVariables().playerRights >= 2 && c.getVariables().playerRights != 4
				&& c.getVariables().playerRights <= 6) {

		}
	}
}
