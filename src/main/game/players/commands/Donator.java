package main.game.players.commands;

import main.game.players.Player;
import main.game.players.packets.Commands;

public class Donator extends Commands {

	/**
	 * Handles Donator commands
	 * 
	 * @param c
	 * @param playerCommand
	 */
	public static void handleCommands(Player c, String playerCommand) {
		if (c.getVariables().playerRights >= 2 && c.getVariables().playerRights <= 6) {

		}
	}

}
